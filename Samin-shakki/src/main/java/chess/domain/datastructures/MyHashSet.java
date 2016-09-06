/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.domain.datastructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * My own implementation of Java's HashSet. LoadFactor of 0.75 is used as it's
 * Java's standard and generally offers good balance between efficiency and
 * memory usage. InitialCapacity set to 16 for for Java standard. Uses function
 * f(o)=|o.hashcode()%capacity| for hashing purposes to hash generic data types.
 *
 * @author sami
 * @param <T> type of object being stored in this set.
 */
public class MyHashSet<T extends Object> implements Set<T> {

    private final double loadFactor;
    private int capacity;
    private int size;
    private MyLinkedList<T>[] buckets;

    public MyHashSet() {
        loadFactor = 0.75;
        capacity = 16;
        size = 0;
        buckets = new MyLinkedList[capacity];
        initializeLinkedLists(buckets);
    }

    /**
     * Returns amount of values saved in this set.
     *
     * @return size of this set.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns true if this set is empty, otherwise false.
     *
     * @return true if set is empty, otherwise false.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Checks whether or not set contains given object o by going through bucket
     * indicated by o's hash.
     *
     * @param o object being searched for.
     * @return true if set contains o, otherwise false.
     */
    @Override
    public boolean contains(Object o) {
        return buckets[Math.abs(o.hashCode() % capacity)].contains(o);
    }

    /**
     * Returns iterator over this set that will go through all the buckets one
     * by one.
     *
     * @return iterator over this set.
     */
    @Override
    public Iterator<T> iterator() {
        Iterator<T> it = new Iterator<T>() {
            private int soFar = 0;
            private int bucket = 0;
            private Iterator<T> listIt = buckets[0].iterator();

            @Override
            public boolean hasNext() {
                return soFar < size;
            }

            @Override
            public T next() {
                soFar++;

                while (!listIt.hasNext()) {
                    bucket++;
                    listIt = buckets[bucket].iterator();
                }

                return listIt.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };

        return it;
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Adds given object to set. First makes sure that there's enough memory for
     * adding new value and doubles available memory if necessary. Then checks
     * if set already contains e, if not then adds e to bucket indicated by
     * hashing function and increments size by one.
     *
     * @param e object being added to set.
     * @return true if new object was added, otherwise false.
     */
    @Override
    public boolean add(T e) {
        ensureCapacity();
        int hash = Math.abs(e.hashCode() % capacity);
        if (buckets[hash].contains(e)) {
            return false;
        }
        buckets[hash].add(e);
        size++;
        return true;
    }

    private void ensureCapacity() {
        if (size >= loadFactor * capacity) {
            capacity *= 2;
            rehashValues(capacity / 2);
        }
    }

    private void initializeLinkedLists(MyLinkedList[] linkedListTable) {
        for (int i = 0; i < capacity; i++) {
            linkedListTable[i] = new MyLinkedList();
        }
    }

    /**
     * Removes the given object from set. Tries to remove object o from bucket
     * indicated by its hash and if it was removed decrements size by one.
     *
     * @param o object being removed.
     * @return true if object was removed, otherwise false.
     */
    @Override
    public boolean remove(Object o) {
        if (buckets[Math.abs(o.hashCode() % capacity)].remove(o)) {
            size--;
            return true;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Adds contents of given collection to this set. Speeds up process by
     * ensuring there's enough memory reserved beforehand and then rehashes old
     * values only once. After that adds all values one by one to set.
     *
     * @param clctn collection of which objects are being added to this set.
     * @return true if at least one object was added to set, otherwise false.
     */
    @Override
    public boolean addAll(Collection<? extends T> clctn) {
        boolean reservedMemory = false;
        boolean valueWasAdded = false;
        int oldCapacity = capacity;
        while (loadFactor * capacity < size + clctn.size()) {
            capacity *= 2;
            reservedMemory = true;
        }

        if (reservedMemory) {
            rehashValues(oldCapacity);
        }

        for (T t : clctn) {
            if (add(t)) {
                valueWasAdded = true;
            }
        }

        return valueWasAdded;
    }

    private void rehashValues(int oldCapacity) {
        MyLinkedList[] newBuckets = new MyLinkedList[capacity];
        initializeLinkedLists(newBuckets);

        for (int i = 0; i < oldCapacity; i++) {
            for (T element : buckets[i]) {
                newBuckets[Math.abs(element.hashCode() % capacity)].add(element);
            }
        }

        buckets = newBuckets;
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Clears this set of all values and sets capacity back to 16.
     */
    @Override
    public void clear() {
        capacity = 16;
        size = 0;
        buckets = new MyLinkedList[capacity];
        initializeLinkedLists(buckets);
    }

}
