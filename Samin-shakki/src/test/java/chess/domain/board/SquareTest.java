package chess.domain.board;

import chess.domain.datastructures.MyHashSet;
import static chess.domain.board.Klass.PAWN;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sami
 */
public class SquareTest {

    public static void testMultipleSquares(int[] columns, int[] rows, Set<Square> possibleMoves) {
        Set<Square> realPossibilities = new MyHashSet<>();
        for (int i = 0; i < columns.length; i++) {
            realPossibilities.add(new Square(columns[i], rows[i]));
        }
        for (Square sq : realPossibilities) {
            assertTrue(possibleMoves.contains(sq));
        }
    }

    private Square square;

    public SquareTest() {
    }

    @Before
    public void setUp() {
        square = new Square(2, 3);
    }

    @Test
    public void returnCorrectColumn() {
        assertEquals(2, square.getColumn());
    }

    @Test
    public void returnCorrectRow() {
        assertEquals(3, square.getRow());
    }

    @Test
    public void pieceNullIfNoPieceOnThisSquare() {
        assertEquals(null, square.getPiece());
    }

    @Test
    public void pieceCorrectIfNotNull() {
        Piece pawn = new Piece(PAWN, 2, 1, Player.WHITE, "wp");
        square.setPiece(pawn);
        assertEquals(pawn, square.getPiece());
    }

    @Test
    public void twoSquaresAreEqualIfSameColumnAndRow() {
        assertTrue(square.equals(new Square(2, 3)));
    }

    @Test
    public void twoSquaresAreNotEqualIfDifferentColumn() {
        assertFalse(square.equals(new Square(3, 3)));
    }

    @Test
    public void twoSquaresAreNotEqualIfDifferentRow() {
        assertFalse(square.equals(new Square(2, 2)));
    }

    @Test
    public void intIsNotASquare() {
        assertFalse(square.equals(1));
    }

    @Test
    public void toStringIsInCorrectFormat() {
        assertEquals("(2,3)", square.toString());
    }

    @Test
    public void cloneReturnsEqualsSquare() {
        assertEquals(square, square.clone());
    }

    @Test
    public void cloneDoesNotReferToSameSquare() {
        Square clone = square.clone();
        square = new Square(1, 2);
        assertNotEquals(square, clone);
    }
}
