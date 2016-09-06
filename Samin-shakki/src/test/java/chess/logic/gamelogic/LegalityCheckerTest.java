package chess.logic.gamelogic;

import chess.logic.movementlogic.MovementLogic;
import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.PAWN;
import static chess.logic.chessboardinitializers.ChessBoardInitializer.putPieceOnBoard;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sami
 */
public class LegalityCheckerTest {

    ChessBoard board;
    LegalityChecker checker;

    public LegalityCheckerTest() {
    }

    @Before
    public void setUp() {
        board = new ChessBoard(new MovementLogic());
        checker = new LegalityChecker(board);
    }

    @Test
    public void checkPlayerOwnsAPieceOnTargetSquareReturnsFalseIfSquareEmpty() {
        assertFalse(checker.checkPlayerOwnsPieceOnTargetSquare(Player.WHITE, 3, 3));
    }

    @Test
    public void checkPlayerOwnsAPieceOnTargetSquareReturnsFalseIfTargetSquareContainsOpposingPiece() {
        putPieceOnBoard(board, new Piece(PAWN, 2, 2, Player.WHITE, "wp"));
        assertFalse(checker.checkPlayerOwnsPieceOnTargetSquare(Player.BLACK, 2, 2));
    }

    @Test
    public void checkPlayerOwnsAPieceOnTargetSquareReturnsTrueIfTargetSquareContainsOwnPiece() {
        putPieceOnBoard(board, new Piece(PAWN, 2, 2, Player.WHITE, "wp"));
        assertTrue(checker.checkPlayerOwnsPieceOnTargetSquare(Player.WHITE, 2, 2));
    }
}
