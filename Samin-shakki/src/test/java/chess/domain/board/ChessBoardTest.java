package chess.domain.board;

import static chess.domain.board.Klass.QUEEN;
import chess.logic.chessboardinitializers.ChessBoardInitializer;
import static chess.logic.chessboardinitializers.ChessBoardInitializer.putPieceOnBoard;
import chess.logic.movementlogic.MovementLogic;
import chess.logic.chessboardinitializers.StandardChessBoardInitializer;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sami
 */
public class ChessBoardTest {

    private ChessBoard board;
    private static ChessBoardInitializer init;

    public ChessBoardTest() {
        init = new StandardChessBoardInitializer();
    }

    @Before
    public void setUp() {
        board = new ChessBoard(new MovementLogic());
    }

    @Test
    public void withinTableReturnTrueIfSquareWithinTable() {
        assertTrue(board.withinTable(3, 6));
    }

    @Test
    public void withinTableReturnTrueIfSquareOnTheEdge() {
        assertTrue(board.withinTable(0, 0));
    }

    @Test
    public void withinTableReturnFalseIfSquareNotWithinTable() {
        assertFalse(board.withinTable(-1, 5));
    }

    @Test
    public void blackThreatenedSquaresReturnsAllSquaresThreatenedByBlackInStandardStart() {
        init.initialize(board);
        Set<Square> correct = new HashSet();
        int[] rows = new int[]{0, 0, 0, 0, 0, 0};
        int[] cols = new int[]{1, 2, 3, 4, 5, 6};

        for (int i = 0; i < cols.length; i++) {
            correct.add(new Square(cols[i], rows[i]));
        }

        for (int i = 1; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                correct.add(new Square(j, i));
            }
        }

        board.updateThreatenedSquares(Player.BLACK);
        for (Square possible : correct) {
            assertTrue(board.threatenedSquares(Player.BLACK).contains(possible));
        }
    }

    @Test
    public void blackThreatenedSquaresReturnsOnlyThreatenedSquares() {
        init.initialize(board);
        Set<Square> wrong = new HashSet();
        int[] cols = new int[]{0, 7};
        int[] rows = new int[]{0, 0};

        for (int i = 0; i < cols.length; i++) {
            wrong.add(new Square(cols[i], rows[i]));
        }

        for (int i = 3; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                wrong.add(new Square(j, i));
            }
        }

        board.updateThreatenedSquares(Player.BLACK);
        for (Square possible : wrong) {
            assertFalse(board.threatenedSquares(Player.BLACK).contains(possible));
        }
    }

    @Test
    public void whiteThreatenedSquaresReturnsAllSquaresThreatenedByWhiteInStandardStart() {
        init.initialize(board);
        Set<Square> correct = new HashSet();
        int[] rows = new int[]{7, 7, 7, 7, 7, 7};
        int[] cols = new int[]{1, 2, 3, 4, 5, 6};

        for (int i = 0; i < cols.length; i++) {
            correct.add(new Square(cols[i], rows[i]));
        }

        for (int i = 5; i < 7; i++) {
            for (int j = 0; j < 8; j++) {
                correct.add(new Square(j, i));
            }
        }

        board.updateThreatenedSquares(Player.WHITE);
        for (Square possible : correct) {
            assertTrue(board.threatenedSquares(Player.WHITE).contains(possible));
        }
    }

    @Test
    public void whiteThreatenedSquaresReturnsOnlySquaresThreatenedByWhite() {
        init.initialize(board);
        Set<Square> wrong = new HashSet();
        int[] cols = new int[]{0, 7};
        int[] rows = new int[]{7, 7};

        for (int i = 0; i < cols.length; i++) {
            wrong.add(new Square(cols[i], rows[i]));
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 8; j++) {
                wrong.add(new Square(j, i));
            }
        }

        board.updateThreatenedSquares(Player.WHITE);
        for (Square possible : wrong) {
            assertFalse(board.threatenedSquares(Player.WHITE).contains(possible));
        }
    }

    @Test
    public void whiteThreatenedSquaresWorksInMoreComplexSituation() {
        init.initialize(board);
        putPieceOnBoard(board, new Piece(QUEEN, 4, 4, Player.WHITE, "wq1"));
        Piece q = board.getPiece(4, 4);
        board.updateThreatenedSquares(Player.WHITE);
        for (Square sq : board.getMovementLogic().threatenedSquares(q, board)) {
            assertTrue(board.threatenedSquares(Player.WHITE).contains(sq));
        }
    }

    @Test
    public void getKingsReturnsMapThatContainsKingLocations() {
        init.initialize(board);
        Piece whiteKing = board.getKings().get(Player.WHITE);
        Piece blackKing = board.getKings().get(Player.BLACK);
        assertEquals(new Square(4, 7), whiteKing.getLocation());
        assertEquals(new Square(4, 0), blackKing.getLocation());
    }
}
