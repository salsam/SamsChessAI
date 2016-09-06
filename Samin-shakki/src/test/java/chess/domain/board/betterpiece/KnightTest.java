package chess.domain.board.betterpiece;

import chess.domain.board.Piece;
import chess.domain.board.Player;
import static chess.domain.board.Klass.KNIGHT;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sami
 */
public class KnightTest {

    private Piece knight;

    public KnightTest() {
    }

    @Before
    public void setUp() {
        knight = new Piece(KNIGHT, 2, 3, Player.BLACK, "bn");
    }

    @Test
    public void cloneReturnsDifferentKnight() {
        assertFalse(knight == knight.clone());
    }

    @Test
    public void cloneReturnsIdenticalKnight() {
        assertTrue(knight.deepEquals(knight.clone()));
    }
}
