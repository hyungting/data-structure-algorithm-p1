/**
 * A singly linked list with a head pointer, a tail pointer, and a cached size.
 *
 * Class invariants, which every public method must preserve:
 * 1. size is the number of reachable nodes starting at head.
 * 2. tail is the last reachable node, or null when the list is empty.
 * 3. tail.getNext() is null.
 *
 * @param <T>
 *            the type of data held in the list
 * @author CS 5040 course staff
 * @version Spring 2023
 */
public class LinkedList<T> {
    private LinkedNode<T> head;
    private LinkedNode<T> tail;
    private int size;

    /**
     * Create an empty list.
     */
    public LinkedList() {
        this(null);
    }


    /**
     * Create a list whose only node is the one given.
     *
     * @param head
     *            the initial (and only) node, or null for an empty list
     */
    public LinkedList(LinkedNode<T> head) {
        this.head = head;
        this.tail = head;
        this.size = head == null ? 0 : 1;
    }


    /**
     * @return the first node of the list, or null if empty
     */
    public LinkedNode<T> getHead() {
        return this.head;
    }


    /**
     * @return the last node of the list, or null if empty
     */
    public LinkedNode<T> getTail() {
        return this.tail;
    }


    /**
     * Append a node to the end of the list.
     *
     * @param node
     *            the node to append
     */
    public void add(LinkedNode<T> node) {
        if (this.head == null) {
            this.head = node;
        }
        else {
            this.tail.setNext(node);
        }

        this.tail = node;
        this.size++;
    }


    /**
     * Append a value to the end of the list.
     *
     * @param el
     *            the value to append
     */
    public void add(T el) {
        this.add(new LinkedNode<T>(el));
    }


    /**
     * Insert a value at the front of the list.
     *
     * @param el
     *            the value to insert
     */
    public void addFirst(T el) {
        LinkedNode<T> node = new LinkedNode<T>(el, this.head);
        this.head = node;
        if (this.tail == null) {
            this.tail = node;
        }

        this.size++;
    }


    /**
     * Remove and return the first node of the list.
     *
     * @return the removed node, or null if the list is empty
     */
    public LinkedNode<T> removeFirst() {
        if (this.head == null) {
            return null;
        }

        LinkedNode<T> removed = this.head;
        this.head = removed.getNext();
        if (this.head == null) {
            this.tail = null;
        }

        removed.setNext(null);
        this.size--;
        return removed;
    }


    /** TODO: remove while loop, add size adjustment
     * Discard every node after the given node, which must belong to this list.
     *
     * @param node
     *            the node that should become the last node of the list
     */
    public void truncateAfter(LinkedNode<T> node) {
        if (node == null) {
            return;
        }

        int removedSize = 0;
        LinkedNode<T> current = node.getNext();
        while (current != null) {
            removedSize++;
            current = current.getNext();
        }
        node.setNext(null);
        this.tail = node;
        this.size -= removedSize;

    }


    /**
     * Remove the node at the given index.
     *
     * @param index
     *            zero-based position of the node to remove
     * @return the removed node
     */
    public LinkedNode<T> remove(int index) {
        if (index < 0 || index >= this.size) {
            throw new IllegalArgumentException(
                "Index out of bounds. There are only " + this.size
                    + " elements in the list.");
        }

        int i = 0;
        LinkedNode<T> prev = null;
        LinkedNode<T> current = this.head;
        while (i < index) {
            prev = current;
            current = current.getNext();
            i++;
        }

        if (prev == null) {
            this.head = current.getNext();
        }
        else {
            prev.setNext(current.getNext());
        }

        if (current == this.tail) {
            this.tail = prev;
        }

        current.setNext(null);
        this.size--;
        return current;
    }


    /**
     * @param index
     *            zero-based position of the node to fetch
     * @return the node at that position
     */
    public LinkedNode<T> getItemAt(int index) {
        if (index < 0 || index >= this.size) {
            throw new IllegalArgumentException(
                "Index out of bounds. There are only " + this.size
                    + " elements in the list.");
        }

        int i = 0;
        LinkedNode<T> current = this.head;
        while (i < index) {
            current = current.getNext();
            i++;
        }

        return current;
    }


    /**
     * @return the number of nodes in the list
     */
    public int getSize() {
        return this.size;
    }


    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (!(other instanceof LinkedList)) {
            return false;
        }

        LinkedList<?> otherList = (LinkedList<?>)other;
        if (this.size != otherList.size) {
            return false;
        }

        LinkedNode<?> otherCurrent = otherList.getHead();
        LinkedNode<T> thisCurrent = this.head;
        while (otherCurrent != null && thisCurrent != null) {
            if (!thisCurrent.equals(otherCurrent)) {
                return false;
            }

            otherCurrent = otherCurrent.getNext();
            thisCurrent = thisCurrent.getNext();
        }

        return true;
    }


    @Override
    public int hashCode() {
        int result = 1;
        LinkedNode<T> current = this.head;
        while (current != null) {
            result = 31 * result + current.hashCode();
            current = current.getNext();
        }

        return result;
    }


    @Override
    public String toString() {
        StringBuilder rep = new StringBuilder(this.size);
        LinkedNode<T> current = this.head;
        while (current != null) {
            rep.append(current.getData());
            current = current.getNext();
        }

        return rep.toString();
    }
}
