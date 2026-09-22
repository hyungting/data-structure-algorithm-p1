/**
 * A LIFO stack backed by a linked list. All operations are constant time
 * because pushes and pops happen at the head of the list.
 *
 * @param <T>
 *            the type of element held by the stack
 * @author CS 5040 course staff
 * @version Spring 2023
 */
public class Stack<T> {
    private LinkedList<T> elements;

    /**
     * Create an empty stack.
     */
    public Stack() {
        this.elements = new LinkedList<T>();
    }


    /**
     * Push an element onto the top of the stack.
     *
     * @param element
     *            the element to push
     */
    public void push(T element) {
        this.elements.addFirst(element);
    }


    /**
     * Remove and return the top element.
     *
     * @return the top element, or null if the stack is empty
     */
    public T pop() {
        LinkedNode<T> removed = this.elements.removeFirst();
        return removed == null ? null : removed.getData();
    }


    /**
     * Return the top element without removing it.
     *
     * @return the top element, or null if the stack is empty
     */
    public T peek() {
        if (this.isEmpty()) {
            return null;
        }

        return this.elements.getHead().getData();
    }


    /**
     * @return true if the stack holds no elements
     */
    public boolean isEmpty() {
        return this.getSize() == 0;
    }


    /**
     * @return the number of elements on the stack
     */
    public int getSize() {
        return this.elements.getSize();
    }
}
