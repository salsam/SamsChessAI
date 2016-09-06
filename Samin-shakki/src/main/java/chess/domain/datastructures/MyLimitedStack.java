package chess.domain.datastructures;

/**
 * My implementation of stack for use in HashMap. This version of stack has only
 * limited capacity as there was no need for more complex implementation.
 *
 * @author sami
 * @param <T> type of objects being stored in this stack.
 */
public class MyLimitedStack<T extends Object> {

    private T[] array;
    private int head;

    public MyLimitedStack(int capacity) {
        array = (T[]) new Object[capacity];
        head = -1;
    }

    /**
     * Checks if this stack is empty.
     *
     * @return true if stack is empty, otherwise false.
     */
    public boolean isEmpty() {
        return head == -1;
    }

    /**
     * Returns the top object of this stack if stack isn't empty. Returns null
     * if stack is empty.
     *
     * @return object at top of this stack, null if stack if empty.
     */
    public T pop() {
        if (head == -1) {
            return null;
        }
        return array[head--];
    }

    /**
     * Pushes given element to top of this stack and increases head by one. If
     * this stack is full throws IndexoutOfBoundException warning that stack's
     * capacity has been surpassed.
     *
     * @param element object pushed as the uppermost object in this stack.
     */
    public void push(T element) {
        if (head >= array.length) {
            throw new IndexOutOfBoundsException("You have passed capacity of this stack.");
        }
        array[++head] = element;
    }
}
