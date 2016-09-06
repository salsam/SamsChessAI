package chess.domain.board.betterpiece;

import chess.domain.board.Piece;
import chess.domain.board.Player;
import static chess.domain.board.Klass.ROOK;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sami
 */
public class RookTest {

    private Piece rook;

    public RookTest() {
    }

    @Before
    public void setUp() {
        rook = new Piece(ROOK, 7, 5, Player.WHITE, "wr");
    }

    @Test
    public void rooksAreEqualIfSamePieceCode() {
        assertTrue(rook.equals(new Piece(ROOK, 4, 5, Player.BLACK, "wr")));
    }

    @Test
    public void rooksAreNotEqualIfDifferentPieceCode() {
        assertFalse(rook.equals(new Piece(ROOK, 7, 5, Player.WHITE, "wr1")));
    }

    @Test
    public void cloneReturnsDifferentRook() {
        assertFalse(rook == rook.clone());
    }

    @Test
    public void cloneReturnsIdenticalRook() {
        assertTrue(rook.equals(rook.clone()));
    }
}
