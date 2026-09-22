/**
 * A single node in a singly linked list.
 *
 * @param <T>
 *            the type of data held by this node
 * @author CS 5040 course staff
 * @version Spring 2023
 */
public class LinkedNode<T> {
    private T data;
    private LinkedNode<T> next;

    /**
     * Create a node holding the given data and pointing at the given successor.
     *
     * @param data
     *            the value stored in this node
     * @param next
     *            the successor node, or null
     */
    public LinkedNode(T data, LinkedNode<T> next) {
        this.data = data;
        this.next = next;
        OpCounter.nodeAlloc();
    }


    /**
     * Create a node holding the given data with no successor.
     *
     * @param data
     *            the value stored in this node
     */
    public LinkedNode(T data) {
        this(data, null);
    }


    /**
     * @return the value stored in this node
     */
    public T getData() {
        return this.data;
    }


    /**
     * @param data
     *            the value to store in this node
     */
    public void setData(T data) {
        this.data = data;
    }


    /**
     * @return the successor node, or null if this is the last node
     */
    public LinkedNode<T> getNext() {
        return this.next;
    }


    /**
     * @param next
     *            the node that should follow this one
     */
    public void setNext(LinkedNode<T> next) {
        this.next = next;
    }


    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (!(other instanceof LinkedNode)) {
            return false;
        }

        return this.data.equals(((LinkedNode<?>)other).getData());
    }


    @Override
    public int hashCode() {
        return this.data == null ? 0 : this.data.hashCode();
    }


    @Override
    public String toString() {
        return this.data.toString();
    }
}
