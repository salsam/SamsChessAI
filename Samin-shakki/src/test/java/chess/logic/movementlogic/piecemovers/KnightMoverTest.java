package chess.logic.movementlogic.piecemovers;

import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import chess.domain.board.Square;
import chess.domain.board.SquareTest;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.KNIGHT;
import chess.logic.chessboardinitializers.ChessBoardInitializer;
import static chess.logic.chessboardinitializers.ChessBoardInitializer.putPieceOnBoard;
import chess.logic.chessboardinitializers.EmptyBoardInitializer;
import chess.logic.movementlogic.MovementLogic;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 *
 * @author sami
 */
public class KnightMoverTest {

    private Piece knight;
    private static KnightMover knightMover;
    private static ChessBoard board;
    private static ChessBoardInitializer init;

    public KnightMoverTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        knightMover = new KnightMover();
        board = new ChessBoard(new MovementLogic());
        init = new EmptyBoardInitializer();
    }

    @Before
    public void setUp() {
        init.initialize(board);
        knight = new Piece(KNIGHT, 4, 4, Player.WHITE, "wn");
        putPieceOnBoard(board, knight);
    }

    @Test
    public void knightCannotStayStillWhenMoving() {
        assertFalse(knightMover.possibleMoves(knight, board).contains(new Square(4, 4)));
    }

    @Test
    public void knightCanMoveToEveryPossibleSquare() {
        int[] columns = new int[]{2, 2, 3, 3, 5, 5, 6, 6};
        int[] rows = new int[]{3, 5, 2, 6, 2, 6, 3, 5};

        SquareTest.testMultipleSquares(columns, rows, knightMover.possibleMoves(knight, board));
    }

    @Test
    public void knightCannotMoveOverTheEdge() {
        knight = new Piece(KNIGHT, 0, 0, Player.WHITE, "wn");
        putPieceOnBoard(board, knight);
        assertFalse(knightMover.possibleMoves(knight, board).contains(new Square(-1, -2)));
        assertFalse(knightMover.possibleMoves(knight, board).contains(new Square(1, -2)));
        assertFalse(knightMover.possibleMoves(knight, board).contains(new Square(-1, 2)));
    }

}
