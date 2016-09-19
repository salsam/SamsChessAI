package chess.logic.gamelogic;

import chess.domain.GameSituation;
import chess.domain.board.Klass;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.PAWN;

/**
 * This class is responsible promotion related logic.
 *
 * @author sami
 */
public class PromotionLogic {

    public static void promote(GameSituation sit, Piece piece, Klass klass) {
        if (piece.getKlass() == PAWN && !piece.isTaken()) {
            if (piece.isAtOpposingEnd()) {
                sit.updateHashForPromotion(
                        sit.getChessBoard().getSquare(piece.getColumn(), piece.getRow()), klass);
                piece.setKlass(klass);
            }
        }
    }

    public static void undoPromotion(GameSituation sit, Piece piece) {
        sit.updateHashForUndoingPromotion(
                sit.getChessBoard().getSquare(piece.getColumn(), piece.getRow()));
        piece.setKlass(PAWN);
    }
}
