/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.domain.datastructures;

/**
 * Node used for my linked list implementation.
 *
 * @author sami
 * @param <T> type of value saved in this node.
 */
public class Node<T extends Object> {

    private T value;
    private Node next;

    public Node(T value) {
        this.value = value;
        this.next = null;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

}
