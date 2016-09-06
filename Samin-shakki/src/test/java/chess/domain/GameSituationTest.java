package chess.domain;

import chess.logic.movementlogic.MovementLogic;
import chess.domain.board.ChessBoard;
import static chess.domain.board.ChessBoardCopier.chessBoardsAreDeeplyEqual;
import static chess.domain.board.ChessBoardCopier.copy;
import chess.domain.board.Player;
import chess.domain.board.Square;
import chess.domain.board.Piece;
import chess.logic.chessboardinitializers.EmptyBoardInitializer;
import static chess.domain.board.Klass.KING;
import static chess.domain.board.Klass.PAWN;
import static chess.logic.chessboardinitializers.ChessBoardInitializer.putPieceOnBoard;
import chess.logic.chessboardinitializers.BetterChessBoardInitializer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author sami
 */
public class GameSituationTest {

    private GameSituation game;
    private static BetterChessBoardInitializer init;

    public GameSituationTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        init = new BetterChessBoardInitializer();
    }

    @Before
    public void setUp() {
        game = new GameSituation(new EmptyBoardInitializer(), new MovementLogic());
        ChessBoard board = game.getChessBoard();
        putPieceOnBoard(board, new Piece(KING, 0, 0, Player.WHITE, "wk"));
    }

    @Test
    public void whoseTurnReturnsWhiteIfOddTurnAndBlackIfEvenTurn() {
        assertEquals(Player.WHITE, game.whoseTurn());
        game.nextTurn();
        assertEquals(Player.BLACK, game.whoseTurn());
        game.nextTurn();
        assertEquals(Player.WHITE, game.whoseTurn());
    }

    @Test
    public void playersPawnsBecomeNoLongerCapturableEnPassantOnPlayersNextTurn() {
        Piece whitePawn = new Piece(PAWN, 4, 4, Player.WHITE, "wp");
        Piece blackPawn = new Piece(PAWN, 4, 6, Player.BLACK, "bp");
        ChessBoard board = game.getChessBoard();
        putPieceOnBoard(board, whitePawn);
        putPieceOnBoard(board, blackPawn);
        board.getMovementLogic().move(whitePawn, board.getSquare(4, 6), game);
        assertTrue(whitePawn.isMovedTwoSquaresLastTurn());
        game.nextTurn();
        game.nextTurn();
        assertFalse(whitePawn.isMovedTwoSquaresLastTurn());
    }

    @Test
    public void turnCounterStartsFrom1() {
        assertEquals(1, game.getTurn());
    }

    @Test
    public void firstWhitesTurn() {
        assertEquals(Player.WHITE, game.whoseTurn());
    }

    @Test
    public void nextTurnIncreasesTurnByOne() {
        game.nextTurn();
        assertEquals(2, game.getTurn());
        game.nextTurn();
        assertEquals(3, game.getTurn());
    }

    @Test
    public void whiteAndBlackMovePiecesInTurns() {
        assertEquals(Player.WHITE, game.whoseTurn());
        game.nextTurn();
        assertEquals(Player.BLACK, game.whoseTurn());
        game.nextTurn();
        assertEquals(Player.WHITE, game.whoseTurn());
        game.nextTurn();
        assertEquals(Player.BLACK, game.whoseTurn());
        game.nextTurn();
        assertEquals(Player.WHITE, game.whoseTurn());
    }

    @Test
    public void resetResetsTurnCounter() {
        game.nextTurn();
        game.reset();
        assertEquals(1, game.getTurn());
        assertEquals(Player.WHITE, game.whoseTurn());
    }

    @Test
    public void resetResetsSituationOnBoard() {
        game.reset();
        assertFalse(game.getChessBoard().getSquare(0, 0).containsAPiece());
    }

    @Test
    public void resetMakesGameContinueAgain() {
        game.setContinues(false);
        game.reset();
        assertTrue(game.getContinues());
    }

    @Test
    public void startingSituationHasBeenMetOnce() {
        assertEquals(1, game.getCountOfCurrentSituation());
    }

    @Test
    public void decreaseCountOfCurrentSituationDecreasesCountByOne() {
        game.decrementCountOfCurrentBoardSituation();
        assertEquals(0, game.getCountOfCurrentSituation());
    }

    @Test
    public void incrementCountOfCurrentSituationIncreasesCountByOne() {
        game.incrementCountOfCurrentBoardSituation();
        assertEquals(2, game.getCountOfCurrentSituation());
    }

    @Test
    public void boardHashUptoDateAfterInitialization() {
        game = new GameSituation(init, new MovementLogic());
        assertEquals(game.getHasher().hash(game.getChessBoard()), game.getBoardHash());
    }

    @Test
    public void boardHashRemainsSameWhenRehashingIfNoChanges() {
        game = new GameSituation(init, new MovementLogic());
        long oldHash = game.getBoardHash();
        game.reHashBoard(true);
        assertEquals(oldHash, game.getBoardHash());
    }

    @Test
    public void reHashBoardChangesHashIfBoardSituationChanged() {
        long oldHash = game.getBoardHash();
        Piece whitePawn = new Piece(PAWN, 4, 4, Player.WHITE, "wp");
        Piece blackPawn = new Piece(PAWN, 4, 6, Player.BLACK, "bp");
        ChessBoard board = game.getChessBoard();
        putPieceOnBoard(board, whitePawn);
        putPieceOnBoard(board, blackPawn);
        game.reHashBoard(true);
        assertNotEquals(oldHash, game.getBoardHash());
    }

    @Test
    public void boardHashCorrectAfterReHashing() {
        game.reset();
        Piece whitePawn = new Piece(PAWN, 4, 4, Player.WHITE, "wp");
        Piece blackPawn = new Piece(PAWN, 4, 6, Player.BLACK, "bp");
        ChessBoard board = game.getChessBoard();
        ChessBoard comp = new ChessBoard(new MovementLogic());
        putPieceOnBoard(board, whitePawn);
        putPieceOnBoard(board, blackPawn);
        putPieceOnBoard(comp, whitePawn);
        putPieceOnBoard(comp, blackPawn);
        game.reHashBoard(true);
        assertEquals(game.getHasher().hash(comp), game.getBoardHash());
    }

    @Test
    public void hashIsUpdatedCorrectlyForMove() {
        game.reHashBoard(true);
        Square from = game.getChessBoard().getSquare(0, 0);
        Square to = game.getChessBoard().getSquare(1, 0);
        game.updateHashForMoving(from, to);
        long updatedHash = game.getBoardHash();
        MovementLogic ml = game.getChessBoard().getMovementLogic();
        Piece wk = from.getPiece();
        ml.move(wk, to, game);
        game.reHashBoard(true);
        assertEquals(game.getBoardHash(), updatedHash);
    }

    @Test
    public void gameSituationRevertsToBeginningWhenGameIsReset() {
        game = new GameSituation(init, new MovementLogic());
        ChessBoard bu = copy(game.getChessBoard());
        ChessBoard cb = game.getChessBoard();
        MovementLogic ml = cb.getMovementLogic();

        long oldHash = game.getBoardHash();
        ml.move(cb.getSquare(1, 6).getPiece(), cb.getSquare(1, 4), game);
        game.reset();

        assertTrue(chessBoardsAreDeeplyEqual(game.getChessBoard(), bu));
        assertEquals(oldHash, game.getBoardHash());

        game = new GameSituation(new EmptyBoardInitializer(), new MovementLogic());
    }
}
