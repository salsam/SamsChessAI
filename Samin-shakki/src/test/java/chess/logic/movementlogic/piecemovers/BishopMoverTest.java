package chess.logic.movementlogic.piecemovers;

import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import chess.domain.board.Square;
import chess.domain.board.SquareTest;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.BISHOP;
import static chess.domain.board.Klass.PAWN;
import chess.logic.chessboardinitializers.ChessBoardInitializer;
import static chess.logic.chessboardinitializers.ChessBoardInitializer.putPieceOnBoard;
import chess.logic.chessboardinitializers.EmptyBoardInitializer;
import chess.logic.movementlogic.MovementLogic;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author sami
 */
public class BishopMoverTest {

    private Piece bishop;
    private static BishopMover bishopMover;
    private static ChessBoard board;
    private static ChessBoardInitializer init;

    public BishopMoverTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        board = new ChessBoard(new MovementLogic());
        bishopMover = new BishopMover();
        init = new EmptyBoardInitializer();
    }

    @Before
    public void setUp() {
        init.initialize(board);
        bishop = new Piece(BISHOP, 3, 4, Player.WHITE, "wb");
        putPieceOnBoard(board, bishop);
    }

    @Test
    public void bishopCannotStayStillWhenMoving() {
        assertFalse(bishopMover.possibleMoves(bishop, board).contains(new Square(3, 4)));
    }

    @Test
    public void bishopCanMoveNorthEastToEveryPossibleSquareOnBoard() {
        int[] columns = new int[]{4, 5, 6};
        int[] rows = new int[]{5, 6, 7};

        SquareTest.testMultipleSquares(columns, rows, bishopMover.possibleMoves(bishop, board));
    }

    @Test
    public void bishopCanMoveNorthWestToEveryPossibleSquareOnBoard() {
        int[] columns = new int[]{2, 1, 0};
        int[] rows = new int[]{5, 6, 7};

        SquareTest.testMultipleSquares(columns, rows, bishopMover.possibleMoves(bishop, board));
    }

    @Test
    public void bishopCanMoveSouthEastToEveryPossibleSquareOnBoard() {
        int[] columns = new int[]{4, 5, 6, 7};
        int[] rows = new int[]{3, 2, 1, 0};

        SquareTest.testMultipleSquares(columns, rows, bishopMover.possibleMoves(bishop, board));
    }

    @Test
    public void bishopCanMoveSouthWestToEveryPossibleSquareOnBoard() {
        int[] columns = new int[]{2, 1, 0};
        int[] rows = new int[]{3, 2, 1};

        SquareTest.testMultipleSquares(columns, rows, bishopMover.possibleMoves(bishop, board));
    }

    @Test
    public void bishopCannotMoveOnTopOfOwnPiece() {
        Piece pawn = new Piece(PAWN, 4, 5, Player.WHITE, "wp");
        putPieceOnBoard(board, pawn);
        assertFalse(bishopMover.possibleMoves(bishop, board).contains(new Square(4, 5)));
    }

    @Test
    public void bishopCanTakeAnEnemyPiece() {
        Piece pawn = new Piece(PAWN, 4, 5, Player.BLACK, "bp");
        putPieceOnBoard(board, pawn);
        assertTrue(bishopMover.possibleMoves(bishop, board).contains(new Square(4, 5)));
    }

    @Test
    public void bishopCannotMovePastAPiece() {
        Piece pawn = new Piece(PAWN, 4, 5, Player.WHITE, "wp");
        putPieceOnBoard(board, pawn);
        assertFalse(bishopMover.possibleMoves(bishop, board).contains(new Square(5, 6)));
    }

    @Test
    public void bishopCannotMovePastOpposingPiece() {
        Piece pawn = new Piece(PAWN, 4, 5, Player.BLACK, "bp");
        putPieceOnBoard(board, pawn);
        assertFalse(bishopMover.possibleMoves(bishop, board).contains(new Square(5, 6)));
    }
}
