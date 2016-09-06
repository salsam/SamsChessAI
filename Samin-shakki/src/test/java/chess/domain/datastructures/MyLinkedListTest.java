package chess.domain.datastructures;

import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sami
 */
public class MyLinkedListTest {

    private MyLinkedList<Integer> mll;

    public MyLinkedListTest() {
    }

    @Before
    public void setUp() {
        mll = new MyLinkedList();
    }

    @Test
    public void isEmptyTrueBeforeAddingElements() {
        assertTrue(mll.isEmpty());
    }

    @Test
    public void isEmptyFalseAfterAddingElements() {
        mll.add(1);
        assertFalse(mll.isEmpty());
    }

    @Test
    public void sizeIsZeroBeforeAddingElements() {
        assertEquals(0, mll.size());
    }

    @Test
    public void sizeGoesUpByOneWhenAddingElements() {
        mll.add(1);
        assertEquals(1, mll.size());
        mll.add(2);
        assertEquals(2, mll.size());
        mll.add(42);
        mll.add(7);
        mll.add(-273);
        assertEquals(5, mll.size());
    }

    @Test
    public void containsReturnsFalseIfListIsEmpty() {
        assertFalse(mll.contains(3));
    }

    @Test
    public void containsReturnsFalseIfListDoesNotContainElement() {
        mll.add(7);
        assertFalse(mll.contains(42));
    }

    @Test
    public void containsTrueIfListContainsElement() {
        mll.add(7);
        mll.add(-273);
        mll.add(42);
        mll.add(21);
        mll.add(13);
        assertTrue(mll.contains(7));
        assertTrue(mll.contains(13));
        assertTrue(mll.contains(42));
    }

    @Test
    public void removeReturnsFalseWhenListEmpty() {
        assertFalse(mll.remove(Integer.valueOf(1)));
    }

    @Test
    public void removeReturnsFalseIfListDoesNotContainItsParameter() {
        mll.add(3);
        mll.add(42);
        mll.add(101);
        assertFalse(mll.remove(Integer.valueOf(7)));
    }

    @Test
    public void sizeRemainsUnchangedWhenListDoesNotContainRemovesParameter() {
        mll.add(3);
        mll.add(42);
        mll.add(101);
        mll.remove(Integer.valueOf(7));
        assertEquals(3, mll.size());
    }

    @Test
    public void removeReturnsTrueIfListContainsItsParameter() {
        mll.add(3);
        assertTrue(mll.remove(Integer.valueOf(3)));
    }

    @Test
    public void sizeGoesDownWhenObjectIsRemoved() {
        mll.add(3);
        mll.remove(Integer.valueOf(3));
        assertEquals(0, mll.size());
    }

    @Test
    public void containsFalseAfterObjectIsRemoved() {
        mll.add(42);
        mll.remove(Integer.valueOf(42));
        assertFalse(mll.contains(42));
    }

    @Test
    public void onlyFirstInstanceOfSpecifiedElementIsRemoved() {
        mll.add(42);
        mll.add(42);
        assertEquals(2, mll.size());
        mll.remove(Integer.valueOf(42));
        assertEquals(1, mll.size());
        assertTrue(mll.contains(42));
    }

    @Test
    public void listIsEmptyIfAllElementsAreRemoved() {
        mll.add(42);
        mll.remove(Integer.valueOf(42));
        assertTrue(mll.isEmpty());
    }

    @Test
    public void listIsEmptyAfterClearing() {
        mll.add(42);
        mll.clear();
        assertTrue(mll.isEmpty());
        assertEquals(0, mll.size());
        assertFalse(mll.contains(42));
    }

    @Test
    public void iteratorStartsFromFirstNode() {
        mll.add(42);
        mll.add(314);
        Iterator<Integer> it = mll.iterator();
        assertTrue(it.hasNext());
        assertEquals(42, (int) it.next());
    }

    @Test
    public void iteratorHasNextUntilEndOfListIsReached() {
        mll.add(7);
        mll.add(-273);
        mll.add(42);
        mll.add(21);
        mll.add(13);
        Iterator<Integer> it = mll.iterator();

        for (int i = 0; i < 5; i++) {
            assertTrue(it.hasNext());
            it.next();
        }

        assertFalse(it.hasNext());
    }

    @Test
    public void iteratorDoesNotHaveNextIfListEmpty() {
        Iterator<Integer> it = mll.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    public void iteratorIteratesOverWholeList() {
        mll.add(7);
        mll.add(-273);
        mll.add(42);
        mll.add(21);
        mll.add(13);

        Iterator<Integer> it = mll.iterator();
        int sum = 0;

        while (it.hasNext()) {
            sum += it.next();
        }

        assertEquals(-190, sum);
    }

}
