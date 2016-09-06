package chess.domain.datastructures;

import java.util.Collection;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sami
 */
public class MyHashMapTest {

    private MyHashMap mhm;

    public MyHashMapTest() {
    }

    @Before
    public void setUp() {
        mhm = new MyHashMap();
    }

    @Test
    public void hashMapEmptyAndSizeZeroAfterInitialization() {
        assertTrue(mhm.isEmpty());
        assertEquals(0, mhm.size());
    }

    @Test
    public void containsKeyFalseWhenEmpty() {
        assertFalse(mhm.containsKey(1));
    }

    @Test
    public void containsValueFalseWhenEmpty() {
        assertFalse(mhm.containsValue(1));
    }

    @Test
    public void sizeGoesUpWhenAddingKeyValuePairsToMap() {
        mhm.put(1, 2);
        assertEquals(1, mhm.size());

        mhm.put(2, 3);
        assertEquals(2, mhm.size());

        mhm.put(3, 4);
        assertEquals(3, mhm.size());
    }

    @Test
    public void sizeDoesNotGoUpWhenChangingValueOfExistingKey() {
        mhm.put(1, 2);
        mhm.put(1, 3);
        assertEquals(1, mhm.size());
    }

    @Test
    public void containsKeyFalseIfMapDoesNotContainsKey() {
        mhm.put(1, 2);
        mhm.put(2, 3);
        mhm.put(3, 4);

        assertFalse(mhm.containsKey(4));
    }

    @Test
    public void containsKeyTrueIfKeyHasBeenAddedToMap() {
        mhm.put(1, 2);
        mhm.put(2, 3);
        mhm.put(3, 4);

        assertTrue(mhm.containsKey(1));
        assertTrue(mhm.containsKey(2));
        assertTrue(mhm.containsKey(3));
    }

    @Test
    public void containsValueFalseIfMapDoesNotContainValue() {
        mhm.put(1, 2);
        mhm.put(2, 3);
        mhm.put(3, 4);

        assertFalse(mhm.containsValue(1));
    }

    @Test
    public void containsValueTrueIfValueHasBeenAddedToMap() {
        mhm.put(1, 2);
        mhm.put(2, 3);
        mhm.put(3, 4);

        assertTrue(mhm.containsValue(4));
        assertTrue(mhm.containsValue(2));
        assertTrue(mhm.containsValue(3));
    }

    @Test
    public void containsValueFalseIfAssociatedKeyMappedToNewValue() {
        mhm.put(1, 2);
        mhm.put(1, 42);
        assertFalse(mhm.containsValue(2));
    }

    @Test
    public void getReturnsNullIfNoValueIfMapDoesNotContainGivenKey() {
        assertEquals(null, mhm.get(42));
    }

    @Test
    public void getReturnsValueAssociatedWithGivenKey() {
        mhm.put(1, 2);
        mhm.put(2, -273);
        mhm.put(-273, 42);

        assertEquals(2, mhm.get(1));
        assertEquals(-273, mhm.get(2));
        assertEquals(42, mhm.get(-273));
    }

    @Test
    public void removeRemovesNothingAndReturnsNullIfNoneOfKeysMatchParameter() {
        mhm.put(1, 2);
        mhm.put(314, -273);
        mhm.put(-273, 42);

        assertEquals(null, mhm.remove(2));
        assertEquals(3, mhm.size());
    }

    @Test
    public void removeRemovesKeyValuePairMatchingItsParameter() {
        mhm.put(1, 2);
        mhm.put(314, -273);
        mhm.put(-273, 42);

        mhm.remove(314);
        assertFalse(mhm.containsKey(314));
    }

    @Test
    public void removingKeyValuePairsDecresesSize() {
        mhm.put(1, 2);
        mhm.put(314, -273);
        mhm.put(-273, 42);

        mhm.remove(314);
        assertEquals(2, mhm.size());

        mhm.remove(1);
        assertEquals(1, mhm.size());

        mhm.remove(-273);
        assertEquals(0, mhm.size());
    }

    @Test
    public void ifObjectIsRemovedRemoveReturnsItsAssociatedValue() {
        mhm.put(1, 2);
        mhm.put(314, -273);
        mhm.put(-273, 42);

        assertEquals(-273, mhm.remove(314));
    }

    @Test
    public void ifLastPairIsRemovedMapIsEmpty() {
        mhm.put(1, 2);
        mhm.remove(1);
        assertTrue(mhm.isEmpty());
    }

    @Test
    public void mapEmptyAndSizeZeroAfterClearing() {
        mhm.put(1, 2);
        mhm.put(314, -273);
        mhm.put(-273, 42);

        mhm.clear();
        assertTrue(mhm.isEmpty());
        assertEquals(0, mhm.size());
    }

    @Test
    public void containsKeyFalseAfterClearing() {
        mhm.put(1, 2);
        mhm.put(314, -273);
        mhm.put(-273, 42);

        mhm.clear();
        assertFalse(mhm.containsKey(1));
        assertFalse(mhm.containsKey(314));
        assertFalse(mhm.containsKey(-273));
    }

    @Test
    public void containsValueFalseAfterClearing() {
        mhm.put(1, 2);
        mhm.put(314, -273);
        mhm.put(-273, 42);

        mhm.clear();
        assertFalse(mhm.containsValue(2));
        assertFalse(mhm.containsValue(42));
        assertFalse(mhm.containsValue(-273));
    }

    @Test
    public void keysetEmptyButNotNullIfMApEmpty() {
        Set<Integer> s = mhm.keySet();
        assertNotEquals(null, s);
        assertTrue(s.isEmpty());
    }

    @Test
    public void keysetContainsAllKeysFromMap() {
        mhm.put(1, 2);
        mhm.put(314, -273);
        mhm.put(-273, 42);

        Set<Integer> s = mhm.keySet();

        assertTrue(s.contains(1));
        assertTrue(s.contains(314));
        assertTrue(s.contains(-273));
    }

    @Test
    public void keySetDoesNotContainNulls() {
        mhm.put(1, 2);
        mhm.put(314, -273);
        mhm.put(-273, 42);
        mhm.remove(1);
        mhm.remove(-273);

        Set<Integer> s = mhm.keySet();
        s.stream().forEach(i -> {
            assertFalse(i == null);
        });
    }

    @Test
    public void keysetDoesNotContainRemovedKeys() {
        mhm.put(1, 2);
        mhm.put(314, -273);
        mhm.put(-273, 42);
        mhm.remove(1);
        mhm.remove(-273);

        Set<Integer> s = mhm.keySet();
        assertFalse(s.contains(1));
        assertFalse(s.contains(-273));
    }

    @Test
    public void valuesEmptyButNotNullIfMapEmpty() {
        Collection<Integer> values = mhm.values();
        assertNotEquals(null, values);
        assertTrue(values.isEmpty());
    }

    @Test
    public void valuesContainsAllValuesFromMap() {
        mhm.put(1, 2);
        mhm.put(314, -273);
        mhm.put(-273, 42);

        Collection<Integer> values = mhm.values();

        assertTrue(values.contains(2));
        assertTrue(values.contains(42));
        assertTrue(values.contains(-273));
    }

    @Test
    public void valuesDoesNotContainRemovedValues() {
        mhm.put(1, 2);
        mhm.put(314, -273);
        mhm.put(-273, 42);
        mhm.remove(1);
        mhm.remove(-273);

        Collection<Integer> values = mhm.values();
        assertFalse(values.contains(2));
        assertFalse(values.contains(42));
    }

    @Test
    public void valuesDoesNotContainNulls() {
        mhm.put(1, 2);
        mhm.put(314, -273);
        mhm.put(-273, 42);
        mhm.remove(1);
        mhm.remove(-273);

        Collection<Integer> values = mhm.values();
        values.stream().forEach(v -> {
            assertFalse(v == null);
        });
    }

    @Test
    public void youCanPutAndRemoveMappingsMultipleTimes() {
        for (int i = 0; i < 100; i++) {
            mhm.put(i, 2 * i);
            if (i % 5 == 0) {
                mhm.remove(i / 5);
            }
        }

        for (int i = 30; i < 70; i++) {
            mhm.remove(i);
            if (i % 7 == 0) {
                mhm.put(i / 7, 3 * i);
            }
        }

        for (int i = 100; i < 140; i++) {
            mhm.put(i, 2 * i);
            if (i % 5 == 0) {
                mhm.remove(i / 5);
            }
        }

        for (int i = 0; i < 140; i++) {
            if (i >= 28 && (i < 30 || i >= 70)) {
                if (!mhm.containsKey(i)) {
                    System.out.println(i);
                }
                assertTrue(mhm.containsKey(i));
            }
        }
    }

}
