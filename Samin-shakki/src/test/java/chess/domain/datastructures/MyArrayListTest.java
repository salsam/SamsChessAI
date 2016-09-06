package chess.domain.datastructures;

import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sami
 */
public class MyArrayListTest {

    private MyArrayList test;

    public MyArrayListTest() {
    }

    @Before
    public void setUp() {
        test = new MyArrayList();
    }

    @Test
    public void listIsEmptyAfterInitialization() {
        assertTrue(test.isEmpty());
    }

    @Test
    public void sizeIsZeroAfterInitialilzation() {
        assertEquals(0, test.size());
    }

    @Test
    public void containsFalseIfListIsEmpty() {
        assertFalse(test.contains(42));
    }

    @Test
    public void listNoLongerEmptyAfterAddingAnElement() {
        test.add(1);
        assertFalse(test.isEmpty());
    }

    @Test
    public void sizeGoesUpByOneEachTimeAnElementIsAdded() {
        test.add(1);
        assertEquals(1, test.size());
        test.add(2);
        test.add(3);
        test.add(4);
        test.add(-237);
        assertEquals(5, test.size());
    }

    @Test
    public void addReturnsTrueWhenElementIsAdded() {
        assertTrue(test.add(42));
    }

    @Test
    public void listContainsAddedElements() {
        test.add(42);
        test.add(7);
        test.add(-273);
        assertTrue(test.contains(42));
        assertTrue(test.contains(7));
        assertTrue(test.contains(-273));
    }

    @Test
    public void listDoesNotContainElementsThatHaveNotBeenAddedToIt() {
        test.add(42);
        test.add(101);
        test.add(-273);
        assertFalse(test.contains(314));
        assertFalse(test.contains(3.14));
    }

    @Test
    public void getReturnsNullIfParameterLArgerTahnSize() {
        test.add(42);
        assertEquals(null, test.get(1));
        assertEquals(null, test.get(42));
    }

    @Test
    public void getIReturnsIthElement() {
        test.add(42);
        test.add(101);
        test.add(-273);
        assertEquals(42, test.get(0));
        assertEquals(101, test.get(1));
        assertEquals(-273, test.get(2));
    }

    @Test
    public void getReturnsNullIfArrayIndexOutOfBounds() {
        test.add(42);
        test.add(101);
        test.add(-273);
        assertEquals(null, test.get(3));
    }

    @Test
    public void removeDoesNothingIfListDoesNotContainItsParameter() {
        assertFalse(test.remove(Integer.valueOf(42)));
        assertEquals(0, test.size());
    }

    @Test
    public void isEmptyTrueIfAllElementsAreRemoved() {
        test.add(42);
        test.remove(Integer.valueOf(42));
        assertTrue(test.isEmpty());
    }

    @Test
    public void listNoLongerContainsElementAfterItIsRemoved() {
        test.add(42);
        test.remove(Integer.valueOf(42));
        assertFalse(test.contains(42));
    }

    @Test
    public void removeReturnsTrueIfElementIsRemoved() {
        test.add(42);
        assertTrue(test.remove(Integer.valueOf(42)));
    }

    @Test
    public void sizeGoesDownWhenElementIsRemoved() {
        test.add(42);
        test.add(101);
        test.add(-273);
        assertEquals(3, test.size());
        test.remove(Integer.valueOf(101));
        assertEquals(2, test.size());
        test.remove(Integer.valueOf(42));
        assertEquals(1, test.size());
        test.remove(Integer.valueOf(-273));
        assertEquals(0, test.size());
    }

    @Test
    public void sizeIsZeroAfterClearing() {
        test.add(42);
        test.add(101);
        test.add(-273);
        test.clear();
        assertTrue(test.isEmpty());
        assertEquals(0, test.size());
    }

    @Test
    public void listCanContainOver16Objects() {
        for (int i = 0; i < 33; i++) {
            test.add(i);
        }
        assertEquals(33, test.size());
        for (int i = 0; i < 33; i++) {
            assertTrue(test.contains(i));
        }
    }

    @Test
    public void iteratorHasNoNextIfListEmpty() {
        Iterator<Integer> it = test.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    public void iteratorStartsFromFirstIndex() {
        test.add(42);
        test.add(101);
        test.add(-273);

        Iterator<Integer> it = test.iterator();
        assertEquals(42, (int) it.next());
    }

    @Test
    public void hasNextTrueUntilEndOfArray() {
        test.add(42);
        test.add(101);
        test.add(-273);

        Iterator<Integer> it = test.iterator();
        for (int i = 0; i < 3; i++) {
            assertTrue(it.hasNext());
            it.next();
        }
        assertFalse(it.hasNext());
    }

    @Test
    public void iteratorIteratesOverWholeArray() {
        for (int i = 0; i < 31; i++) {
            test.add(i);
        }

        int sum = 0;
        Iterator<Integer> it = test.iterator();

        while (it.hasNext()) {
            sum += it.next();
        }

        assertEquals(465, sum);
    }

    @Test
    public void removeIndexRemovesObjectAtGivenIndex() {
        test.add(42);
        test.add(101);
        test.add(-273);

        test.remove(2);
        assertEquals(null, test.get(2));
        test.remove(0);
        assertEquals(101, test.get(0));
    }

    @Test
    public void removeIndexReturnsRemovedObject() {
        test.add(42);
        test.add(101);
        test.add(-273);

        assertEquals(-273, test.remove(2));
        assertEquals(42, test.remove(0));
    }

    @Test
    public void removeIndexReturnsNullIfNoObjectAtGivenIndex() {
        assertEquals(null, test.remove(2));
    }

    @Test
    public void removeIndexDoesNotDecrementSizeWhenNothingIsRemoved() {
        test.remove(1);
        assertEquals(0, test.size());
    }

    @Test
    public void sizeGoesDownWhenObjectIsRemovedFromIndex() {
        test.add(42);
        test.add(101);
        test.add(-273);

        assertEquals(3, test.size());
        test.remove(2);
        assertEquals(2, test.size());
        test.remove(0);
        assertEquals(1, test.size());
    }

}
