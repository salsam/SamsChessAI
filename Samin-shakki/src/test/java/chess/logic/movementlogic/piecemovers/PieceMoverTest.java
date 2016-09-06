package chess.logic.movementlogic.piecemovers;

import chess.domain.GameSituation;
import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.PAWN;
import static chess.domain.board.Klass.QUEEN;
import chess.logic.chessboardinitializers.ChessBoardInitializer;
import static chess.logic.chessboardinitializers.ChessBoardInitializer.putPieceOnBoard;
import chess.logic.movementlogic.MovementLogic;
import chess.logic.chessboardinitializers.EmptyBoardInitializer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author sami
 */
public class PieceMoverTest {

    private Piece piece;
    private Piece pawn;
    private ChessBoard board;
    private static ChessBoardInitializer init;
    private static GameSituation sit;

    public PieceMoverTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        init = new EmptyBoardInitializer();
        sit = new GameSituation(init, new MovementLogic());
    }

    @Before
    public void setUp() {
        sit.reset();
        board = sit.getChessBoard();
        piece = new Piece(QUEEN, 3, 4, Player.WHITE, "wq");
        pawn = new Piece(PAWN, 3, 6, Player.BLACK, "bp");
        putPieceOnBoard(board, pawn);
        putPieceOnBoard(board, piece);
        sit.reHashBoard(true);
    }

    @Test
    public void getOwnerReturnCorrectPlayer() {
        assertEquals(Player.WHITE, piece.getOwner());
        assertEquals(Player.BLACK, pawn.getOwner());
    }

    @Test
    public void locationCorrectAfterCreation() {
        assertEquals(board.getSquare(3, 4), board.getSquare(piece.getColumn(), piece.getRow()));
    }

    @Test
    public void movingChangesLocationCorrectly() {
        board.getMovementLogic().move(piece, board.getSquare(3, 5), sit);
        assertEquals(board.getSquare(3, 5), board.getSquare(piece.getColumn(), piece.getRow()));
    }

    @Test
    public void movingRemovesPieceFromPreviousSquare() {
        board.getMovementLogic().move(piece, board.getSquare(3, 5), sit);
        assertEquals(null, board.getSquare(3, 4).getPiece());
    }

    @Test
    public void movingAddsPieceToTargetSquare() {
        board.getMovementLogic().move(piece, board.getSquare(3, 5), sit);
        assertEquals(piece, board.getSquare(3, 5).getPiece());
    }

    @Test
    public void movingToEmptySquareUpdatesHashCorrectly() {
        board.getMovementLogic().move(piece, board.getSquare(3, 5), sit);
        ChessBoard comp = new ChessBoard(new MovementLogic());
        putPieceOnBoard(comp, piece);
        putPieceOnBoard(comp, pawn);
        assertEquals(sit.getHasher().hash(comp), sit.getBoardHash());
    }

    @Test
    public void takingAPieceUpdatesHashCorrectly() {
        board.getMovementLogic().move(piece, board.getSquare(3, 6), sit);
        ChessBoard comp = new ChessBoard(new MovementLogic());
        putPieceOnBoard(comp, piece);
        assertEquals(sit.getHasher().hash(comp), sit.getBoardHash());
    }
}
