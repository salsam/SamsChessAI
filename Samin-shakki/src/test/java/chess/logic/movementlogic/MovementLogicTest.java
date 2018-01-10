package chess.logic.movementlogic;

import chess.domain.GameSituation;
import chess.domain.Move;
import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import chess.domain.board.Square;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.BISHOP;
import static chess.domain.board.Klass.KING;
import static chess.domain.board.Klass.KNIGHT;
import static chess.domain.board.Klass.PAWN;
import static chess.domain.board.Klass.QUEEN;
import static chess.domain.board.Klass.ROOK;
import chess.logic.chessboardinitializers.ChessBoardInitializer;
import chess.logic.chessboardinitializers.EmptyBoardInitializer;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author sami
 */
public class MovementLogicTest {

    private static MovementLogic ml;
    private static ChessBoardInitializer emptyinit;
    private ChessBoard board;
    private GameSituation sit;

    public MovementLogicTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        ml = new MovementLogic();
        emptyinit = new EmptyBoardInitializer();
    }

    @Before
    public void setUp() {
        sit = new GameSituation(emptyinit, ml);
        board = sit.getChessBoard();
    }

    @Test
    public void possibleMovesByPlayerContainsAllMovesPlayersPieceCanMake() {
        emptyinit.initialize(board);
        ChessBoardInitializer.putPieceOnBoard(board, new Piece(QUEEN, 4, 3, Player.BLACK, "bq"));
        ChessBoardInitializer.putPieceOnBoard(board, new Piece(ROOK, 2, 2, Player.BLACK, "br"));
        ChessBoardInitializer.putPieceOnBoard(board, new Piece(PAWN, 4, 1, Player.BLACK, "bp"));
        ChessBoardInitializer.putPieceOnBoard(board, new Piece(BISHOP, 7, 1, Player.BLACK, "bb"));
        ChessBoardInitializer.putPieceOnBoard(board, new Piece(KING, 4, 1, Player.BLACK, "bk"));
        ChessBoardInitializer.putPieceOnBoard(board, new Piece(KNIGHT, 7, 1, Player.BLACK, "bn"));
        Set<Square> possibleMoves = ml.possibleMovesByPlayer(Player.BLACK, board);
        assertTrue(board.getPieces(Player.BLACK).stream()
                .allMatch(bp -> ml.possibleMoves(bp, board).stream().allMatch(move -> possibleMoves.contains(move))));
    }

    @Test
    public void movingANonPawnPieceReducesMovesTillDraw() {
        Piece bq = new Piece(QUEEN, 4, 3, Player.BLACK, "bq");
        ChessBoardInitializer.putPieceOnBoard(board, bq);
        assertEquals(100, sit.getMovesTillDraw());
        ml.move(bq, new Square(4, 4), sit);
        assertEquals(99, sit.getMovesTillDraw());
    }

    @Test
    public void capturingAPieceResetsMovesTillDraw() {
        Piece bq = new Piece(QUEEN, 4, 3, Player.BLACK, "bq");
        ChessBoardInitializer.putPieceOnBoard(board, bq);
        ChessBoardInitializer.putPieceOnBoard(board, new Piece(PAWN, 4, 4, Player.WHITE, "wp1"));
        sit.setMovesTillDraw(50);
        assertEquals(50, sit.getMovesTillDraw());
        ml.move(bq, new Square(4, 4), sit);
        assertEquals(100, sit.getMovesTillDraw());
    }

    @Test
    public void commitMoveIfKingWillNotGetCheckedReturnsTrueIfMovementIsSuccessful() {
        Piece wq = new Piece(QUEEN, 4, 3, Player.WHITE, "wq");
        ChessBoardInitializer.putPieceOnBoard(board, wq);
        Move move = new Move(wq, 4, 4);
        assertTrue(ml.commitMoveIfKingWillNotGetChecked(move, sit));
    }

    @Test
    public void commitMoveIfKingWillNotGetCheckedReturnsFalseIfMovementFails() {
        Piece wq = new Piece(QUEEN, 4, 3, Player.WHITE, "wq");
        ChessBoardInitializer.putPieceOnBoard(board, wq);
        ChessBoardInitializer.putPieceOnBoard(board, new Piece(KING, 7, 3, Player.WHITE, "wk"));
        ChessBoardInitializer.putPieceOnBoard(board, new Piece(QUEEN, 1, 3, Player.BLACK, "bq"));
        Move move = new Move(wq, 4, 4);
        assertFalse(ml.commitMoveIfKingWillNotGetChecked(move, sit));
        assertFalse(sit.getCheckLogic().checkIfChecked(Player.WHITE));
    }
}
