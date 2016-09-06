package chess.logic.inputprocessing;

import chess.domain.board.Player;
import chess.domain.board.Square;
import chess.logic.chessboardinitializers.ChessBoardInitializer;
import chess.logic.chessboardinitializers.EmptyBoardInitializer;
import chess.domain.GameSituation;
import chess.domain.datastructures.MyHashMap;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.KING;
import static chess.domain.board.Klass.PAWN;
import static chess.domain.board.Klass.QUEEN;
import static chess.domain.board.Klass.ROOK;
import chess.logic.chessboardinitializers.BetterChessBoardInitializer;
import chess.logic.movementlogic.MovementLogic;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JLabel;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author sami
 */
public class InputProcessorTest {

    private InputProcessor inputProcessor;
    private static JLabel output;
    private GameSituation game;
    private static ChessBoardInitializer init;

    public InputProcessorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        init = new BetterChessBoardInitializer();
        output = new JLabel("");
    }

    @Before
    public void setUp() {
        game = new GameSituation(init, new MovementLogic());
        inputProcessor = new InputProcessor();
        inputProcessor.setTextArea(output);
        output.setText("");
    }

    @Test
    public void ifNoPieceIsSelectedChosenAndPossibilitiesAreNull() {
        assertEquals(null, inputProcessor.getPossibilities());
        assertEquals(null, inputProcessor.getChosen());
    }

    @Test
    public void ifNoPieceIsChosenSelectPieceOnTargetSquare() {
        inputProcessor.processClick(1, 6, game);
        assertEquals(game.getChessBoard().getSquare(1, 6).getPiece(), inputProcessor.getChosen());
    }

    @Test
    public void ifNoPieceIsSelectedAndTargetSquareContainsNoPieceIrOpposingPieceNoPieceIsSelected() {
        inputProcessor.processClick(4, 4, game);
        assertEquals(null, inputProcessor.getChosen());
        inputProcessor.processClick(6, 1, game);
        assertEquals(null, inputProcessor.getChosen());
    }

    @Test
    public void whenPieceIsSelectedItsPossibleMovesAreSavedInFieldPossibilities() {
        inputProcessor.processClick(1, 6, game);
        assertTrue(inputProcessor.getPossibilities().contains(new Square(1, 5)));
        assertTrue(inputProcessor.getPossibilities().contains(new Square(1, 4)));
    }

    @Test
    public void ifAnotherPieceOwnedPieceIsClickedItIsSetAsChosen() {
        inputProcessor.processClick(1, 6, game);
        inputProcessor.processClick(1, 7, game);

        assertTrue(inputProcessor.getPossibilities().contains(new Square(0, 5)));
        assertTrue(inputProcessor.getPossibilities().contains(new Square(2, 5)));
        assertFalse(inputProcessor.getPossibilities().contains(new Square(1, 5)));
        assertFalse(inputProcessor.getPossibilities().contains(new Square(1, 4)));
    }

    @Test
    public void ifPieceIsChosenAndThenAPossibilityIsClickedMoveToThatSquare() {
        inputProcessor.processClick(1, 6, game);
        Piece piece = game.getChessBoard().getSquare(1, 6).getPiece();
        inputProcessor.processClick(1, 5, game);
        assertFalse(game.getChessBoard().getSquare(1, 6).containsAPiece());
        assertTrue(game.getChessBoard().getSquare(1, 5).containsAPiece());
        assertEquals(piece, game.getChessBoard().getSquare(1, 5).getPiece());
    }

    @Test
    public void ifPawnIsMovedToOpposingEndOfBoardItIsPromotedToQueenOnSameSquare() {
        ChessBoardInitializer.putPieceOnBoard(game.getChessBoard(), new Piece(PAWN, 1, 1, Player.WHITE, "wp9"));
        inputProcessor.processClick(1, 1, game);
        inputProcessor.processClick(0, 0, game);

        assertTrue(game.getChessBoard().getSquare(0, 0).containsAPiece());
        assertTrue(game.getChessBoard().getSquare(0, 0).getPiece().getKlass() == QUEEN);
        Piece q = game.getChessBoard().getSquare(0, 0).getPiece();
        assertEquals(Player.WHITE, q.getOwner());
        assertEquals("wp9", q.getPieceCode());
    }

    @Test
    public void ifPawnIsMovedToOpposingEndOfBoardItIsReplacedByQueenWithSamePieceCode() {
        ChessBoardInitializer.putPieceOnBoard(game.getChessBoard(), new Piece(PAWN, 1, 1, Player.WHITE, "wp9"));
        inputProcessor.processClick(1, 1, game);
        inputProcessor.processClick(0, 0, game);
        assertTrue(game.getChessBoard().getPieces(Player.WHITE).stream()
                .noneMatch(whitePiece -> whitePiece.getPieceCode().equals("wp9")
                        && whitePiece.getKlass() == PAWN));

        assertTrue(game.getChessBoard().getPieces(Player.WHITE).stream()
                .anyMatch(whitePiece -> whitePiece.getPieceCode().equals("wp9")));
    }

    @Test
    public void outputTellsWhoseTurnItIsCorrectly() {
        inputProcessor.processClick(1, 6, game);
        inputProcessor.processClick(1, 5, game);
        assertEquals("BLACK's turn.", output.getText());
        inputProcessor.processClick(1, 1, game);
        inputProcessor.processClick(1, 2, game);
        assertEquals("WHITE's turn.", output.getText());
    }

    @Test
    public void outputTellsIfPlayerIsChecked() {
        EmptyBoardInitializer emptyinit = new EmptyBoardInitializer();
        emptyinit.initialize(game.getChessBoard());
        ChessBoardInitializer.putPieceOnBoard(game.getChessBoard(), new Piece(KING, 4, 7, Player.WHITE, "wk"));
        ChessBoardInitializer.putPieceOnBoard(game.getChessBoard(), new Piece(PAWN, 4, 5, Player.BLACK, "bp1"));
        inputProcessor.processClick(4, 7, game);
        inputProcessor.processClick(5, 7, game);
        inputProcessor.processClick(4, 5, game);
        inputProcessor.processClick(4, 6, game);

        assertEquals("WHITE's turn. Check!", output.getText());
    }

    @Test
    public void outputTellsIfGameHasEndedInCheckMate() {
        Map<String, JFrame> frames = new MyHashMap<>();
        frames.put("endingScreen", new JFrame());
        frames.get("endingScreen").setVisible(false);
        inputProcessor.setFrames(frames);
        EmptyBoardInitializer emptyinit = new EmptyBoardInitializer();
        emptyinit.initialize(game.getChessBoard());
        ChessBoardInitializer.putPieceOnBoard(game.getChessBoard(), new Piece(KING, 1, 0, Player.WHITE, "wk"));
        ChessBoardInitializer.putPieceOnBoard(game.getChessBoard(), new Piece(QUEEN, 4, 1, Player.BLACK, "bq"));
        ChessBoardInitializer.putPieceOnBoard(game.getChessBoard(), new Piece(ROOK, 7, 1, Player.BLACK, "br1"));
        inputProcessor.processClick(1, 0, game);
        inputProcessor.processClick(0, 0, game);
        inputProcessor.processClick(4, 1, game);
        inputProcessor.processClick(1, 1, game);

        assertEquals("Checkmate! BLACK won!", output.getText());
    }

    @Test
    public void outputTellsIfGameHasEndedInStaleMate() {
        Map<String, JFrame> frames = new MyHashMap<>();
        frames.put("endingScreen", new JFrame());
        frames.get("endingScreen").setVisible(false);
        inputProcessor.setFrames(frames);
        EmptyBoardInitializer emptyinit = new EmptyBoardInitializer();
        emptyinit.initialize(game.getChessBoard());
        ChessBoardInitializer.putPieceOnBoard(game.getChessBoard(), new Piece(KING, 1, 0, Player.WHITE, "wk"));
        ChessBoardInitializer.putPieceOnBoard(game.getChessBoard(), new Piece(QUEEN, 1, 7, Player.BLACK, "bq"));
        ChessBoardInitializer.putPieceOnBoard(game.getChessBoard(), new Piece(ROOK, 7, 1, Player.BLACK, "br1"));
        inputProcessor.processClick(1, 0, game);
        inputProcessor.processClick(0, 0, game);
        inputProcessor.processClick(1, 7, game);
        inputProcessor.processClick(1, 6, game);

        assertEquals("Stalemate! Game ended as a draw!", output.getText());
    }
}
