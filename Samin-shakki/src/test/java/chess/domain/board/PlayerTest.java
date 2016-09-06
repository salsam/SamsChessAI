package chess.domain.board;

import static chess.domain.board.Player.getOpponent;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sami
 */
public class PlayerTest {

    public PlayerTest() {
    }

    @Test
    public void getOpponentReturnBlackIfPlayerIsWhite() {
        assertEquals(Player.BLACK, getOpponent(Player.WHITE));
    }

    @Test
    public void getOpponentReturnsWhiteIfPlayerIsWhite() {
        assertEquals(Player.WHITE, getOpponent(Player.BLACK));
    }
}
