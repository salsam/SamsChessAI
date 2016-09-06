package chess.logic.ailogic;

import chess.domain.GameSituation;
import chess.domain.Move;
import static chess.domain.board.ChessBoardCopier.copy;
import chess.domain.board.*;
import static chess.domain.board.ChessBoardCopier.chessBoardsAreDeeplyEqual;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.*;
import static chess.logic.ailogic.GameSituationEvaluator.evaluateGameSituation;
import static chess.logic.chessboardinitializers.ChessBoardInitializer.putPieceOnBoard;
import chess.logic.chessboardinitializers.*;
import chess.logic.movementlogic.MovementLogic;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author sami
 */
public class AILogicTest {

    private static GameSituation sit;
    private static AILogic ai;

    public AILogicTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        sit = new GameSituation(new EmptyBoardInitializer(), new MovementLogic());
        ai = new AILogic();
    }

    @Before
    public void setUp() {
        sit = new GameSituation(new EmptyBoardInitializer(), new MovementLogic());
        ai.setSituation(sit);
        ai.setSearchDepth(3);
    }

    @Test
    public void findBestMoveChoosesAMove() {
        Piece pawn = new Piece(PAWN, 1, 7, Player.WHITE, "wp1");
        putPieceOnBoard(sit.getChessBoard(), pawn);
        sit.reHashBoard(true);
        ai.findBestMoves(sit);
        assertFalse(ai.getBestMove() == null);
    }

    @Test
    public void bestMoveCorrectWithOnePiece() {
        Piece pawn = new Piece(PAWN, 0, 7, Player.WHITE, "wp1");
        Piece bpawn = new Piece(PAWN, 7, 0, Player.BLACK, "bp1");
        putPieceOnBoard(sit.getChessBoard(), bpawn);
        putPieceOnBoard(sit.getChessBoard(), pawn);
        sit.reHashBoard(true);
        ai.findBestMoves(sit);
        assertTrue(ai.getBestMove().getTarget().equals(sit.getChessBoard().getSquare(0, 6))
                || ai.getBestMove().getTarget().equals(sit.getChessBoard().getSquare(0, 5)));
        assertEquals(pawn, ai.getBestMove().getPiece());
    }

    @Test
    public void checkMateIsBetterThanTakingKnight() {
        Piece bk = new Piece(KING, 0, 7, Player.BLACK, "bk");
        Piece bn = new Piece(KNIGHT, 7, 3, Player.BLACK, "bn");
        Piece wq = new Piece(QUEEN, 1, 3, Player.WHITE, "wq");
        Piece wk = new Piece(KING, 2, 5, Player.WHITE, "wk");

        putPieceOnBoard(sit.getChessBoard(), wk);
        putPieceOnBoard(sit.getChessBoard(), bk);
        putPieceOnBoard(sit.getChessBoard(), bn);
        putPieceOnBoard(sit.getChessBoard(), wq);

        sit.reHashBoard(true);

        ai.findBestMoves(sit);
        assertEquals(new Move(wq, sit.getChessBoard().getSquare(1, 6)), ai.getBestMove());
    }

    @Test
    public void aiWillNotInitiateLosingTrades() {
        Piece bb = new Piece(BISHOP, 0, 1, Player.BLACK, "bb");
        Piece bn = new Piece(KNIGHT, 1, 0, Player.BLACK, "bn");
        Piece wq = new Piece(QUEEN, 1, 3, Player.WHITE, "wq");

        putPieceOnBoard(sit.getChessBoard(), bb);
        putPieceOnBoard(sit.getChessBoard(), bn);
        putPieceOnBoard(sit.getChessBoard(), wq);

        ai.findBestMoves(sit);
        assertNotEquals(new Move(wq, new Square(1, 0)), ai.getBestMove());
    }

    @Test
    public void findBestMoveDoesNotChangeChessBoard() {
        sit = new GameSituation(new BetterChessBoardInitializer(), new MovementLogic());
        ChessBoard backUp = copy(sit.getChessBoard());
        ai.findBestMoves(sit);

        for (Player player : Player.values()) {
            backUp.getPieces(player).stream().forEach((piece) -> {
                assertTrue(sit.getChessBoard().getPieces(player).contains(piece));
            });
        }
        sit = new GameSituation(new EmptyBoardInitializer(), new MovementLogic());

    }

    @Test
    public void findBestMoveDoesNotChangePawns() {
        sit = new GameSituation(new BetterChessBoardInitializer(), new MovementLogic());
        MovementLogic ml = sit.getChessBoard().getMovementLogic();
        ChessBoard cb = sit.getChessBoard();
        ml.move(cb.getSquare(1, 1).getPiece(), cb.getSquare(1, 3), sit);
        ChessBoard backUp = copy(sit.getChessBoard());
        ai.findBestMoves(sit);

        for (Player player : Player.values()) {
            for (Piece p : backUp.getPieces(player)) {
                for (Piece newP : sit.getChessBoard().getPieces(player)) {
                    if (p.equals(newP) && p.getKlass() == PAWN) {
                        assertEquals(p.isHasBeenMoved(), newP.isHasBeenMoved());
                    }
                }
            }
        }
        sit = new GameSituation(new EmptyBoardInitializer(), new MovementLogic());
    }

    @Test
    public void pawnRemainsPawnIfPromotionIsTested() {
        ChessBoard cb = sit.getChessBoard();
        Piece wp = new Piece(PAWN, 1, 4, Player.WHITE, "wp");
        putPieceOnBoard(cb, wp);
        MovementLogic ml = cb.getMovementLogic();
        ml.move(wp, cb.getSquare(1, 5), sit);
        ChessBoard backUp = copy(sit.getChessBoard());
        ai.findBestMoves(sit);

        for (Player player : Player.values()) {
            for (Piece p : backUp.getPieces(player)) {
                for (Piece newP : sit.getChessBoard().getPieces(player)) {
                    if (p.equals(newP)) {
                        assertEquals(PAWN, p.getKlass());
                        assertEquals(p.getKlass(), newP.getKlass());
                        assertEquals(p.getColumn(), newP.getColumn());
                        assertEquals(p.getRow(), newP.getRow());
                        assertEquals(p.getOwner(), newP.getOwner());
                    }
                }
            }
        }
    }

    @Test
    public void pawnRemainsPawnInComplexSituation() {
        ChessBoard cb = sit.getChessBoard();
        Piece wp = new Piece(PAWN, 1, 4, Player.WHITE, "wp");
        Piece wq = new Piece(QUEEN, 2, 1, Player.WHITE, "wq1");
        Piece bk = new Piece(KING, 0, 0, Player.BLACK, "bk");

        putPieceOnBoard(cb, bk);
        putPieceOnBoard(cb, wp);
        putPieceOnBoard(cb, wq);
        sit.reHashBoard(true);

        ChessBoard backUp = copy(sit.getChessBoard());
        ai.findBestMoves(sit);

        for (Player player : Player.values()) {
            for (Piece p : backUp.getPieces(player)) {
                for (Piece newP : sit.getChessBoard().getPieces(player)) {
                    if (p.equals(newP)) {
                        assertEquals(p.getKlass(), newP.getKlass());
                        assertEquals(p.getColumn(), newP.getColumn());
                        assertEquals(p.getRow(), newP.getRow());
                        assertEquals(p.getOwner(), newP.getOwner());
                    }
                }
            }
        }
    }

    @Test
    public void negamaxForHeightZeroReturnsValueOfSituation() {
        ChessBoard cb = sit.getChessBoard();
        Piece wr = new Piece(ROOK, 1, 4, Player.WHITE, "wr");
        Piece bp = new Piece(PAWN, 5, 6, Player.BLACK, "bp");
        putPieceOnBoard(cb, wr);
        putPieceOnBoard(cb, bp);
        sit.reHashBoard(true);
        ai.setSituation(sit);
        ai.setStart(System.currentTimeMillis());
        assertEquals(evaluateGameSituation(sit, Player.WHITE), ai.negaMax(0, -123456798, 123456789, Player.WHITE));
    }

    @Test
    public void negamaxReturnsMinus123456789IfTimeLimitIsReached() {
        ChessBoard cb = sit.getChessBoard();
        Piece wr = new Piece(ROOK, 1, 4, Player.WHITE, "wr");
        Piece bp = new Piece(PAWN, 5, 6, Player.BLACK, "bp");
        putPieceOnBoard(cb, wr);
        putPieceOnBoard(cb, bp);
        sit.reHashBoard(true);
        ai.setStart(System.currentTimeMillis() - 1000);
        assertEquals(-123456789, ai.negaMax(0, -12345679, 12345678, Player.WHITE));

    }

    @Test
    public void checkForChangeIncreasesAlphaIfBetterValueFound() {
        ChessBoard cb = sit.getChessBoard();
        Piece wp = new Piece(PAWN, 1, 4, Player.WHITE, "wp");
        Piece bp = new Piece(PAWN, 5, 6, Player.BLACK, "bp");
        putPieceOnBoard(cb, wp);
        putPieceOnBoard(cb, bp);
        sit.reHashBoard(true);

        ai.getBestValues()[1] = -100;
        ai.setSituation(sit);
        ai.setStart(System.currentTimeMillis());
        int newAlpha = ai.checkForChange(wp, cb.getSquare(1, 4), 1, Player.WHITE, -123456789, -123456789, 123456789);
        assertEquals(evaluateGameSituation(sit, Player.WHITE), newAlpha);
        assertEquals(evaluateGameSituation(sit, Player.WHITE), ai.getBestValues()[1]);
    }

    @Test
    public void alphaIsNotChangedIfWorseValueFound() {
        ChessBoard cb = sit.getChessBoard();
        Piece wp = new Piece(PAWN, 1, 4, Player.WHITE, "wp");
        Piece bp = new Piece(PAWN, 5, 6, Player.BLACK, "bp");
        putPieceOnBoard(cb, wp);
        putPieceOnBoard(cb, bp);
        sit.reHashBoard(true);

        ai.getBestValues()[1] = 100;
        ai.setSituation(sit);
        ai.setStart(System.currentTimeMillis());
        int newAlpha = ai.checkForChange(wp, cb.getSquare(1, 4), 1, Player.WHITE, 100, 100, 123456789);
        assertEquals(100, newAlpha);
        assertEquals(100, ai.getBestValues()[1]);
    }

    @Test
    public void moveIsNotEvaluatedIfItIsIllegal() {
        ChessBoard cb = sit.getChessBoard();
        MovementLogic ml = cb.getMovementLogic();
        Piece wk = new Piece(KING, 1, 0, Player.WHITE, "wk");
        Piece wb = new Piece(BISHOP, 1, 4, Player.WHITE, "wp");
        Piece br = new Piece(ROOK, 1, 7, Player.BLACK, "br");
        putPieceOnBoard(cb, wk);
        putPieceOnBoard(cb, wb);
        putPieceOnBoard(cb, br);
        sit.reHashBoard(true);
        ml.move(wb, cb.getSquare(2, 3), sit);

        ai.setSituation(sit);
        ai.getBestValues()[1] = -250;
        ai.setStart(System.currentTimeMillis());
        int newAlpha = ai.checkForChange(wb, cb.getSquare(1, 4), 1, Player.WHITE, -250, -250, 123456789);
        assertEquals(-250, newAlpha);
        assertEquals(-250, ai.getBestValues()[1]);
    }

//    @Test
//    public void checkForChangeSavesFoundValueInTranspositionTable() {
//        ChessBoard cb = situation.getChessBoard();
//        Pawn wp = new Pawn(1, 4, Player.WHITE, "wp");
//        Pawn bp = new Pawn(5, 6, Player.BLACK, "bp");
//        putPieceOnBoard(cb, wp);
//        putPieceOnBoard(cb, bp);
//        situation.reHashBoard(true);
//
//        ai.setSituation(situation);
//        ai.setStart(System.currentTimeMillis());
//        ai.getTranspositionTable().clear();
//        assertTrue(ai.getTranspositionTable().isEmpty());
//        ai.checkForChange(Player.WHITE, 1, -123456789, 123456789, wp, cb.getSquare(1, 3));
//        TranspositionKey key = new TranspositionKey(1, Player.WHITE, situation.getBoardHash());
//        assertFalse(ai.getTranspositionTable().isEmpty());
//        assertTrue(ai.getTranspositionTable().containsKey(key));
//        assertEquals(evaluateGameSituation(situation, Player.WHITE), (int) ai.getTranspositionTable().get(key));
//    }
    @Test
    public void testAMoveReturnsAlphaIfTimeIsUpAndDoesNotChangeBoard() {
        ChessBoard cb = sit.getChessBoard();
        Piece wr = new Piece(ROOK, 1, 4, Player.WHITE, "wr");
        Piece bp = new Piece(PAWN, 5, 6, Player.BLACK, "bp");
        putPieceOnBoard(cb, wr);
        putPieceOnBoard(cb, bp);
        sit.reHashBoard(true);
        ChessBoard backUp = copy(cb);
        ai.setTimeLimit(1000);
        ai.setStart(System.currentTimeMillis() - 1000);
        assertEquals(-12345, ai.testAMove(wr, cb.getSquare(1, 6), cb.getSquare(1, 4), Player.WHITE, 1, -12345, -12345, 123456789, backUp));
        assertTrue(ChessBoardCopier.chessBoardsAreDeeplyEqual(cb, backUp));
    }

    @Test
    public void boardDoesNotChangeWhenTestingAMove() {
        ChessBoard cb = sit.getChessBoard();
        Piece wr = new Piece(ROOK, 1, 4, Player.WHITE, "wr");
        Piece bp = new Piece(PAWN, 5, 6, Player.BLACK, "bp");
        putPieceOnBoard(cb, wr);
        putPieceOnBoard(cb, bp);
        sit.reHashBoard(true);
        ChessBoard backUp = copy(cb);
        ai.setStart(System.currentTimeMillis());
        ai.setSituation(sit);
        ai.testAMove(wr, cb.getSquare(1, 6), cb.getSquare(1, 4), Player.WHITE, 1, -12345, -12345, 1234567, backUp);
        assertTrue(ChessBoardCopier.chessBoardsAreDeeplyEqual(cb, backUp));
    }

    @Test
    public void testAMoveReturnsValueOfSituationAfterMakingChosenMove() {
        ChessBoard cb = sit.getChessBoard();
        Piece wr = new Piece(ROOK, 1, 4, Player.WHITE, "wr");
        Piece bp = new Piece(PAWN, 5, 6, Player.BLACK, "bp");
        putPieceOnBoard(cb, wr);
        putPieceOnBoard(cb, bp);
        sit.reHashBoard(true);
        ChessBoard backUp = copy(cb);
        ai.setSituation(sit);
        ai.setStart(System.currentTimeMillis());
        assertEquals(470, ai.testAMove(wr, cb.getSquare(1, 6), cb.getSquare(1, 4), Player.WHITE, 1, -12345, -12345, 1234567, backUp));
    }

    @Test
    public void testAMoveRevertsPossiblePromotion() {
        ChessBoard cb = sit.getChessBoard();
        Piece wp = new Piece(PAWN, 1, 6, Player.WHITE, "wp");
        Piece bp = new Piece(PAWN, 5, 6, Player.BLACK, "bp");
        putPieceOnBoard(cb, wp);
        putPieceOnBoard(cb, bp);
        sit.reHashBoard(true);
        ai.setStart(System.currentTimeMillis());
        ai.setSituation(sit);
        ChessBoard backUp = copy(cb);
        ai.testAMove(wp, cb.getSquare(1, 7), cb.getSquare(1, 6), Player.WHITE, 1, -12345, -12345, 1234567, backUp);
        assertTrue(ChessBoardCopier.chessBoardsAreDeeplyEqual(cb, backUp));
        assertEquals(PAWN, cb.getSquare(1, 6).getPiece().getKlass());
    }

    @Test
    public void testAMoveRevertsPossiblePromotionEvenIfMovesMadeAfterIt() {
        ChessBoard cb = sit.getChessBoard();
        Piece wp = new Piece(PAWN, 1, 6, Player.WHITE, "wp");
        Piece bp = new Piece(PAWN, 5, 6, Player.BLACK, "bp");
        putPieceOnBoard(cb, wp);
        putPieceOnBoard(cb, bp);
        sit.reHashBoard(true);
        ai.setStart(System.currentTimeMillis());
        ai.setSituation(sit);
        ai.setSearchDepth(2);
        ChessBoard backUp = copy(cb);
        ai.testAMove(wp, cb.getSquare(1, 7), cb.getSquare(1, 6), Player.WHITE, 2, -12345, -12345, 1234567, backUp);
        assertTrue(ChessBoardCopier.chessBoardsAreDeeplyEqual(cb, backUp));
        assertEquals(PAWN, cb.getSquare(1, 6).getPiece().getKlass());
    }

    @Test
    public void tryMovingPieceStopsIfTimeLimitReached() {
        ChessBoard cb = sit.getChessBoard();
        Piece wr = new Piece(ROOK, 1, 4, Player.WHITE, "wr");
        Piece bp = new Piece(PAWN, 5, 6, Player.BLACK, "bp");
        putPieceOnBoard(cb, wr);
        putPieceOnBoard(cb, bp);
        sit.reHashBoard(true);
        ChessBoard backUp = copy(cb);
        ai.setSituation(sit);
        ai.setStart(System.currentTimeMillis() - 1000);
        assertEquals(-12345, ai.tryMovingPiece(1, 1, wr, cb.getSquare(1, 4), -12345, -12345, 123456, Player.WHITE, backUp));
    }

    @Test
    public void tryMovingPieceReturnsHighestValueForSituationAfterMovingPiece() {
        ChessBoard cb = sit.getChessBoard();
        Piece wr = new Piece(ROOK, 1, 4, Player.WHITE, "wr");
        Piece bp = new Piece(PAWN, 5, 6, Player.BLACK, "bp");
        putPieceOnBoard(cb, wr);
        putPieceOnBoard(cb, bp);
        sit.reHashBoard(true);
        ChessBoard backUp = copy(cb);
        ai.setSituation(sit);
        ai.setStart(System.currentTimeMillis());
        assertEquals(500, ai.tryMovingPiece(1, 1, wr, cb.getSquare(1, 4), -12345, -12345, 123456, Player.WHITE, backUp));
    }

    @Test
    public void whenMoveDoesNotProduceBetaCutOffItIsSavedAsKillerCandidate() {
        ChessBoard cb = sit.getChessBoard();
        Piece wp = new Piece(PAWN, 1, 6, Player.WHITE, "wp");
        Piece bp = new Piece(PAWN, 5, 6, Player.BLACK, "bp");
        putPieceOnBoard(cb, wp);
        putPieceOnBoard(cb, bp);
        sit.reHashBoard(true);
        ChessBoard backUp = copy(cb);

        ai.setSituation(sit);
        ai.setSearchDepth(1);
        ai.getKillerCandidates()[0] = null;

        ai.setStart(System.currentTimeMillis());
        ai.tryMovingPiece(1, 1, wp, cb.getSquare(1, 6), -12345, -12345, 123456, Player.WHITE, backUp);
        assertNotEquals(null, ai.getKillerCandidates()[0]);
    }

    @Test
    public void whenMoveDoesProduceBetaCutOffItIsNotSavedAsKillerCandidate() {
        ChessBoard cb = sit.getChessBoard();
        Piece wp = new Piece(PAWN, 1, 2, Player.WHITE, "wp");
        Piece bp = new Piece(PAWN, 5, 1, Player.BLACK, "bp");
        putPieceOnBoard(cb, wp);
        putPieceOnBoard(cb, bp);
        sit.reHashBoard(true);
        ChessBoard backUp = copy(cb);

        ai.setSearchDepth(1);
        ai.getKillerCandidates()[0] = null;

        ai.setStart(System.currentTimeMillis());
        ai.tryMovingPiece(1, 1, wp, cb.getSquare(1, 2), -12345, -12345, 0, Player.WHITE, backUp);
        assertEquals(null, ai.getKillerCandidates()[0]);
    }

    @Test
    public void promotionDoesNotChangeChessBoardSituationInComplexSituation() {
        ChessBoard cb = sit.getChessBoard();

        Piece bk = new Piece(KING, 1, 1, Player.BLACK, "bk");
        Piece wk = new Piece(KING, 4, 5, Player.WHITE, "wk");
        Piece bp1 = new Piece(PAWN, 1, 2, Player.BLACK, "bp1");
        Piece wp1 = new Piece(PAWN, 1, 3, Player.WHITE, "wp1");
        Piece wn1 = new Piece(KNIGHT, 2, 2, Player.WHITE, "wn1");
        Piece bp2 = new Piece(PAWN, 5, 5, Player.BLACK, "bp2");
        Piece wp2 = new Piece(PAWN, 6, 3, Player.WHITE, "wp2");
        Piece wp3 = new Piece(PAWN, 7, 4, Player.WHITE, "wp3");

        bp1.setHasBeenMoved(true);
        bp2.setHasBeenMoved(true);
        wp1.setHasBeenMoved(true);
        wp2.setHasBeenMoved(true);
        wp3.setHasBeenMoved(true);

        putPieceOnBoard(cb, bk);
        putPieceOnBoard(cb, wk);
        putPieceOnBoard(cb, bp1);
        putPieceOnBoard(cb, wp1);
        putPieceOnBoard(cb, wn1);
        putPieceOnBoard(cb, bp2);
        putPieceOnBoard(cb, wp2);
        putPieceOnBoard(cb, wp3);

        sit.reHashBoard(true);

        ChessBoard bu = copy(cb);
        ai.setTimeLimit(1000);
        ai.setStart(System.currentTimeMillis());
        ai.findBestMoves(sit);
        assertTrue(chessBoardsAreDeeplyEqual(cb, bu));
    }
}
