package chess.logic.movementlogic.piecemovers;

import chess.domain.GameSituation;
import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import chess.domain.board.Square;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.PAWN;
import chess.logic.chessboardinitializers.ChessBoardInitializer;
import static chess.logic.chessboardinitializers.ChessBoardInitializer.putPieceOnBoard;
import chess.logic.chessboardinitializers.EmptyBoardInitializer;
import chess.logic.movementlogic.MovementLogic;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author sami
 */
public class PawnMoverTest {

    private Piece pawn;
    private static ChessBoard board;
    private static ChessBoardInitializer init;
    private static GameSituation sit;
    private static PawnMover pawnMover;

    public PawnMoverTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        init = new EmptyBoardInitializer();
        sit = new GameSituation(init, new MovementLogic());
        board = sit.getChessBoard();
        pawnMover = new PawnMover();
    }

    @Before
    public void setUp() {
        init.initialize(board);
        pawn = new Piece(PAWN, 2, 6, Player.WHITE, "wp");
        putPieceOnBoard(board, pawn);
    }

    @Test
    public void startingcolumnCorrect() {
        assertEquals(2, pawn.getColumn());
    }

    @Test
    public void startingrowCorrect() {
        assertEquals(6, pawn.getRow());
    }

    @Test
    public void pawnCannotStayStillWhenMoving() {
        assertFalse(pawnMover.possibleMoves(pawn, board).contains(new Square(2, 6)));
    }

    @Test
    public void pawnCannotMoveThreeStepsUp() {
        assertFalse(pawnMover.possibleMoves(pawn, board).contains(new Square(2, 3)));
    }

    @Test
    public void pawnCannotMoveSideways() {
        assertFalse(pawnMover.possibleMoves(pawn, board).contains(new Square(1, 6)));
    }

    @Test
    public void pawnCanMoveTwoStepsUpFromStartingLocation() {
        assertTrue(pawnMover.possibleMoves(pawn, board).contains(new Square(2, 4)));
    }

    @Test
    public void pawnCannotMoveTwoStepsAfterMovingOnce() {
        pawn = new Piece(PAWN, 2, 6, Player.WHITE, "wp");
        pawnMover.move(pawn, board.getSquare(2, 5), sit);
        assertFalse(pawnMover.possibleMoves(pawn, board).contains(new Square(2, 3)));
    }

    @Test
    public void pawnCanTakeAPieceDiagonallyForwardToRight() {
        Piece enemyPawn = new Piece(PAWN, 3, 5, Player.BLACK, "ep");
        putPieceOnBoard(board, enemyPawn);
        assertTrue(pawnMover.possibleMoves(pawn, board).contains(new Square(3, 5)));
    }

    @Test
    public void pawnCanTakeAPieceDiagonallyForwardToLeft() {
        Piece enemyPawn = new Piece(PAWN, 1, 5, Player.BLACK, "ep");
        putPieceOnBoard(board, enemyPawn);
        assertTrue(pawnMover.possibleMoves(pawn, board).contains(new Square(1, 5)));
    }

    @Test
    public void pawnCannotTakeOwnPieceDiagonallyForward() {
        Piece enemyPawn = new Piece(PAWN, 3, 5, Player.WHITE, "ep");
        putPieceOnBoard(board, enemyPawn);
        assertFalse(pawnMover.possibleMoves(pawn, board).contains(new Square(3, 5)));
    }

    @Test
    public void pawnCannotMoveOverTheEdge() {
        pawn = new Piece(PAWN, 0, 0, Player.WHITE, "wp");
        putPieceOnBoard(board, pawn);
        assertFalse(pawnMover.possibleMoves(pawn, board).contains(new Square(0, -1)));
    }

    @Test
    public void pawnCanEnPassantOpposingPawnThatMovedTwoSquaresLastTurn() {
        Piece opposingPawn = new Piece(PAWN, 3, 4, Player.BLACK, "op");
        putPieceOnBoard(board, opposingPawn);
        pawnMover.move(opposingPawn, board.getSquare(3, 6), sit);
        assertTrue(pawnMover.possibleMoves(pawn, board).contains(new Square(3, 5)));
    }

    @Test
    public void whenEnPassantingOpposingPawnIsRemovedFromBoard() {
        Piece opposingPawn = new Piece(PAWN, 3, 4, Player.BLACK, "op");
        putPieceOnBoard(board, opposingPawn);
        pawnMover.move(opposingPawn, board.getSquare(3, 6), sit);
        pawnMover.move(pawn, board.getSquare(3, 5), sit);
        assertFalse(board.getSquare(3, 6).containsAPiece());
    }

    @Test
    public void whenEnPassantingHashIsUpdatedCorrectly() {
        Piece opposingPawn = new Piece(PAWN, 3, 4, Player.BLACK, "op");
        putPieceOnBoard(board, opposingPawn);
        sit.reHashBoard(true);
        pawnMover.move(opposingPawn, board.getSquare(3, 6), sit);
        pawnMover.move(pawn, board.getSquare(3, 5), sit);
        assertEquals(sit.getHasher().hash(board), sit.getBoardHash());
    }
}
