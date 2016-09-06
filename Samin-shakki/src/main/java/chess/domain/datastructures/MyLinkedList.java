/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.domain.datastructures;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * My own implementation of linked list. Includes size and last node as
 * attributes for faster modification and usage. Only linked in one direction.
 *
 * @author sami
 * @param <T> type of values saved in this linked list.
 */
public class MyLinkedList<T extends Object> implements List<T> {

    private Node<T> first;
    private Node<T> last;
    private int size;

    public MyLinkedList() {
        size = 0;
    }

    /**
     * Returns the amount of values saved in this linked list.
     *
     * @return size of this linked list.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Checks if this list is empty.
     *
     * @return true if list is empty, otherwise false.
     */
    @Override
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Checks if this list contains object o by going through nodes one by one.
     *
     * @param o object being searched for.
     * @return true if list contains object o, otherwise false.
     */
    @Override
    public boolean contains(Object o) {
        Node cur = first;
        while (cur != null) {
            if (cur.getValue().equals(o)) {
                return true;
            }
            cur = cur.getNext();
        }
        return false;
    }

    /**
     * Returns iterator over this linked list. Iterator will go through nodes
     * one by one until it finds null.
     *
     * @return iterator that will go over this list node by node.
     */
    @Override
    public Iterator<T> iterator() {
        Iterator<T> ret = new Iterator<T>() {
            private Node<T> cur = first;

            @Override
            public boolean hasNext() {
                return cur != null;
            }

            @Override
            public T next() {
                T curVal = cur.getValue();
                cur = cur.getNext();
                return curVal;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };

        return ret;
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
     * Adds given element to this list. If list is empty, sets new node with
     * value e as first and last. If list isn't empty searches for last node and
     * adds node with value e as next of last and then makes last point to new
     * last node. In either case increases size by one.
     *
     * @param e element being added to this list.
     * @return true if object was added
     */
    @Override
    public boolean add(T e) {
        if (isEmpty()) {
            first = new Node(e);
            last = first;
        } else {
            last.setNext(new Node(e));
            last = last.getNext();
        }
        size++;
        return true;
    }

    /**
     * Removes given object from this list. Checks first manually that first
     * node exists and whether it's value equals o or not. If there was no
     * match, loops over rest of linked list until it finds value equal to o or
     * meets null.
     *
     * If value equal to null is found makes previous node point to current
     * node's next pointer effectively removing current node. In case match was
     * in first node, makes field first point to current node's next node. In
     * either case decreases size by one in the end.
     *
     * @param o object being removed.
     * @return true if object was removed, otherwise false.
     */
    @Override
    public boolean remove(Object o) {
        if (isEmpty()) {
            return false;
        }

        if (first.getValue().equals(o)) {
            first = first.getNext();
            size--;
            return true;
        }

        Node cur = first.getNext();
        Node prev = first;

        while (cur != null) {
            if (cur.getValue().equals(o)) {
                prev.setNext(cur.getNext());
                size--;
                return true;
            }
            prev = cur;
            cur = cur.getNext();
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addAll(Collection<? extends T> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean removeAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean retainAll(Collection<?> clctn) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Clears this linked list of all nodes and sets size to 0;
     */
    @Override
    public void clear() {
        first = null;
        last = first;
        size = 0;
    }

    @Override
    public T get(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T set(int i, T e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void add(int i, T e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T remove(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
    public ListIterator<T> listIterator(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<T> subList(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
