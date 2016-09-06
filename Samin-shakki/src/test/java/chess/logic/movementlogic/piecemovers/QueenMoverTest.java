package chess.logic.movementlogic.piecemovers;

import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import chess.domain.board.Square;
import chess.domain.board.SquareTest;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.PAWN;
import static chess.domain.board.Klass.QUEEN;
import chess.logic.chessboardinitializers.ChessBoardInitializer;
import static chess.logic.chessboardinitializers.ChessBoardInitializer.putPieceOnBoard;
import chess.logic.chessboardinitializers.EmptyBoardInitializer;
import chess.logic.movementlogic.MovementLogic;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author sami
 */
public class QueenMoverTest {

    private Piece queen;
    private static ChessBoard board;
    private static ChessBoardInitializer init;
    private static QueenMover queenMover;

    public QueenMoverTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        board = new ChessBoard(new MovementLogic());
        init = new EmptyBoardInitializer();
        queenMover = new QueenMover();
    }

    @Before
    public void setUp() {
        init.initialize(board);
        queen = new Piece(QUEEN, 3, 5, Player.WHITE, "wq");
        putPieceOnBoard(board, queen);
    }

    @Test
    public void queenCannotStayStill() {
        assertFalse(queenMover.possibleMoves(queen, board).contains(new Square(3, 5)));
    }

    @Test
    public void queenCanMoveHorizontallyToEveryNonBlockedSquare() {
        int[] columns = new int[]{0, 1, 2, 4, 5, 6, 7};

        for (int i = 0; i < columns.length; i++) {
            assertTrue(queenMover.possibleMoves(queen, board).contains(new Square(columns[i], 5)));
        }
    }

    @Test
    public void queenCanMoveVerticallyToEveryNonBlockedSquare() {
        int[] rows = new int[]{0, 1, 2, 3, 4, 6, 7};

        for (int i = 0; i < rows.length; i++) {
            assertTrue(queenMover.possibleMoves(queen, board).contains(new Square(3, rows[i])));
        }
    }

    @Test
    public void queenCanMoveNorthEastToEveryPossibleSquareOnBoard() {
        int[] columns = new int[]{4, 5};
        int[] rows = new int[]{6, 7};

        for (int i = 0; i < columns.length; i++) {
            assertTrue(queenMover.possibleMoves(queen, board).contains(new Square(columns[i], rows[i])));
        }
    }

    @Test
    public void queenCanMoveNorthWestToEveryPossibleSquareOnBoard() {
        int[] columns = new int[]{2, 1};
        int[] rows = new int[]{6, 7};

        SquareTest.testMultipleSquares(columns, rows, queenMover.possibleMoves(queen, board));
    }

    @Test
    public void queenCanMoveSouthEastToEveryPossibleSquareOnBoard() {
        int[] columns = new int[]{4, 5, 6, 7};
        int[] rows = new int[]{4, 3, 2, 1};

        SquareTest.testMultipleSquares(columns, rows, queenMover.possibleMoves(queen, board));
    }

    @Test
    public void queenCanMoveSouthWestToEveryPossibleSquareOnBoard() {
        int[] columns = new int[]{2, 1, 0};
        int[] rows = new int[]{4, 3, 2};

        SquareTest.testMultipleSquares(columns, rows, queenMover.possibleMoves(queen, board));
    }

    @Test
    public void queenCannotOnTopOfOwnPiece() {
        Piece pawn = new Piece(PAWN, 3, 1, Player.WHITE, "wp");
        putPieceOnBoard(board, pawn);
        assertFalse(queenMover.possibleMoves(queen, board).contains(new Square(3, 1)));
    }

    @Test
    public void queenCanMoveOnTopOfEnemyPiece() {
        Piece pawn = new Piece(PAWN, 3, 1, Player.BLACK, "bp");
        putPieceOnBoard(board, pawn);
        assertTrue(queenMover.possibleMoves(queen, board).contains(new Square(3, 1)));
    }

    @Test
    public void queenCannotMovePastAPiece() {
        Piece pawn = new Piece(PAWN, 3, 1, Player.WHITE, "wp");
        putPieceOnBoard(board, pawn);
        assertFalse(queenMover.possibleMoves(queen, board).contains(new Square(3, 0)));
    }

    @Test
    public void queenCannotMovePastOpposingPiece() {
        Piece pawn = new Piece(PAWN, 3, 1, Player.BLACK, "bp");
        putPieceOnBoard(board, pawn);
        assertFalse(queenMover.possibleMoves(queen, board).contains(new Square(3, 0)));
    }
}
