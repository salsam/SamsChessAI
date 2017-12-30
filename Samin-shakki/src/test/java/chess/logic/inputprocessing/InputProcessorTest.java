package chess.logic.inputprocessing;

import chess.domain.Game;
import chess.domain.board.Player;
import chess.domain.board.Square;
import chess.logic.chessboardinitializers.*;
import chess.domain.board.Piece;
import static chess.domain.board.Klass.*;
import chess.gui.GameWindow;
import chess.logic.chessboardinitializers.StandardChessBoardInitializer;
import java.util.HashMap;
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
    private Game game;
    private static ChessBoardInitializer init;

    public InputProcessorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        init = new StandardChessBoardInitializer();
        output = new JLabel("");
    }

    @Before
    public void setUp() {
        game = new Game();
        inputProcessor = game.getInput();
        inputProcessor.setTextArea(output);
        Map<String, JFrame> frames = new HashMap();
        frames.put("game", new GameWindow(game));
        inputProcessor.setFrames(frames);
        output.setText("");
        game.start();
    }

    @Test
    public void ifNoPieceIsSelectedChosenAndPossibilitiesAreNull() {
        assertEquals(null, inputProcessor.getPossibilities());
        assertEquals(null, inputProcessor.getChosen());
    }

    @Test
    public void ifNoPieceIsChosenSelectPieceOnTargetSquare() {
        inputProcessor.processClick(1, 6);
        assertEquals(game.getSituation().getChessBoard().getPiece(1, 6), inputProcessor.getChosen());
    }

    @Test
    public void ifNoPieceIsSelectedAndTargetSquareContainsNoPieceIrOpposingPieceNoPieceIsSelected() {
        inputProcessor.processClick(4, 4);
        assertEquals(null, inputProcessor.getChosen());
        inputProcessor.processClick(6, 1);
        assertEquals(null, inputProcessor.getChosen());
    }

    @Test
    public void whenPieceIsSelectedItsPossibleMovesAreSavedInFieldPossibilities() {
        inputProcessor.processClick(1, 6);
        assertTrue(inputProcessor.getPossibilities().contains(new Square(1, 5)));
        assertTrue(inputProcessor.getPossibilities().contains(new Square(1, 4)));
    }

    @Test
    public void ifAnotherPieceOwnedPieceIsClickedItIsSetAsChosen() {
        inputProcessor.processClick(1, 6);
        inputProcessor.processClick(1, 7);

        assertTrue(inputProcessor.getPossibilities().contains(new Square(0, 5)));
        assertTrue(inputProcessor.getPossibilities().contains(new Square(2, 5)));
        assertFalse(inputProcessor.getPossibilities().contains(new Square(1, 5)));
        assertFalse(inputProcessor.getPossibilities().contains(new Square(1, 4)));
    }

    @Test
    public void ifPieceIsChosenAndThenAPossibilityIsClickedMoveToThatSquare() {
        inputProcessor.processClick(1, 6);
        Piece piece = game.getSituation().getChessBoard().getPiece(1, 6);
        inputProcessor.processClick(1, 5);
        assertFalse(game.getSituation().getChessBoard().getSquare(1, 6).containsAPiece());
        assertTrue(game.getSituation().getChessBoard().getSquare(1, 5).containsAPiece());
        assertEquals(piece, game.getSituation().getChessBoard().getPiece(1, 5));
    }

    @Test
    public void outputTellsWhoseTurnItIsCorrectly() {
        inputProcessor.processClick(1, 6);
        inputProcessor.processClick(1, 5);
        assertEquals("BLACK's turn.", inputProcessor.getText());
        inputProcessor.processClick(1, 1);
        inputProcessor.processClick(1, 2);
        assertEquals("WHITE's turn.", inputProcessor.getText());
    }

    @Test
    public void outputTellsIfPlayerIsChecked() {
        EmptyBoardInitializer emptyinit = new EmptyBoardInitializer();
        emptyinit.initialize(game.getSituation().getChessBoard());
        ChessBoardInitializer.putPieceOnBoard(game.getSituation().getChessBoard(), new Piece(KING, 4, 7, Player.WHITE, "wk"));
        ChessBoardInitializer.putPieceOnBoard(game.getSituation().getChessBoard(), new Piece(PAWN, 4, 5, Player.BLACK, "bp1"));
        inputProcessor.processClick(4, 7);
        inputProcessor.processClick(5, 7);
        inputProcessor.processClick(4, 5);
        inputProcessor.processClick(4, 6);

        assertEquals("WHITE's turn. Check!", inputProcessor.getText());
    }

    @Test
    public void outputTellsIfGameHasEndedInCheckMate() {
        Map<String, JFrame> frames = new HashMap<>();
        frames.put("endingScreen", new JFrame());
        frames.put("game", new GameWindow(game));
        frames.get("endingScreen").setVisible(false);
        inputProcessor.setFrames(frames);
        EmptyBoardInitializer emptyinit = new EmptyBoardInitializer();
        emptyinit.initialize(game.getSituation().getChessBoard());
        ChessBoardInitializer.putPieceOnBoard(game.getSituation().getChessBoard(), new Piece(KING, 1, 0, Player.WHITE, "wk"));
        ChessBoardInitializer.putPieceOnBoard(game.getSituation().getChessBoard(), new Piece(QUEEN, 4, 1, Player.BLACK, "bq"));
        ChessBoardInitializer.putPieceOnBoard(game.getSituation().getChessBoard(), new Piece(ROOK, 7, 1, Player.BLACK, "br1"));
        inputProcessor.processClick(1, 0);
        inputProcessor.processClick(0, 0);
        inputProcessor.processClick(4, 1);
        inputProcessor.processClick(1, 1);

        assertEquals("Checkmate! BLACK won!", inputProcessor.getText());
    }

    @Test
    public void outputTellsIfGameHasEndedInStaleMate() {
        Map<String, JFrame> frames = new HashMap<>();
        frames.put("endingScreen", new JFrame());
        frames.put("game", new JFrame());
        frames.get("endingScreen").setVisible(false);
        inputProcessor.setFrames(frames);
        EmptyBoardInitializer emptyinit = new EmptyBoardInitializer();
        emptyinit.initialize(game.getSituation().getChessBoard());
        ChessBoardInitializer.putPieceOnBoard(game.getSituation().getChessBoard(), new Piece(KING, 1, 0, Player.WHITE, "wk"));
        ChessBoardInitializer.putPieceOnBoard(game.getSituation().getChessBoard(), new Piece(QUEEN, 1, 7, Player.BLACK, "bq"));
        ChessBoardInitializer.putPieceOnBoard(game.getSituation().getChessBoard(), new Piece(ROOK, 7, 1, Player.BLACK, "br1"));
        inputProcessor.processClick(1, 0);
        inputProcessor.processClick(0, 0);
        inputProcessor.processClick(1, 7);
        inputProcessor.processClick(1, 6);

        assertEquals("Stalemate! Game ended as a draw!", inputProcessor.getText());
    }
}
