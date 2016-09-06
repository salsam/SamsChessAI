package chess.logic.gamelogic;

import chess.domain.GameSituation;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.PAWN;
import static chess.domain.board.Klass.QUEEN;

/**
 *
 * @author sami
 */
public class PromotionLogic {

    public static void promotePiece(Piece piece, GameSituation sit) {
        if (piece.getKlass() == PAWN && !piece.isTaken()) {
            if (piece.isAtOpposingEnd()) {
                sit.updateHashForPromotion(
                        sit.getChessBoard().getSquare(piece.getColumn(), piece.getRow()));
                piece.setKlass(QUEEN);
            }
        }
    }
}
