package chess.domain.board.betterpiece;

import chess.domain.board.Piece;
import chess.domain.board.Player;
import static chess.domain.board.Klass.PAWN;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sami
 */
public class PawnTest {

    private Piece pawn;

    public PawnTest() {
    }

    @Before
    public void setUp() {
        pawn = new Piece(PAWN, 7, 5, Player.WHITE, "wp");
    }

    @Test
    public void pawnsAreNotEqualIfDifferentPieceCode() {
        assertFalse(pawn.equals(new Piece(PAWN, 7, 5, Player.WHITE, "wp1")));
    }

    @Test
    public void pawnsAreEqualIfSamePieceCode() {
        assertTrue(pawn.equals(new Piece(PAWN, 1, 5, Player.BLACK, "wp")));
    }

    @Test
    public void cloneReturnsDifferentPawn() {
        assertFalse(pawn == pawn.clone());
    }

    @Test
    public void cloneReturnsIdenticalPawn() {
        assertTrue(pawn.equals(pawn.clone()));
    }
}
