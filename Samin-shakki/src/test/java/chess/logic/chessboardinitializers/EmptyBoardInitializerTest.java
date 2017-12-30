package chess.logic.chessboardinitializers;

import chess.domain.board.ChessBoard;
import chess.domain.board.Player;
import chess.logic.movementlogic.MovementLogic;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sami
 */
public class EmptyBoardInitializerTest {

    private static ChessBoardInitializer init;

    public EmptyBoardInitializerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        init = new EmptyBoardInitializer();
    }

    @Test
    public void initializerClearsBoardOfAllPiecesWhenInitializingABoard() {
        ChessBoard board = new ChessBoard(new MovementLogic());
        ChessBoardInitializer stdinit = new StandardChessBoardInitializer();
        stdinit.initialize(board);
        init.initialize(board);

        for (int i = 0; i < board.getTable().length; i++) {
            for (int j = 0; j < board.getTable()[0].length; j++) {
                assertTrue(board.getPiece(i, j) == null);
            }
        }
    }

    @Test
    public void initializerClearsAllPiecesFromPlayers() {
        ChessBoard board = new ChessBoard(new MovementLogic());
        ChessBoardInitializer stdinit = new StandardChessBoardInitializer();
        stdinit.initialize(board);
        init.initialize(board);

        assertTrue(board.getPieces(Player.WHITE).isEmpty());
        assertTrue(board.getPieces(Player.BLACK).isEmpty());
    }
}
