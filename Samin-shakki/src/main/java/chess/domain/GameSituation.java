package chess.domain;

import chess.logic.movementlogic.MovementLogic;
import chess.domain.board.ChessBoard;
import chess.domain.board.Klass;
import chess.logic.chessboardinitializers.ChessBoardInitializer;
import chess.domain.board.Player;
import chess.domain.board.Square;
import static chess.domain.board.Klass.PAWN;
import chess.logic.ailogic.ZobristHasher;
import chess.logic.gamelogic.CheckingLogic;
import chess.logic.gamelogic.LegalityChecker;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for keeping track of current game situation. Class
 * offers methods to check if game has ended and whether or not one player is
 * checked. Class also offers methods to keep track of current turn and start
 * next turn.
 *
 * @author sami
 */
public class GameSituation {

    /**
     * ChessBoard of this GameSituation.
     */
    private ChessBoard board;
    /**
     * initializer that is used to initialize board when starting a new game.
     */
    private ChessBoardInitializer init;
    /**
     * Turn is a number that is used to keep track of what turn number is now.
     */
    private int turn;
    /**
     * legalityChecker is used to check if certain movements are legal on board.
     */
    private LegalityChecker legalityChecker;
    /**
     * checkingLogic is CheckingLogic used to determine whether or not a player
     * is checked.
     */
    private CheckingLogic checkLogic;
    /**
     * continues tells if this game has ended or not.
     */
    private boolean continues;

    /**
     * Keeps track of which players are ais. ais[0] is black and ais[1] is
     * white. If ais[0]=1, then black is ai etc.
     */
    private boolean[] ais;

    /**
     * Used to count how many times given situation has occurred on board.
     */
    private Map<Long, Integer> chessBoardSituationCounter;

    /**
     * Used to hash chessboard situations.
     */
    private ZobristHasher hasher;

    /**
     * Hash of current board situation.
     */
    private long boardHash;

    /**
     * Keeps track of how many moves have to be made until game is drawn by
     * 50-move rule.
     */
    private int movesTillDraw;

    /**
     * Creates a new game with given movement logic and chessboard initializer.
     *
     * @param init chessboard initializer to be used for this game
     * @param movementLogic movement logic to be used for this game
     */
    public GameSituation(ChessBoardInitializer init, MovementLogic movementLogic) {
        this.board = new ChessBoard(movementLogic);
        this.init = init;
        this.init.initialize(board);
        turn = 1;
        legalityChecker = new LegalityChecker(board);
        checkLogic = new CheckingLogic(this);
        chessBoardSituationCounter = new HashMap();
        continues = true;
        movesTillDraw = 100;
        hasher = new ZobristHasher();
        boardHash = hasher.hash(board);
        incrementCountOfCurrentBoardSituation();
        ais = new boolean[2];
    }

    /**
     * Returns player whose turn is now.
     *
     * @return white if it is whites turn else black.
     */
    public Player whoseTurn() {
        if (turn % 2 == 1) {
            return Player.WHITE;
        } else {
            return Player.BLACK;
        }
    }

    public boolean getContinues() {
        return continues;
    }

    public void setContinues(boolean continues) {
        this.continues = continues;
    }

    public LegalityChecker getChecker() {
        return legalityChecker;
    }

    public ChessBoard getChessBoard() {
        return this.board;
    }

    public CheckingLogic getCheckLogic() {
        return checkLogic;
    }

    public boolean[] getAis() {
        return this.ais;
    }

    public ZobristHasher getHasher() {
        return hasher;
    }

    public int getTurn() {
        return this.turn;
    }

    public void setBlackAI(boolean isAi) {
        this.ais[0] = isAi;
    }

    public void setWhiteAI(boolean isAi) {
        this.ais[1] = isAi;
    }

    public long getBoardHash() {
        return boardHash;
    }

    public void refresh50MoveRule() {
        movesTillDraw = 100;
    }

    public void decrementMovesTillDraw() {
        movesTillDraw--;
    }

    public void setMovesTillDraw(int moves) {
        this.movesTillDraw = moves;
    }

    public int getMovesTillDraw() {
        return movesTillDraw;
    }

    public void setAis(boolean[] ais) {
        this.ais = ais;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void setChessBoardSituationCounter(Map<Long, Integer> chessBoardSituationCounter) {
        this.chessBoardSituationCounter = chessBoardSituationCounter;
    }

    /**
     * Returns how many times current situation has been met.
     *
     * @return how many times current chessboard situation has occurred.
     */
    public int getCountOfCurrentSituation() {
        if (!chessBoardSituationCounter.containsKey(boardHash)) {
            return 0;
        }
        return chessBoardSituationCounter.get(boardHash);
    }

    /**
     * Decreases the amount of times current board situation has been met by 1.
     */
    public void decrementCountOfCurrentBoardSituation() {
        if (!chessBoardSituationCounter.containsKey(boardHash)) {
            return;
        }
        chessBoardSituationCounter.put(boardHash,
                chessBoardSituationCounter.get(boardHash) - 1);
    }

    /**
     * Increases the amount of times current board situation has been met by 1.
     */
    public void incrementCountOfCurrentBoardSituation() {
        if (!chessBoardSituationCounter.containsKey(boardHash)) {
            chessBoardSituationCounter.put(boardHash, 0);
        }
        chessBoardSituationCounter.put(boardHash,
                chessBoardSituationCounter.get(boardHash) + 1);
    }

    /**
     * Calculates hash for current chessboard situation again. If increment is
     * selected, also adds 1 to amount of times current situation has occurred.
     *
     * @param increment if true, adds 1 to count of current board situation
     * occurrences.
     */
    public void reHashBoard(boolean increment) {
        long oldHash = boardHash;
        boardHash = hasher.hash(board);
        if (increment && oldHash != boardHash) {
            incrementCountOfCurrentBoardSituation();
        }
    }

    /**
     * Updates hash for movement.
     *
     * @param from square that is moved from.
     * @param to square that is moved to.
     */
    public void updateHashForMoving(Square from, Square to) {
        boardHash = hasher.getHashAfterMove(boardHash, board, from, to);
    }

    /**
     * Updates hash for setting piece at location taken.
     *
     * @param location location of piece being taken.
     */
    public void updateHashForTakingPiece(Square location) {
        boardHash = hasher.getHashAfterPieceIsTaken(boardHash, board, location);
    }

    /**
     * Updates hash for undoing move from square from to square to.
     *
     * @param backup backup of situation before movement.
     * @param from square that is moved from.
     * @param to square that is moved to.
     */
    public void updateHashForUndoingMove(ChessBoard backup, Square from, Square to) {
        boardHash = hasher.getHashBeforeMove(boardHash, board, backup, from, to);
    }

    /**
     * Updates hash for having piece at target square promoted to chosen class.
     *
     * @param location square that piece is located on.
     * @param klass class that piece is promoted to.
     */
    public void updateHashForPromotion(Square location, Klass klass) {
        boardHash = hasher.getHashAfterPromotion(boardHash, board, location, klass);
    }

    /**
     * Updates hash for having piece at location reverted to pawn.
     *
     * @param location location of piece being reverted to pawn.
     */
    public void updateHashForUndoingPromotion(Square location) {
        boardHash = hasher.getHashBeforePromotion(boardHash, board, location);
    }

    /**
     * Sets the given chessBoard in the field board and updates LegalityChecker
     * to check that board instead of old board.
     *
     * @param chessBoard new chessboard
     */
    public void setChessBoard(ChessBoard chessBoard) {
        this.board = chessBoard;
        this.legalityChecker.setBoard(chessBoard);
    }

    /**
     * Updates the squares that current player threatens and adds 1 to turn
     * counter in field turn. Thus changing the player whose turn is now. After
     * turn has changed also makes next player's pawns no longer possible to
     * capture en passant as one turn has passed.
     */
    public void nextTurn() {
        board.updateThreatenedSquares(whoseTurn());
        turn++;
        makePawnsUnEnPassantable(whoseTurn());
    }

    /**
     * Changes the field movedTwoSquaresLastTurn to false for every pawn player
     * owns thus making them no longer possible to be captured en passant.
     *
     * @param player player
     */
    public void makePawnsUnEnPassantable(Player player) {
        board.getPieces(player).stream().forEach(piece -> {
            if (piece.getKlass() == PAWN) {
                piece.setMovedTwoSquaresLastTurn(false);
            }
        });
    }

    /**
     * Resets the game situation to beginning of the game.
     */
    public void reset() {
        continues = true;
        init.initialize(board);
        chessBoardSituationCounter.clear();
        reHashBoard(true);
        turn = 1;
    }

    /**
     *
     * @return true if current player has been checkmated.
     */
    public boolean isLost() {
        return hasLost(whoseTurn());
    }

    /**
     *
     * @param player to move.
     * @return true if player has been checkmated.
     */
    public boolean hasLost(Player player) {
        return checkLogic.checkIfCheckedAndMated(player);
    }

    /**
     *
     * @return true if current situation is a draw.
     */
    public boolean isDraw() {
        return isDrawForPlayer(whoseTurn());
    }

    /**
     *
     * @param player player who is assumed to be moving next.
     * @return true if game ended as draw assuming players turn.
     */
    public boolean isDrawForPlayer(Player player) {
        if (getCountOfCurrentSituation() > 3) {
            return true;
        }

        if (getMovesTillDraw() < 1) {
            return true;
        }

        if (checkLogic.stalemate(player)) {
            return true;
        }

        return checkLogic.insufficientMaterial();
    }

    /**
     *
     * @return true if game has ended.
     */
    public boolean isEnded() {
        return isDraw() || isEnded();
    }

    /**
     *
     * @param player to move.
     * @return true if game has ended when player is next to move.
     */
    public boolean isEndedForPlayer(Player player) {
        return isDrawForPlayer(player) || hasLost(player);
    }

    /**
     * Clones current GameSituations.
     *
     * @return new GameSituation only reusing movementLogic and
     * ChessBoardInitializer.
     */
    public GameSituation clone() {
        GameSituation copy = new GameSituation(init, board.getMovementLogic());
        copy.setTurn(turn);
        copy.setMovesTillDraw(movesTillDraw);
        copy.setChessBoardSituationCounter(chessBoardSituationCounter);
        copy.setAis(ais);
        return copy;
    }
}
