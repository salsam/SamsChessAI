package chess.logic.chessboardinitializers;

import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.*;
import static chess.logic.chessboardinitializers.ChessBoardInitializer.putPieceOnBoard;
import chess.logic.movementlogic.MovementLogic;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sami
 */
public class BetterChessBoardInitializerTest {

    private static ChessBoard board;
    private static ChessBoardInitializer init;

    public BetterChessBoardInitializerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        board = new ChessBoard(new MovementLogic());
        init = new BetterChessBoardInitializer();
    }

    @Before
    public void setUp() {
        init.initialize(board);
    }

    @Test
    public void pawnsOnCorrectSquares() {
        int[] columns = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
        int[] rows = new int[]{1, 6};

        testThatSquaresHavePieceOfCorrectClass(rows, columns, new Piece(PAWN, 0, 1, Player.WHITE, "wp"));
    }

    @Test
    public void rooksOnCorrectSquares() {
        int[] columns = new int[]{0, 7, 0, 7};
        int[] rows = new int[]{0, 0, 7, 7};

        testThatSquaresHavePieceOfCorrectClass(rows, columns, new Piece(ROOK, 0, 0, Player.WHITE, "wr"));
    }

    @Test
    public void knightsOnCorrectSquares() {
        int[] columns = new int[]{1, 6, 1, 6};
        int[] rows = new int[]{0, 0, 7, 7};

        testThatSquaresHavePieceOfCorrectClass(rows, columns, new Piece(KNIGHT, 1, 0, Player.WHITE, "wn"));
    }

    @Test
    public void bishopsOnCorrectSquares() {
        int[] columns = new int[]{2, 5, 2, 5};
        int[] rows = new int[]{0, 0, 7, 7};

        testThatSquaresHavePieceOfCorrectClass(rows, columns, new Piece(BISHOP, 2, 0, Player.WHITE, "wb"));
    }

    @Test
    public void kingsOnCorrectSquares() {
        int[] columns = new int[]{4, 4};
        int[] rows = new int[]{0, 7};

        testThatSquaresHavePieceOfCorrectClass(rows, columns, new Piece(KING, 4, 0, Player.WHITE, "wk"));
    }

    @Test
    public void queensOnCorrectSquares() {
        int[] columns = new int[]{3, 3};
        int[] rows = new int[]{0, 7};

        testThatSquaresHavePieceOfCorrectClass(rows, columns, new Piece(QUEEN, 3, 0, Player.WHITE, "wq"));
    }

    private void testThatSquaresHavePieceOfCorrectClass(int[] rows, int[] columns, Piece piece) {
        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < columns.length; j++) {
                assertEquals(piece.getClass(), board.getSquare(columns[i], rows[i]).getPiece().getClass());
            }
        }
    }

    public void testThatSquaresHavePieceOfCorrectOwner() {
        Player owner = Player.WHITE;
        for (int i = 0; i < board.getTable().length; i++) {
            if (i == 2) {
                i = 5;
                owner = Player.BLACK;
                continue;
            }

            for (int j = 0; j < board.getTable()[0].length; j++) {
                assertEquals(owner, board.getSquare(j, i).getPiece().getOwner());
            }
        }
    }

    public void testThatThereIsNoPiecesBetweenrowsTwoAndFive() {
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                assertFalse(board.getSquare(j, i).containsAPiece());
            }
        }
    }

    public void putPieceOnBoardPutsCorrectPieceInCorrectSpot() {
        Piece pawn = new Piece(PAWN, 5, 4, Player.WHITE, "wp");
        putPieceOnBoard(board, pawn);
        assertTrue(board.getSquare(5, 4).containsAPiece());
        assertEquals(PAWN, board.getSquare(5, 4).getPiece().getKlass());
        assertEquals(Player.WHITE, board.getSquare(5, 4).getPiece().getOwner());
    }
}
