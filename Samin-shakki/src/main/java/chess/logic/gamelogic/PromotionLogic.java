package chess.logic.gamelogic;

import chess.domain.GameSituation;
import chess.domain.board.Klass;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.*;

/**
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
}
