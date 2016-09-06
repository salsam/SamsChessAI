package chess.logic.movementlogic.piecemovers;

import chess.domain.GameSituation;
import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import chess.domain.board.Square;
import chess.domain.board.SquareTest;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.BISHOP;
import static chess.domain.board.Klass.KING;
import static chess.domain.board.Klass.QUEEN;
import static chess.domain.board.Klass.ROOK;
import chess.logic.chessboardinitializers.ChessBoardInitializer;
import static chess.logic.chessboardinitializers.ChessBoardInitializer.putPieceOnBoard;
import chess.logic.chessboardinitializers.EmptyBoardInitializer;
import chess.logic.movementlogic.MovementLogic;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author sami
 */
public class KingMoverTest {

    private Piece king;
    private static GameSituation sit;
    private static ChessBoard board;
    private static ChessBoardInitializer init;
    private static KingMover kingMover;

    public KingMoverTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        init = new EmptyBoardInitializer();
        sit = new GameSituation(init, new MovementLogic());
        board = sit.getChessBoard();
        kingMover = new KingMover();
    }

    @Before
    public void setUp() {
        init.initialize(board);
        king = new Piece(KING, 2, 3, Player.WHITE, "wk");
        putPieceOnBoard(board, king);
    }

    @Test
    public void startingColumnCorrect() {
        assertEquals(2, king.getColumn());
    }

    @Test
    public void startingRowCorrect() {
        assertEquals(3, king.getRow());
    }

    @Test
    public void kingCannotStayStillWhenMoving() {
        assertFalse(kingMover.possibleMoves(king, board).contains(new Square(2, 3)));
    }

    @Test
    public void kingThreatensEveryNeighboringSquare() {
        int[] cols = new int[]{3, 2, 1, 3, 1, 3, 2, 1};
        int[] rows = new int[]{4, 4, 4, 3, 3, 2, 2, 2};

        SquareTest.testMultipleSquares(cols, rows, kingMover.threatenedSquares(king, board));
    }

    @Test
    public void kingCanMoveToEveryNeighboringSquare() {
        int[] cols = new int[]{3, 2, 1, 3, 1, 3, 2, 1};
        int[] rows = new int[]{4, 4, 4, 3, 3, 2, 2, 2};
        board.updateThreatenedSquares(Player.BLACK);

        SquareTest.testMultipleSquares(cols, rows, kingMover.possibleMoves(king, board));
    }

    @Test
    public void kingCannotMoveOutOfBoard() {
        init.initialize(board);
        king = new Piece(KING, 0, 0, Player.WHITE, "wk");
        putPieceOnBoard(board, king);

        assertFalse(kingMover.possibleMoves(king, board).contains(new Square(-1, 0)));
        assertFalse(kingMover.possibleMoves(king, board).contains(new Square(0, -1)));
    }

    @Test
    public void kingCannotMoveToThreatenedSquare() {
        init.initialize(board);
        putPieceOnBoard(board, king);
        Piece opposingQueen = new Piece(QUEEN, 3, 5, Player.BLACK, "bq");
        putPieceOnBoard(board, opposingQueen);
        board.updateThreatenedSquares(Player.BLACK);

        board.getMovementLogic().threatenedSquares(opposingQueen, board).stream().forEach(i -> {
            assertFalse(kingMover.possibleMoves(king, board).contains(i));
        });
    }

    @Test
    public void kingCanTakeOpposingUnprotectedPieceThatChecksIt() {
        putPieceOnBoard(board, new Piece(QUEEN, 2, 4, Player.BLACK, "bq"));
        board.updateThreatenedSquares(Player.BLACK);
        assertTrue(kingMover.possibleMoves(king, board).contains(board.getSquare(2, 4)));
    }

    @Test
    public void kingCannotTakeProtectedPieces() {
        putPieceOnBoard(board, new Piece(QUEEN, 2, 4, Player.BLACK, "bq"));
        putPieceOnBoard(board, new Piece(BISHOP, 3, 5, Player.BLACK, "bb"));
        board.updateThreatenedSquares(Player.BLACK);

        assertFalse(kingMover.possibleMoves(king, board).contains(board.getSquare(2, 4)));
    }

    @Test
    public void kingCanCastleKingSideIfAllRequirementsAreMet() {
        Piece blackKing = new Piece(KING, 4, 7, Player.BLACK, "bk");
        putPieceOnBoard(board, blackKing);
        putPieceOnBoard(board, new Piece(ROOK, 7, 7, Player.BLACK, "br"));
        assertTrue(kingMover.possibleMoves(blackKing, board).contains(board.getSquare(6, 7)));
    }

    @Test
    public void kingCanCastleQueenSideIfAllRequirementsAreMet() {
        Piece blackKing = new Piece(KING, 4, 7, Player.BLACK, "bk");
        putPieceOnBoard(board, blackKing);
        putPieceOnBoard(board, new Piece(ROOK, 0, 7, Player.BLACK, "br"));
        assertTrue(kingMover.possibleMoves(blackKing, board).contains(board.getSquare(2, 7)));
    }

    @Test
    public void whenCastlingKingSideChosenRookIsAlsoMovedToCorrectSquare() {
        Piece blackKing = new Piece(KING, 4, 7, Player.BLACK, "bk");
        Piece blackRook = new Piece(ROOK, 7, 7, Player.BLACK, "br");
        putPieceOnBoard(board, blackKing);
        putPieceOnBoard(board, blackRook);
        kingMover.move(blackKing, board.getSquare(6, 7), sit);
        assertEquals(board.getSquare(5, 7), board.getSquare(blackRook.getColumn(), blackRook.getRow()));
    }

    @Test
    public void whenCastlingQueenSideChosenRookIsAlsoMovedToCorrectSquare() {
        Piece blackKing = new Piece(KING, 4, 7, Player.BLACK, "bk");
        Piece blackRook = new Piece(ROOK, 0, 7, Player.BLACK, "br");
        putPieceOnBoard(board, blackKing);
        putPieceOnBoard(board, blackRook);
        kingMover.move(blackKing, board.getSquare(2, 7), sit);
        assertEquals(board.getSquare(3, 7), board.getSquare(blackRook.getColumn(), blackRook.getRow()));
    }

    @Test
    public void hashUpdatedCorrectlyWhenCastlingKingside() {
        init.initialize(board);
        Piece wk = new Piece(KING, 3, 0, Player.WHITE, "wk");
        Piece wr = new Piece(ROOK, 0, 0, Player.WHITE, "wr");
        putPieceOnBoard(board, wk);
        putPieceOnBoard(board, wr);

        sit.reHashBoard(true);
        kingMover.move(wk, board.getSquare(1, 0), sit);
        assertEquals(sit.getHasher().hash(board), sit.getBoardHash());
    }
}
