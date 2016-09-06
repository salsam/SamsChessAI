package chess.domain.datastructures;

import java.util.Iterator;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sami
 */
public class MyHashSetTest {

    private MyHashSet mhs;

    public MyHashSetTest() {
    }

    @Before
    public void setUp() {
        mhs = new MyHashSet();
    }

    @Test
    public void isEmptyTrueAfterInitialization() {
        assertTrue(mhs.isEmpty());
    }

    @Test
    public void sizeIsZeroAfterInitialization() {
        assertEquals(0, mhs.size());
    }

    @Test
    public void containsFalseWhenSetIsEmpty() {
        assertFalse(mhs.contains(42));
    }

    @Test
    public void isEmptyTrueAfterAddingAnElement() {
        mhs.add(42);
        assertFalse(mhs.isEmpty());
    }

    @Test
    public void sizeGoesUpByOneWhenAddingAnElement() {
        mhs.add(42);
        assertEquals(1, mhs.size());
        mhs.add(101);
        assertEquals(2, mhs.size());
        mhs.add(-273);
        assertEquals(3, mhs.size());
        mhs.add(0);
        assertEquals(4, mhs.size());
    }

    @Test
    public void containsFalseIfElementNotAdded() {
        mhs.add(42);
        mhs.add(101);
        mhs.add(-273);
        mhs.add(0);
        assertFalse(mhs.contains(314));
    }

    @Test
    public void containsTrueIfElementHasBeenAddedToSet() {
        mhs.add(42);
        mhs.add(101);
        mhs.add(-273);
        mhs.add(0);
        assertTrue(mhs.contains(0));
        assertTrue(mhs.contains(42));
    }

    @Test
    public void addReturnsTrueWhenElementIsAdded() {
        assertTrue(mhs.add(42));
    }

    @Test
    public void addingSameElementTwiceDoesNotIncreaseSize() {
        mhs.add(42);
        mhs.add(42);
        assertEquals(1, mhs.size());
    }

    @Test
    public void addReturnsFalseIfElementIsAlreadyAdded() {
        mhs.add(42);
        assertFalse(mhs.add(42));
    }

    @Test
    public void sizeGoesDownWhenElementIsRemoved() {
        mhs.add(42);
        mhs.add(101);
        mhs.add(-273);
        mhs.add(0);

        mhs.remove(101);
        assertEquals(3, mhs.size());

        mhs.remove(42);
        assertEquals(2, mhs.size());

        mhs.remove(-273);
        assertEquals(1, mhs.size());

        mhs.remove(0);
        assertEquals(0, mhs.size());
    }

    @Test
    public void sizeIsUnaffectedIfNoElementIsRemoved() {
        mhs.add(42);
        mhs.add(101);
        mhs.add(-273);
        mhs.add(0);

        mhs.remove(666);
        assertEquals(4, mhs.size());
    }

    @Test
    public void setIsEmptyIfLastElementIsRemoved() {
        mhs.add(42);
        mhs.remove(42);
        assertTrue(mhs.isEmpty());
    }

    @Test
    public void setEmptyAndSizeZeroAfterBeingCleared() {
        mhs.add(42);
        mhs.add(101);
        mhs.add(-273);
        mhs.add(0);

        mhs.clear();
        assertTrue(mhs.isEmpty());
        assertEquals(0, mhs.size());
    }

    @Test
    public void containsFalseAfterClearing() {
        mhs.add(42);
        mhs.add(101);
        mhs.add(-273);
        mhs.add(0);

        mhs.clear();
        assertFalse(mhs.contains(42));
    }

    @Test
    public void setCanContainOver16Objects() {
        for (int i = 0; i < 17; i++) {
            mhs.add(i);
        }

        assertEquals(17, mhs.size());

        for (int i = 0; i < 17; i++) {
            assertTrue(mhs.contains(i));
        }
    }

    @Test
    public void iteratorDoesNotHaveNextIfSetEmpty() {
        Iterator<Integer> it = mhs.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    public void iteratorStartsFromFirstBucket() {
        mhs.add(42);
        mhs.add(101);
        mhs.add(-273);
        mhs.add(0);

        Iterator<Integer> it = mhs.iterator();
        assertEquals(0, (int) it.next());
    }

    @Test
    public void iteratorHasNextUntilEndOfSet() {
        mhs.add(42);
        mhs.add(101);
        mhs.add(-273);
        mhs.add(0);

        Iterator<Integer> it = mhs.iterator();

        for (int i = 0; i < 4; i++) {
            assertTrue(it.hasNext());
            it.next();
        }
        assertFalse(it.hasNext());
    }

    @Test
    public void iteratorIteratesOverWholeSet() {
        for (int i = 0; i < 31; i++) {
            mhs.add(i);
        }

        int sum = 0;
        Iterator<Integer> it = mhs.iterator();

        while (it.hasNext()) {
            sum += it.next();
        }

        assertEquals(465, sum);
    }

    @Test
    public void addAllAddsAllElementsToSet() {
        MyArrayList<Integer> mal = new MyArrayList();
        mal.add(42);
        mal.add(101);
        mal.add(-273);
        mal.add(0);

        mhs.addAll(mal);
        assertTrue(mhs.contains(42));
        assertTrue(mhs.contains(101));
        assertTrue(mhs.contains(-273));
        assertTrue(mhs.contains(0));
    }

    @Test
    public void addAllCanAddOver32Elements() {
        MyArrayList<Integer> mal = new MyArrayList();
        for (int i = 0; i < 33; i++) {
            mal.add(i);
        }

        mhs.addAll(mal);

        for (int i = 0; i < 33; i++) {
            assertTrue(mhs.contains(i));
        }
    }
}
