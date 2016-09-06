package chess.domain.datastructures;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * My own take on Java's HashMap. LoadFactor and initial capacity set to Java's
 * standard 0.75 and 16. Ignores null keys and values. Uses function
 * f(o)=|o.hashcode()%capacity| for hashing purposes to hash generic data types.
 *
 * @author sami
 * @param <K> Class of keys.
 * @param <V> Class of values.
 */
public class MyHashMap<K extends Object, V extends Object> implements Map {

    private K[] keys;
    private V[] values;
    private MyLinkedList<Integer>[] indices;
    private MyLimitedStack<Integer> freedIndices;
    private int capacity;
    private int size;
    private final double loadFactor;

    public MyHashMap() {
        size = 0;
        capacity = 16;
        keys = (K[]) new Object[capacity];
        values = (V[]) new Object[capacity];
        freedIndices = new MyLimitedStack(capacity);
        indices = new MyLinkedList[capacity];
        initializeLinkedLists(indices);
        loadFactor = 0.75;
    }

    private void initializeLinkedLists(MyLinkedList[] linkedListTable) {
        for (int i = 0; i < capacity; i++) {
            linkedListTable[i] = new MyLinkedList();
        }
    }

    /**
     * Returns amount of Key-Value-pairs saved in this HashMap.
     *
     * @return size of this HashMap.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns true if this map contains no Key-Value-pairs, otherwise false.
     *
     * @return true if this map is empty, otherwise false.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns true if map contains object o, otherwise false. Searches for o by
     * using its hashcode to find correct bucket and then goes through contents
     * of that bucket looking for match.
     *
     * @param o object to be searched for.
     * @return true if map contains object o as key, otherwise false.
     */
    @Override
    public boolean containsKey(Object o) {
        return indices[Math.abs(o.hashCode() % capacity)].stream()
                .anyMatch((index) -> keys[index].equals(o));
    }

    /**
     * Searches for o by going through values one by one until one value equals
     * o and returns true. If none of values equal o, false is returned instead.
     *
     * @param o object to be searched for.
     * @return true if map contains o as value, otherwise false.
     */
    @Override
    public boolean containsValue(Object o) {
        for (int i = 0; i < size; i++) {
            if (values[i] != null && values[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Searches for value associated with key o by hashing with o's hash code
     * mod capacity. Searches through bucket indicated by hash for index of key
     * that equals o. If such is found returns value in the same index in value
     * array, otherwise returns null.
     *
     * @param o of which corresponding value is searched for.
     * @return value associated with key o, if no value is associated with o
     * then null.
     */
    @Override
    public Object get(Object o) {
        int hash = Math.abs(o.hashCode() % capacity);

        for (Integer index : indices[hash]) {
            if (keys[index].equals(o)) {
                return values[index];
            }
        }
        return null;
    }

    /**
     * Associates given key with given value in this map. First ensures that
     * there's enough memory space for key-Value pair and then searches if key
     * was already associated with a value saving old index of key in array keys
     * in variable oldIndex.
     *
     * If key was already included in array saves old value associated with key
     * to variable oldValue and replaces associated value with new value.
     *
     * If key wasn't included in map checks if any previously used indices have
     * been freed for use. If index has been freed, saves key and value to given
     * index in arrays keys and values and then saves that index to bucket
     * indicated by key's hash. Otherwise does the same for first free index.
     * And finally increases size by one.
     *
     * @param key key which will be associated with value.
     * @param value value that key will be associated with.
     * @return previous value that key was associated with if such exists,
     * otherwise null.
     */
    @Override
    public Object put(Object key, Object value) {
        ensureCapacity();
        int hash = Math.abs(key.hashCode() % capacity);
        int oldIndex = findOldIndex(hash, key);
        int newIndex = size;
        Object oldValue = null;

        if (oldIndex != -1) {
            oldValue = values[oldIndex];
            values[oldIndex] = (V) value;
        } else {
            if (!freedIndices.isEmpty()) {
                newIndex = freedIndices.pop();
            }
            keys[newIndex] = (K) key;
            values[newIndex] = (V) value;
            indices[hash].add(newIndex);
            size++;
        }
        return oldValue;
    }

    private int findOldIndex(int hash, Object key) {
        int oldIndex = -1;
        for (Integer index : indices[hash]) {
            if (keys[index].equals(key)) {
                oldIndex = index;
                break;
            }
        }
        return oldIndex;
    }

    private void ensureCapacity() {
        if (size >= loadFactor * capacity) {
            capacity *= 2;
            K[] newKeys = (K[]) new Object[capacity];
            V[] newValues = (V[]) new Object[capacity];
            MyLinkedList<Integer>[] newIndices = new MyLinkedList[capacity];
            initializeLinkedLists(newIndices);

            rehashToNewIndices(newKeys, newValues, newIndices);

            freedIndices = new MyLimitedStack(capacity);
            indices = newIndices;
            keys = newKeys;
            values = newValues;
        }
    }

    private void rehashToNewIndices(K[] newKeys, V[] newValues, MyLinkedList<Integer>[] newIndices) {
        for (int i = 0; i < size; i++) {
            newKeys[i] = keys[i];
            newValues[i] = values[i];
            newIndices[Math.abs(keys[i].hashCode() % capacity)].add(i);
        }
    }

    /**
     * Searches through bucket indicated by o's hash for index of key that
     * equals o. If such is found saves associated value in ret and sets key and
     * value to null before removing index from bucket. Also pushes freed index
     * to freedIndices for further use and decreases size.
     *
     * @param o key of the key-value-pair being removed.
     * @return returns the value associated with removed key.
     */
    @Override
    public Object remove(Object o) {
        int hash = Math.abs(o.hashCode() % capacity);
        V ret = null;

        for (Integer index : indices[hash]) {
            if (keys[index].equals(o)) {
                ret = values[index];
                keys[index] = null;
                values[index] = null;
                indices[hash].remove(index);

                size--;
                freedIndices.push(index);
                return ret;
            }
        }

        return ret;
    }

    @Override
    public void putAll(Map map) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Clears HashMap of all saved values and keys and sets capacity back to 16.
     */
    @Override
    public void clear() {
        capacity = 16;
        size = 0;
        freedIndices = new MyLimitedStack(capacity);
        indices = new MyLinkedList[capacity];
        initializeLinkedLists(indices);
        keys = (K[]) new Object[capacity];
        values = (V[]) new Object[capacity];
    }

    /**
     * Returns a set containing all keys in this map excluding nulls.
     *
     * @return set containing all keys of this map.
     */
    @Override
    public Set keySet() {
        Set<K> ks = new MyHashSet();
        for (int i = 0; i < size; i++) {
            if (keys[i] != null) {
                ks.add(keys[i]);
            }
        }
        return ks;
    }

    /**
     * Returns collection containing all values from this map except for nulls.
     *
     * @return collection containing all values in this map.
     */
    @Override
    public Collection values() {
        Collection<V> vals = new MyHashSet();
        for (int i = 0; i < size; i++) {
            if (values[i] != null) {
                vals.add(values[i]);
            }
        }
        return vals;
    }

    @Override
    public Set entrySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
