package chess.domain.datastructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * My own implementation of Java's ArrayList. Initial capacity set to Java
 * standard 16.
 *
 * @author sami
 * @param <T> Type of object being stored in this list.
 */
public class MyArrayList<T extends Object> implements List<T> {

    private T[] array;
    private int capacity;
    private int size;

    public MyArrayList() {
        this.capacity = 16;
        this.size = 0;
        this.array = (T[]) new Object[capacity];
    }

    /**
     * Returns size of ArrayList AKA how many objects are stored in this list.
     *
     * @return size of this ArrayList
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Returns true if list is empty, otherwise false.
     *
     * @return true if list is empty.
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns true if list contains object o, otherwise false.
     *
     * @param o object being searched for in this list.
     * @return true if list contains object o.
     */
    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns iterator over this ArrayList.
     *
     * @return iterator over this ArrayList.
     */
    @Override
    public Iterator<T> iterator() {
        Iterator<T> it = new Iterator<T>() {
            private int cur = 0;

            @Override
            public boolean hasNext() {
                return cur < size;
            }

            @Override
            public T next() {
                return array[cur++];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }

    /**
     * Returns array containing contents of this list.
     *
     * @return array containing contents of this list.
     */
    @Override
    public Object[] toArray() {
        return array;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * First ensures this list has enough memory left to add a value, if not
     * then doubles capacity and reallocates contents to new array of size
     * capacity. Then adds object e to first open slot in array and increases
     * size by one.
     *
     * @param e object of type T to be added.
     * @return true if object was added to list.
     */
    @Override
    public boolean add(T e) {
        ensureCapacity();
        array[size] = e;
        size++;
        return true;
    }

    private void ensureCapacity() {
        if (size == capacity) {
            capacity *= 2;
            T[] newArray = (T[]) new Object[capacity];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
    }

    /**
     * Removes first occurrence of given object in the list. Searches list index
     * by index until it finds equal element and then moves all following
     * objects one spot towards the beginning of the list.
     *
     * @param o object to be removed.
     * @return true if list contained o, otherwise false.
     */
    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(o)) {
                for (int j = i; j < size - 1; j++) {
                    array[j] = array[j + 1];
                }
                size--;
                array[size] = null;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Clears the list of all objects and returns it's capacity back to 16 and
     * size to 0.
     */
    @Override
    public void clear() {
        size = 0;
        capacity = 16;
        array = (T[]) new Object[capacity];
    }

    @Override
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        return array[index];
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Removes the object in given position on the list by moving all following
     * object one index towards start of the list.
     *
     * @param index index of object to be removed.
     * @return removed object.
     */
    @Override
    public T remove(int index) {
        T removed = array[index];

        if (index >= size) {
            return removed;
        }
        
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }

        size--;
        array[size] = null;

        return removed;
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
