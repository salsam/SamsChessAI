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

/**
 *
 * @author sami
 */
public class SimpleNegamax implements AI {

    private GameSituation sit;
    private Move bestMove;
    private final int plies = 10;
    private int searchDepth = 3;

    public void setSearchDepth(int searchDepth) {
        this.searchDepth = searchDepth;
    }

    int negaMax(int depth, Player player) {
        if (depth == 0) {
            return GameSituationEvaluator.evaluateGameSituation(sit, player);
        }

        int best = Integer.MIN_VALUE;
        int movesTillDraw = sit.getMovesTillDraw();
        ChessBoard backUp = ChessBoardCopier.copy(sit.getChessBoard());

        for (Move m : sit.getChessBoard().getMovementLogic().possibleMovementsByPlayer(player, sit.getChessBoard())) {
            sit.getChessBoard().getMovementLogic().move(m.getPiece(), m.getTarget(), sit);

            if (CheckingLogic.checkIfChecked(sit.getChessBoard(), player)) {
                ChessBoardCopier.undoMove(backUp, sit, m.getFrom(), m.getTarget(), movesTillDraw);
                continue;
            }

            if (m.getPiece().getKlass() == PAWN && m.getPiece().isAtOpposingEnd()) {
                PromotionLogic.promote(sit, m.getPiece(), QUEEN);
            }

            int comp = -negaMax(depth - 1, Player.getOpponent(player));
            if (comp > best) {
                best = comp;
                if (depth == searchDepth) {
                    bestMove = m;
                }
            }
            ChessBoardCopier.undoMove(backUp, sit, m.getFrom(), m.getTarget(), movesTillDraw);
        }

        return best;
    }

    @Override
    public Move findBestMove(GameSituation sit) {
        this.sit = sit;
        int best = negaMax(searchDepth, sit.whoseTurn());
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
