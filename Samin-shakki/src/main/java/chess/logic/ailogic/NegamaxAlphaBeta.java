package chess.logic.ailogic;

import chess.domain.GameSituation;
import chess.domain.Move;
import chess.domain.board.ChessBoard;
import chess.domain.board.ChessBoardCopier;
import static chess.domain.board.Klass.PAWN;
import static chess.domain.board.Klass.QUEEN;
import chess.domain.board.Player;
import chess.logic.gamelogic.CheckingLogic;
import chess.logic.gamelogic.PromotionLogic;
import static java.lang.Integer.max;

/**
 *
 * @author sami
 */
public class NegamaxAlphaBeta implements AI {

    private GameSituation sit;
    private Move bestMove;
    private int searchDepth = 3;
    private int highestVictory = GameSituationEvaluator.victory+3;
    private boolean alphaBeta = true;

    public void setSearchDepth(int searchDepth) {
        this.searchDepth = searchDepth;
        highestVictory = GameSituationEvaluator.victory+searchDepth;
    }

    public void setAlphaBeta(boolean alphaBeta) {
        this.alphaBeta = alphaBeta;
    }

    int negaMax(int depth, Player player, int alpha, int beta) {
        if (depth == 0) {
            return GameSituationEvaluator.evaluateGameSituation(sit, player);
        } else if (CheckingLogic.checkMate(sit, player)) {
            return -123456789-depth;
        }

        int best = Integer.MIN_VALUE;
        ChessBoard backUp = ChessBoardCopier.copy(sit.getChessBoard());

        for (Move m : sit.getChessBoard().getMovementLogic().possibleMovementsByPlayer(player, sit.getChessBoard())) {
            sit.getChessBoard().getMovementLogic().move(m.getPiece(), m.getTarget(), sit);

            if (CheckingLogic.checkIfChecked(sit.getChessBoard(), player)) {
                ChessBoardCopier.undoMove(backUp, sit, m.getFrom(), m.getTarget());
                continue;
            }

            if (m.getPiece().getKlass() == PAWN && m.getPiece().isAtOpposingEnd()) {
                PromotionLogic.promote(sit, m.getPiece(), QUEEN);
            }

            int comp = -negaMax(depth - 1, Player.getOpponent(player), -beta, -alpha);
            if (comp > best) {
                best = comp;
                if (depth == searchDepth) {
                    bestMove = m;
                }
            }
            ChessBoardCopier.undoMove(backUp, sit, m.getFrom(), m.getTarget());
            if (alphaBeta) {
                alpha = max(alpha, comp);
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return best;
    }

    @Override
    public Move findBestMove(GameSituation sit) {
        this.sit = sit;
        int best = negaMax(searchDepth, sit.whoseTurn(), -highestVictory, highestVictory);
        System.out.println("Best val: " + best);
        return bestMove;
    }

    @Override
    public void setTimeLimit(long time) {
    }

    @Override
    public void reset() {
    }
}
