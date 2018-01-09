package chess.logic.ailogic;

import chess.domain.GameSituation;
import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.*;
import chess.domain.board.Square;
import static chess.logic.ailogic.GameSituationEvaluator.evaluateGameSituation;
import chess.logic.chessboardinitializers.StandardChessBoardInitializer;
import static chess.logic.chessboardinitializers.ChessBoardInitializer.putPieceOnBoard;
import chess.logic.chessboardinitializers.EmptyBoardInitializer;
import chess.logic.gamelogic.CheckingLogic;
import chess.logic.movementlogic.MovementLogic;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author sami
 */
public class GameSituationEvaluatorTest {

    private static GameSituation situation;
    private static EmptyBoardInitializer init;

    public GameSituationEvaluatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        init = new EmptyBoardInitializer();
        situation = new GameSituation(init, new MovementLogic());
    }

    @Before
    public void setUp() {
        //situation = new GameSituation(init, new MovementLogic());
        situation.reset();
        //init.initialize(situation.getChessBoard());
    }

    @Test
    public void emptyChessBoardValuedZero() {
        assertEquals(0, GameSituationEvaluator.evaluateWithoutWinConditions(situation, Player.BLACK));
        assertEquals(0, GameSituationEvaluator.evaluateWithoutWinConditions(situation, Player.WHITE));
    }

    @Test
    public void standardStartingPositionIsWorthZero() {
        GameSituation sit = new GameSituation(new StandardChessBoardInitializer(), new MovementLogic());
        assertEquals(0, evaluateGameSituation(sit, Player.BLACK));
        assertEquals(0, evaluateGameSituation(sit, Player.WHITE));
    }

    @Test
    public void gamesituationValuedNormallyWhenOpponentWillBeStaleMatedOnTheirTurn() {
        Piece queen = new Piece(QUEEN, 1, 1, Player.WHITE, "wp");
        putPieceOnBoard(situation.getChessBoard(), queen);
        assertEquals(880 + 230, evaluateGameSituation(situation, Player.WHITE));
    }

    @Test
    public void gameSitutionValuedZeroWhenStaleMate() {
        Piece queen = new Piece(QUEEN, 1, 1, Player.WHITE, "wp");
        putPieceOnBoard(situation.getChessBoard(), queen);
        situation.getChessBoard().printTable();
//        for (Piece p : situation.getChessBoard().getPieces(Player.BLACK)) {
//            System.out.println(p);
//        }
//        System.out.println(CheckingLogic.checkIfChecked(situation.getChessBoard(), Player.BLACK));
//        System.out.println(CheckingLogic.checkMate(situation, Player.BLACK));
        assertEquals(0, evaluateGameSituation(situation, Player.BLACK));
    }

    @Test
    public void gameSituationCanHaveNegativeValue() {
        Piece brook = new Piece(ROOK, 0, 0, Player.BLACK, "br");
        Piece bqueen = new Piece(QUEEN, 2, 1, Player.BLACK, "bq");
        Piece wpawn = new Piece(PAWN, 1, 2, Player.WHITE, "wp");

        putPieceOnBoard(situation.getChessBoard(), wpawn);
        putPieceOnBoard(situation.getChessBoard(), brook);
        putPieceOnBoard(situation.getChessBoard(), bqueen);

        assertEquals(-1615, evaluateGameSituation(situation, Player.WHITE));
    }

    @Test
    public void takenPiecesAreNotAccountedFor() {
        Piece brook = new Piece(ROOK, 0, 7, Player.BLACK, "br");
        Piece bqueen = new Piece(QUEEN, 2, 6, Player.BLACK, "bq");
        Piece wpawn = new Piece(PAWN, 1, 5, Player.WHITE, "wp");

        putPieceOnBoard(situation.getChessBoard(), wpawn);
        putPieceOnBoard(situation.getChessBoard(), brook);
        putPieceOnBoard(situation.getChessBoard(), bqueen);

        situation.getChessBoard().getMovementLogic()
                .move(wpawn, new Square(2, 6), situation);
        assertEquals(-530, evaluateGameSituation(situation, Player.WHITE));
    }

    @Test
    public void checkMateWorth123456789() {
        Piece wk = new Piece(KING, 0, 7, Player.WHITE, "wk");
        Piece bk = new Piece(KING, 2, 6, Player.BLACK, "bk");
        Piece bq = new Piece(QUEEN, 1, 2, Player.BLACK, "bq");
        ChessBoard cb = situation.getChessBoard();
        MovementLogic ml = cb.getMovementLogic();

        putPieceOnBoard(cb, wk);
        putPieceOnBoard(cb, bk);
        putPieceOnBoard(cb, bq);
        situation.reHashBoard(true);

        ml.move(bq, new Square(1, 6), situation);

        assertEquals(-123456789, evaluateGameSituation(situation, Player.WHITE));
    }

    @Test
    public void thirdRepetitionOfBoardSituationIsWorthZeroEvenWhenMated() {
        Piece wk = new Piece(KING, 0, 7, Player.WHITE, "wk");
        Piece bk = new Piece(KING, 2, 6, Player.BLACK, "bk");
        Piece bq = new Piece(QUEEN, 1, 2, Player.BLACK, "bq");
        ChessBoard cb = situation.getChessBoard();
        MovementLogic ml = cb.getMovementLogic();

        putPieceOnBoard(cb, wk);
        putPieceOnBoard(cb, bk);
        putPieceOnBoard(cb, bq);
        situation.reHashBoard(true);

        ml.move(bq, new Square(1, 6), situation);
        ml.move(bq, new Square(1, 2), situation);
        ml.move(bq, new Square(1, 6), situation);
        ml.move(bq, new Square(1, 2), situation);
        ml.move(bq, new Square(1, 6), situation);

        assertEquals(0, evaluateGameSituation(situation, Player.WHITE));
    }

    @Test
    public void thirdRepetitionOfBoardSituationIsWorthZero() {
        Piece wr = new Piece(ROOK, 0, 7, Player.WHITE, "wr");
        putPieceOnBoard(situation.getChessBoard(), wr);
        situation.reHashBoard(true);
        ChessBoard cb = situation.getChessBoard();
        MovementLogic ml = cb.getMovementLogic();
        ml.move(wr, new Square(0, 6), situation);
        ml.move(wr, new Square(0, 7), situation);
        ml.move(wr, new Square(0, 6), situation);
        ml.move(wr, new Square(0, 7), situation);

        assertEquals(0, evaluateGameSituation(situation, Player.WHITE));
    }

    @Test
    public void mobilityValueCorrectForPawn() {
        Piece wpawn = new Piece(PAWN, 1, 5, Player.WHITE, "wp");
        ChessBoard cb = situation.getChessBoard();
        putPieceOnBoard(cb, wpawn);
        assertEquals(20, GameSituationEvaluator.mobilityValue(situation, Player.WHITE));
        cb.getMovementLogic().move(wpawn, new Square(1, 6), situation);
        assertEquals(10, GameSituationEvaluator.mobilityValue(situation, Player.WHITE));
    }
}
