package chess.domain.board;

import chess.domain.GameSituation;
import static chess.domain.board.Klass.KING;
import static chess.domain.board.Klass.PAWN;
import static chess.domain.board.Klass.QUEEN;
import static chess.domain.board.Klass.ROOK;
import chess.logic.movementlogic.MovementLogic;
import chess.logic.chessboardinitializers.BetterChessBoardInitializer;
import chess.logic.chessboardinitializers.ChessBoardInitializer;
import static chess.logic.chessboardinitializers.ChessBoardInitializer.putPieceOnBoard;
import chess.logic.chessboardinitializers.EmptyBoardInitializer;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sami
 */
public class ChessBoardCopierTest {

    private static GameSituation sit;
    private static ChessBoardInitializer init;

    public ChessBoardCopierTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        init = new BetterChessBoardInitializer();
        sit = new GameSituation(init, new MovementLogic());
    }

    @Before
    public void setUp() {
        sit.reset();
    }

    @Test
    public void copyCreatesChessBoardWithIdenticalTable() {
        init.initialize(sit.getChessBoard());
        ChessBoard copy = ChessBoardCopier.copy(sit.getChessBoard());

        assertTrue(ChessBoardCopier.chessBoardsAreDeeplyEqual(sit.getChessBoard(), copy));
    }

    @Test
    public void copyCreatesChessBoardWithPieceListsThatContainAllPieces() {
        ChessBoard copy = ChessBoardCopier.copy(sit.getChessBoard());

        for (Player player : Player.values()) {
            sit.getChessBoard().getPieces(player).stream().forEach(piece -> {
                assertTrue(copy.getPieces(player).contains(piece));
            });
        }
    }

    @Test
    public void copyCreatesChessBoardWithPieceListsThatContainNoExtraPieces() {
        ChessBoard copy = ChessBoardCopier.copy(sit.getChessBoard());

        for (Player player : Player.values()) {
            copy.getPieces(player).stream().forEach(piece -> {
                assertTrue(sit.getChessBoard().getPieces(player).contains(piece));
            });
        }
    }

    @Test
    public void copyHasBothKingsInCorrectSpot() {
        ChessBoard copy = ChessBoardCopier.copy(sit.getChessBoard());

        for (Player player : Player.values()) {
            assertTrue(copy.getKings().get(player).equals(sit.getChessBoard().getKings().get(player)));
        }
    }

    @Test
    public void copyAndOriginalAreNotSame() {
        init.initialize(sit.getChessBoard());
        assertFalse(ChessBoardCopier.copy(sit.getChessBoard()) == sit.getChessBoard());
    }

    @Test
    public void copyCreatesANewChessBoard() {
        init.initialize(sit.getChessBoard());
        ChessBoard copy = ChessBoardCopier.copy(sit.getChessBoard());

        Piece queen = new Piece(QUEEN, 4, 4, Player.BLACK, "bp1");
        putPieceOnBoard(sit.getChessBoard(), queen);

        assertTrue(sit.getChessBoard().getSquare(4, 4).containsAPiece());
        assertFalse(copy.getSquare(4, 4).containsAPiece());

        sit.getChessBoard().getMovementLogic().move(queen, sit.getChessBoard().getSquare(4, 6), sit);
        assertEquals(Player.BLACK, sit.getChessBoard().getSquare(4, 6).getPiece().getOwner());
        assertEquals(Player.WHITE, copy.getSquare(4, 6).getPiece().getOwner());
    }

    @Test
    public void undoMoveReturnsChessBoardToSituationBeforeMoveWasMade() {
        ChessBoard backup = ChessBoardCopier.copy(sit.getChessBoard());
        ChessBoard cb = sit.getChessBoard();
        MovementLogic ml = cb.getMovementLogic();
        Square from = cb.getSquare(1, 0);
        Square to = cb.getSquare(2, 2);
        ml.move(from.getPiece(), to, sit);
        assertFalse(from.containsAPiece());
        assertTrue(to.containsAPiece());
        assertTrue(backup.getSquare(from.getColumn(), from.getRow()).containsAPiece());
        ChessBoardCopier.undoMove(backup, sit, from, to);
        assertTrue(ChessBoardCopier.chessBoardsAreDeeplyEqual(backup, cb));
    }

    @Test
    public void undoMoveReturnsBoardHashToSituationBeforeMoveWasMade() {
        ChessBoard backup = ChessBoardCopier.copy(sit.getChessBoard());
        ChessBoard cb = sit.getChessBoard();
        long oldHash = sit.getBoardHash();
        MovementLogic ml = cb.getMovementLogic();
        Square from = cb.getSquare(1, 0);
        Square to = cb.getSquare(2, 2);
        ml.move(from.getPiece(), to, sit);
        assertNotEquals(oldHash, sit.getBoardHash());
        ChessBoardCopier.undoMove(backup, sit, from, to);
        assertEquals(oldHash, sit.getBoardHash());
    }

    @Test
    public void undoMoveReturnsSituationBeforeCastling() {
        EmptyBoardInitializer empty = new EmptyBoardInitializer();
        ChessBoard cb = sit.getChessBoard();
        empty.initialize(cb);

        Piece wk = new Piece(KING, 3, 0, Player.WHITE, "wk");
        Piece wr = new Piece(ROOK, 0, 0, Player.WHITE, "wr");
        putPieceOnBoard(cb, wk);
        putPieceOnBoard(cb, wr);
        sit.reHashBoard(true);

        ChessBoard backup = ChessBoardCopier.copy(cb);
        MovementLogic ml = cb.getMovementLogic();
        Square from = cb.getSquare(3, 0);
        Square to = cb.getSquare(1, 0);
        ml.move(from.getPiece(), to, sit);
        ChessBoardCopier.undoMove(backup, sit, from, to);
        assertTrue(ChessBoardCopier.chessBoardsAreDeeplyEqual(backup, cb));
    }

    @Test
    public void undoMoveReturnsHashToSituationBeforeCastling() {
        EmptyBoardInitializer empty = new EmptyBoardInitializer();
        ChessBoard cb = sit.getChessBoard();
        empty.initialize(cb);

        Piece wk = new Piece(KING, 3, 0, Player.WHITE, "wk");
        Piece wr = new Piece(ROOK, 0, 0, Player.WHITE, "wr");
        putPieceOnBoard(cb, wk);
        putPieceOnBoard(cb, wr);
        sit.reHashBoard(true);

        ChessBoard backup = ChessBoardCopier.copy(cb);
        long oldHash = sit.getBoardHash();
        MovementLogic ml = cb.getMovementLogic();
        Square from = cb.getSquare(3, 0);
        Square to = cb.getSquare(1, 0);
        ml.move(from.getPiece(), to, sit);
        assertNotEquals(oldHash, sit.getBoardHash());
        ChessBoardCopier.undoMove(backup, sit, from, to);
        assertEquals(sit.getHasher().hash(cb), sit.getBoardHash());
        assertEquals(oldHash, sit.getBoardHash());
    }

    @Test
    public void undoMoveReturnsBoardToSituationBeforeEnpassant() {
        ChessBoard cb = sit.getChessBoard();
        MovementLogic ml = cb.getMovementLogic();
        Piece pawn = new Piece(PAWN, 2, 5, Player.WHITE, "wp");
        Piece opposingPawn = new Piece(PAWN, 3, 3, Player.BLACK, "op");

        putPieceOnBoard(cb, pawn);
        putPieceOnBoard(cb, opposingPawn);
        sit.reHashBoard(true);

        ml.move(opposingPawn, cb.getSquare(3, 5), sit);
        ChessBoard before = ChessBoardCopier.copy(cb);
        ml.move(pawn, cb.getSquare(3, 4), sit);
        ChessBoardCopier.undoMove(before, sit, cb.getSquare(2, 5), cb.getSquare(3, 4));
        assertTrue(ChessBoardCopier.chessBoardsAreDeeplyEqual(before, cb));
    }

    @Test
    public void undoMoveReturnsHashToSituationBeforeEnpassant() {
        ChessBoard cb = sit.getChessBoard();
        MovementLogic ml = cb.getMovementLogic();
        Piece pawn = new Piece(PAWN, 2, 5, Player.WHITE, "wp");
        Piece opposingPawn = new Piece(PAWN, 3, 3, Player.BLACK, "op");

        putPieceOnBoard(cb, pawn);
        putPieceOnBoard(cb, opposingPawn);
        sit.reHashBoard(true);

        ml.move(opposingPawn, cb.getSquare(3, 5), sit);
        ChessBoard backup = ChessBoardCopier.copy(cb);
        long oldHash = sit.getBoardHash();
        ml.move(pawn, cb.getSquare(3, 4), sit);
        ChessBoardCopier.undoMove(backup, sit, cb.getSquare(2, 5), cb.getSquare(3, 4));

        assertEquals(oldHash, sit.getBoardHash());
        assertEquals(sit.getHasher().hash(cb), sit.getBoardHash());
    }

    @Test
    public void undoMoveRevertsPromotion() {
        EmptyBoardInitializer emptier = new EmptyBoardInitializer();
        ChessBoard cb = sit.getChessBoard();
        emptier.initialize(cb);
        MovementLogic ml = cb.getMovementLogic();
        Piece pawn = new Piece(PAWN, 2, 1, Player.WHITE, "wp");
        Piece opposingPawn = new Piece(PAWN, 3, 5, Player.BLACK, "op");

        putPieceOnBoard(cb, pawn);
        putPieceOnBoard(cb, opposingPawn);
        sit.reHashBoard(true);

        ChessBoard backup = ChessBoardCopier.copy(cb);
        ml.move(pawn, cb.getSquare(2, 0), sit);
        ChessBoardCopier.undoMove(backup, sit, cb.getSquare(2, 1), cb.getSquare(2, 0));
        assertTrue(cb.getSquare(2, 1).containsAPiece());
        assertEquals(PAWN, cb.getSquare(2, 1).getPiece().getKlass());
        for (Piece p : cb.getPieces(Player.WHITE)) {
            assertNotEquals(QUEEN, p.getKlass());
        }
    }
}
