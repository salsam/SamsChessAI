package chess.logic.ailogic;

import chess.domain.GameSituation;
import chess.domain.Move;
import static chess.domain.board.ChessBoardCopier.copy;
import static chess.domain.board.ChessBoardCopier.undoMove;
import static chess.domain.board.Player.getOpponent;
import static chess.logic.ailogic.GameSituationEvaluator.evaluateGameSituation;
import chess.logic.movementlogic.MovementLogic;
import chess.domain.datastructures.*;
import chess.domain.board.*;
import static chess.domain.board.Klass.PAWN;
import static chess.domain.board.Klass.QUEEN;
import static chess.logic.ailogic.GameSituationEvaluator.getValue;
import chess.logic.gamelogic.PromotionLogic;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is responsible for calculating AI's next move and then returning
 * it when asked for next move. All values are measured in centipawns that is
 * one hundredth of pawn's value. Uses negamax sped up with alpha-beta pruning
 * and transposition tables. Alpha-beta pruning also is sped up by principal
 * variation and killer move heuristics. Also tests captures before positional
 * moves.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Negamax">Negamax</a>
 *
 * @see <a href="https://en.wikipedia.org/wiki/Alpha-beta_pruning">Alpha-beta
 * pruning</a>
 *
 * @see
 * <a href="https://chessprogramming.wikispaces.com/Principal+variation">Principal
 * variation</a>
 *
 * @see
 * <a href="https://en.wikipedia.org/wiki/Transposition_table">Transposition
 * table</a>
 *
 * @author sami
 */
public class AILogic implements AI {

    private GameSituation sit;
    private MovementLogic ml;
    private List<Move> bestMoves;
    private int[] bestValues;
    private long timeLimit;
    private long start;
    private final int plies = 10;
    private int lastPlies;
    private int searchDepth;
    private int oldestIndex;
    private Pair<Integer, Move[]> lastPrincipalVariation;
    private Move[] principalMoves;
    private Move[] killerCandidates;
    private Move[][] killerMoves;
    private TranspositionTable transpositionTable;
    private long sum = 0;
    private int count = 0;

    public AILogic() {
        bestValues = new int[plies + 1];
        bestMoves = new ArrayList();
        killerCandidates = new Move[plies];
        killerMoves = new Move[plies][3];
        lastPlies = 0;
        oldestIndex = 0;
        principalMoves = new Move[plies];
        searchDepth = 3;
        timeLimit = 1000;
        transpositionTable = new TranspositionTable();
    }

    public int[] getBestValues() {
        return bestValues;
    }

    public Move[] getKillerCandidates() {
        return killerCandidates;
    }

    public void setSearchDepth(int searchDepth) {
        this.searchDepth = searchDepth;
    }

    public void setSituation(GameSituation sit) {
        this.sit = sit;
        this.ml = sit.getChessBoard().getMovementLogic();
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getTimeLimit() {
        return timeLimit;
    }

    /**
     * Sets new time limit for ai. This will be how long ai can think of next
     * move (measured in milliseconds).
     *
     * @param newTimeLimit new time limit for chosen ai.
     */
    public void setTimeLimit(long newTimeLimit) {
        this.timeLimit = newTimeLimit;
    }

    public void reset() {
        bestValues = new int[plies + 1];
        bestMoves.clear();
        killerCandidates = new Move[plies];
        killerMoves = new Move[plies][3];
        lastPlies = 0;
        oldestIndex = 0;
        principalMoves = new Move[plies];
        transpositionTable.clear();

        for (int i = 0; i < plies; i++) {
            bestValues[i] = 0;
            killerCandidates[i] = null;
            principalMoves[i] = null;
            for (int j = 0; j < 3; j++) {
                killerMoves[i][j] = null;
            }
        }
        bestValues[plies] = 0;
    }

    /**
     * Returns a random move with highest associated value.
     *
     * @return random best move
     */
    public Move getBestMove() {
        return bestMoves.get(new Random().nextInt(bestMoves.size()));
    }

    /**
     * This method is used to calculate best move for ai in given game situation
     * using negaMax sped up with alpha-beta pruning. For each depth
     * corresponding bestValue is initialized to -12456789 representing negative
     * infinity. If leaf isn't reached yet, method will recurse forward by
     * calling helper method tryAllPossibleMoves.
     *
     * If transposition table already contains results of at least as deep
     * search, we can use those results to speed up search. If results are of
     * type alpha, we know our search will find values of at least that value.
     * For beta the opposite holds true and if type is exact, we can immediately
     * return the value.
     *
     * @param height Height from leaf nodes.
     * @param alpha current alpha value.
     * @param maxingPlayer player whose best move is being figured out
     * @param beta current beta value.
     * @return highest value associated with any move.
     */
    public int negaMax(int height, int alpha, int beta, Player maxingPlayer) {
        int ogAlpha = alpha;
        if (System.currentTimeMillis() - start >= timeLimit) {
            return -123456790;
        }

        TranspositionKey key = new TranspositionKey(maxingPlayer, sit.getBoardHash());

        if (transpositionTable.containsRelevantKey(key, height)) {
            TranspositionEntry entry = transpositionTable.get(key);
            transpositionTable.makeSaved(key);
            switch (entry.getType()) {
                case EXACT:
                    return entry.getValue();

                case ALPHA:
                    alpha = Math.max(alpha, entry.getValue());
                    break;

                case BETA:
                    beta = Math.min(beta, entry.getValue());
                    break;
            }
            if (alpha >= beta) {
                return entry.getValue();
            }
        }
        if (height == 0) {
            int value = evaluateGameSituation(sit, maxingPlayer);
            sit.setContinues(true);
            transpositionTable.put(key, new TranspositionEntry(height, value, Type.EXACT));
            return value;
        }
        return tryAllPossibleMoves(height, ogAlpha, alpha, maxingPlayer, beta);
    }

    /**
     * Tries making all possible moves for maxing player and saves highest value
     * associated with a move in table bestValues. First initializes highest
     * value of current height (node) to -123456789 (acting as minus infinity).
     * Then starts by testing principal variation that is best move for
     * recursion depth based on earlier iterations followed by killer moves for
     * this recursion depth. Third we try all captures and last all positional
     * moves (rest). Also principal variation and killer moves won't be tested
     * again to speed up search.
     *
     * If tested move doesn't produce beta-cutoff, move is saved as candidate
     * for being killer move. If beta-cutoff was reached, last killer candidate
     * will be saved as new killer move assuming such exists.
     *
     * LoopCount keeps track of which of 4 loopthroughs is happening. First loop
     * through only considers winning captures.
     *
     * @param height recursion depth left (height from leaves).
     * @param ogAlpha original alpha value at this height.
     * @param alpha current alpha-value.
     * @param maxingPlayer player whose turn it is.
     * @param beta current beta-value.
     * @return highest value associated with all legal moves.
     */
    public int tryAllPossibleMoves(int height, int ogAlpha, int alpha, Player maxingPlayer, int beta) {
        bestValues[height] = -123456789;
        ChessBoard backUp = copy(sit.getChessBoard());
        alpha = testPrincipalMove(height, maxingPlayer, ogAlpha, alpha, beta, backUp);
        alpha = testKillerMoves(height, maxingPlayer, ogAlpha, alpha, beta, backUp);

        for (int loopCount = 0; loopCount < 4; loopCount++) {
            for (Piece piece : sit.getChessBoard().getPieces(maxingPlayer)) {
                if (alpha >= beta || System.currentTimeMillis() - start >= timeLimit) {
                    break;
                }

                if (piece.isTaken()) {
                    continue;
                }

                Square from = new Square(piece.getColumn(), piece.getRow());
                alpha = tryMovingPiece(height, loopCount, piece, from, ogAlpha, alpha, beta, maxingPlayer, backUp);
            }
        }
        return bestValues[height];
    }

    /**
     * Tries moving chosen piece to each possible square on chessboard. If first
     * run through is going on (i==0) only tests captures while on second only
     * tests positional moves (rest).
     *
     * If tested move doesn't produce beta-cutoff, move is saved as candidate
     * for being killer move. If beta-cutoff was reached, last killer candidate
     * will be saved as new killer move assuming such exists.
     *
     * @param moved piece being moved.
     * @param loopCount looping count.
     * @param height height from leaf nodes.
     * @param alpha current alpha value.
     * @param ogAlpha original alpha value for this depth.
     * @param maxingPlayer player whose turn it is.
     * @param beta current beta value.
     * @param backUp backup of situation before moving chosen piece.
     * @param from square piece is located in before movement.
     * @return new alpha value of situation.
     */
    public int tryMovingPiece(int height, int loopCount, Piece moved, Square from, int ogAlpha, int alpha, int beta, Player maxingPlayer, ChessBoard backUp) {

        for (Square possibility : ml.possibleMoves(moved, sit.getChessBoard())) {

            if (System.currentTimeMillis() - start >= timeLimit) {
                break;
            }

            if (!handledOnThisLoopThrough(loopCount, possibility, height, moved, sit.getChessBoard())) {
                continue;
            }

            alpha = testAMove(moved, possibility, from, maxingPlayer, height, ogAlpha, alpha, beta, backUp);

            if (alpha >= beta) {
                saveNewKillerMove(height);
                break;
            }
            killerCandidates[searchDepth - height] = new Move(moved, possibility);
        }

        return alpha;
    }

    /*
    *First loop handles winning captures.
    *Second handles neutral captures.
    *Third considers losing captures.
    *Last considers positional moves.
     */
    private boolean handledOnThisLoopThrough(int loopCount, Square possibility, int height,
            Piece moved, ChessBoard board) {
        if ((loopCount < 3 && !possibility.containsAPiece())
                || (loopCount == 3 && possibility.containsAPiece())
                || moveHasBeenTestedAlready(height, moved, possibility)) {
            return false;
        }

        if (loopCount < 3) {
            int movedVal = getValue(sit, moved);
            int capVal = getValue(sit, board.getPiece(possibility));
            if ((loopCount == 0 && capVal <= movedVal)
                    || (loopCount == 1 && capVal != movedVal)
                    || (loopCount == 2 && capVal >= movedVal)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Moves chosen piece to chosen square, checks if this changes alpha and
     * undoes move that was made.
     *
     * @param piece piece being moved.
     * @param possibility where piece is moved to.
     * @param alpha current alpha-value.
     * @param maxingPlayer player whose turn it is to move a piece.
     * @param height height from leaves.
     * @param ogAlpha original alpha value for this depth.
     * @param beta current beta-value.
     * @param backUp backUp of situation before move is made.
     * @param from square where moved piece is located before move.
     * @return alpha value after testing chosen move.
     */
    public int testAMove(Piece piece, Square possibility, Square from,
            Player maxingPlayer, int height, int ogAlpha, int alpha, int beta, ChessBoard backUp) {
        if (System.currentTimeMillis() - start >= timeLimit) {
            return alpha;
        }

        ml.move(piece, possibility, sit);
        if (piece.getKlass() == PAWN && piece.isAtOpposingEnd()) {
            sit.decrementCountOfCurrentBoardSituation();
            PromotionLogic.promote(sit, piece, QUEEN);
            sit.incrementCountOfCurrentBoardSituation();
        }
        alpha = checkForChange(piece, possibility, height, maxingPlayer, ogAlpha, alpha, beta);
        undoMove(backUp, sit, from, possibility);
        sit.setContinues(true);
        return alpha;
    }

    /**
     * After testing principal variation the next likely moves to be best are
     * killer moves so they are tested next. Killer moves are moves that caused
     * a (alpha-)beta-cutoff in a node at same depth and they are changed every
     * time new one is found.
     *
     * @param backUp backup of situation on chessboard before move.
     * @param height recursion depth left counted in turns.
     * @param maxingPlayer player whose turn it is.
     * @param alpha current alpha value.
     * @param beta current beta value.
     * @return alpha value after testing killer moves.
     */
    private int testKillerMoves(int height, Player maxingPlayer, int ogAlpha, int alpha, int beta, ChessBoard backUp) {
        for (int i = 0; i < 3; i++) {
            Move killer = killerMoves[searchDepth - height][i];
            if (killer == null) {
                continue;
            }
            Piece piece = killer.getPiece();
            Square from = new Square(piece.getColumn(), piece.getRow());
            Square to = killer.getTarget();

            if (piece.deepEquals(sit.getChessBoard().getPiece(from))) {
                if (ml.possibleMoves(piece, sit.getChessBoard()).contains(to)) {
                    alpha = testAMove(piece, to, from, maxingPlayer, height, ogAlpha, alpha, beta, backUp);
                }
            }
        }

        return alpha;
    }

    /**
     * Tries making principal move first. Principal move is assumed to be best
     * move because of earlier iteration thus testing it first should in most
     * cases increase alpha value asap causing alpha-beta pruning to cut more
     * branches.
     *
     * @param backUp backup of situation on chessboard before move.
     * @param height recursion depth left in turns.
     * @param maxingPlayer player whose turn it is.
     * @param alpha current alpha value.
     * @param beta current beta value.
     * @return alpha value after testing principal move.
     */
    private int testPrincipalMove(int height, Player maxingPlayer, int ogAlpha, int alpha, int beta, ChessBoard backUp) {
        if (principalMoves[searchDepth - height] != null) {
            Piece piece = principalMoves[searchDepth - height].getPiece();
            Square from = new Square(piece.getColumn(), piece.getRow());
            Square to = principalMoves[searchDepth - height].getTarget();

            if (piece.deepEquals(sit.getChessBoard().getPiece(from))) {
                if (ml.possibleMoves(piece, sit.getChessBoard()).contains(to)) {
                    alpha = testAMove(piece, to, from, maxingPlayer, height, ogAlpha, alpha, beta, backUp);
                }
            }
        }

        return alpha;
    }

    /**
     * Checks if killer move candidate exists and is not already saved as
     * principal variation or killer move before saving killer candidate as new
     * killer move. Replaces oldest killer move with current killer candidate.
     *
     * @param height height from leaves.
     */
    private void saveNewKillerMove(int height) {
        if (killerCandidates[searchDepth - height] != null
                && !moveHasBeenTestedAlready(height,
                        killerCandidates[searchDepth - height].getPiece(),
                        killerCandidates[searchDepth - height].getTarget())) {
            killerMoves[searchDepth - height][oldestIndex] = killerCandidates[searchDepth - height];
            oldestIndex = (oldestIndex + 1) % 3;
            killerCandidates[searchDepth - height] = null;
        }
    }

    /**
     * Checks whether or not chosen move is already saved as killer move or in
     * principal variation at current height.
     *
     * @param height height from leaves.
     * @param piece piece being moved.
     * @param possibility square piece is moved to.
     * @return true if move has been tested already.
     */
    private boolean moveHasBeenTestedAlready(int height, Piece piece, Square possibility) {
        Move tested = new Move(piece, possibility);
        for (int i = 0; i < 3; i++) {
            if (tested.equals(killerMoves[searchDepth - height][i])) {
                return true;
            }
        }

        return tested.equals(principalMoves[searchDepth - height]);
    }

    /**
     * Checks if movement was legal and then recurses forward by calling
     * negamax. Updates bestValue for depth and alpha value if necessary. In
     * negamax call alpha and beta are swapped and their signs are changed to
     * use formula max(a,b)=-min(-a,-b) thus preventing need of separate max and
     * min methods. This is also why value is set to -negamax. Saves current
     * board situation with given iteration depth left in transposition table
     * for future use.
     *
     * @param maxingPlayer player who's maxing value of situation this turn.
     * @param height height in game tree.
     * @param alpha previous alpha value.
     * @param beta beta value.
     * @param ogAlpha original alpha value for this depth.
     * @param piece piece that was moved.
     * @param possibility square that piece was moved to.
     * @return new alpha value.
     */
    public int checkForChange(Piece piece, Square possibility, int height, Player maxingPlayer, int ogAlpha, int alpha, int beta) {

        if (sit.getCheckLogic().checkIfChecked(maxingPlayer)) {
            return alpha;
        }
        int value = -negaMax(height - 1, -beta, -alpha, getOpponent(maxingPlayer));
        addSituationToTranpositionTable(maxingPlayer, height, value, ogAlpha, beta);

        if (value >= bestValues[height]) {
            keepTrackOfBestMoves(height, value, piece, possibility);
            bestValues[height] = value;
        }
        if (value > alpha) {
            alpha = value;
            principalMoves[searchDepth - height] = new Move(piece, possibility);
        }
        return alpha;
    }

    private void addSituationToTranpositionTable(Player maxingPlayer, int height, int value, int ogAlpha, int beta) {
        TranspositionKey key = new TranspositionKey(maxingPlayer, sit.getBoardHash());
        TranspositionEntry entry = new TranspositionEntry(height, value, Type.ALPHA);

        if (bestValues[height] <= ogAlpha) {
            entry.setType(Type.BETA);
        } else if (bestValues[height] >= beta) {
            entry.setType(Type.ALPHA);
        } else {
            entry.setType(Type.EXACT);
        }
        transpositionTable.put(key, entry);
    }

    /**
     * Saves best first moves in an arraylist and if better move is found clears
     * the list of previous moves.
     *
     * @param height depth in game tree.
     * @param value value of situation.
     * @param piece piece that was moved.
     * @param possibility square piece was moved to.
     */
    private void keepTrackOfBestMoves(int height, int value, Piece piece, Square possibility) {
        if (height == searchDepth) {
            if (value > bestValues[height]) {
                bestMoves.clear();
            }
            bestMoves.add(new Move(piece, possibility));
        }
    }

    /**
     * This method is used to calculate best move for player whose turn it is
     * now in given game situation. Uses iterative deepening to speed up
     * alpha-beta thus looping over search depths from 1 to wanted depth. If
     * value of best move so far has greater absolute value than 20000, we know
     * that either player will inevitably win the game in i moves and thus
     * there's no need to loop further.
     *
     * @param situation game situation at the beginning of AI's turn.
     */
    public void findBestMoves(GameSituation situation) {
        start = System.currentTimeMillis();
        sit = situation;
        transpositionTable.makePairsUnsaved();
        ml = sit.getChessBoard().getMovementLogic();
        salvageLastPrincipalVariation();
        int i = 1;
        for (; i <= plies; i++) {
            searchDepth = i;
            negaMax(i, -123456789, 123456789, situation.whoseTurn());
            lastPlies++;
            if (System.currentTimeMillis() - start >= timeLimit
                    || Math.abs(bestValues[i]) > 20000) {
                break;
            }
        }
        //System.out.println("Recursion depth: " + i);

        lastPrincipalVariation = new Pair(sit.getTurn(), principalMoves);
    }

    /*
    Find best move for current player in this game situation.
     */
    public Move findBestMove(GameSituation situation) {
        findBestMoves(situation);
        return getBestMove();
    }

    /**
     * Sets matching part of last principal variation as start for current one.
     * So current principal variation will be moves in last one minus the moves
     * that - probably - have been made. If turnsSince is negative (we've reset
     * game board), all killer moves and principal moves are cleared to avoid
     * wrong information.
     */
    private void salvageLastPrincipalVariation() {
        if (lastPrincipalVariation != null) {
            int turnsSince = sit.getTurn() - lastPrincipalVariation.getFirst();
            for (int i = 0; i < plies; i++) {
                if (turnsSince > 0 && i < lastPlies - turnsSince) {
                    principalMoves[i] = lastPrincipalVariation.getSecond()[i + turnsSince];
                    for (int j = 0; j < 3; j++) {
                        killerMoves[i][j] = killerMoves[i + turnsSince][j];
                    }
                } else {
                    for (int j = 0; j < 3; j++) {
                        killerMoves[i][j] = null;
                    }
                    principalMoves[i] = null;
                }
            }
        }
        lastPlies = 0;
    }

}
