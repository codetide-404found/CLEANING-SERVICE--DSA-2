import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A generic doubly linked list implemented from scratch
 * (no java.util.LinkedList used internally), with a custom iterator.
 *
 * <p>In this project, used to represent a single cleaning crew's ordered
 * route for the day — stops can be appended, inserted mid-route for a
 * rush job, or removed for a cancelled visit, all without shifting the
 * rest of the route.</p>
 */
public class MyLinkedList<T> implements Iterable<T> {

    private static class Node<T> {
        T value;
        Node<T> prev;
        Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /** Inserts value at the front of the list. */
    public void addFirst(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
    }

    /** Inserts value at the end of the list. */
    public void addLast(T value) {
        Node<T> node = new Node<>(value);
        if (tail == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    /** Inserts value immediately after the first node whose value equals target. */
    public void insertAfter(T target, T value) {
        Node<T> current = findNode(target);
        if (current == null) {
            throw new NoSuchElementException("Target value not found: " + target);
        }
        Node<T> node = new Node<>(value);
        node.prev = current;
        node.next = current.next;
        if (current.next != null) {
            current.next.prev = node;
        } else {
            tail = node;
        }
        current.next = node;
        size++;
    }

    /** Removes the first node whose value equals the given value. Returns true if removed. */
    public boolean remove(T value) {
        Node<T> current = findNode(value);
        if (current == null) {
            return false;
        }
        if (current.prev != null) {
            current.prev.next = current.next;
        } else {
            head = current.next;
        }
        if (current.next != null) {
            current.next.prev = current.prev;
        } else {
            tail = current.prev;
        }
        size--;
        return true;
    }

    private Node<T> findNode(T value) {
        Node<T> current = head;
        while (current != null) {
            boolean matches = (current.value == null) ? (value == null) : current.value.equals(value);
            if (matches) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyLinkedListIterator();
    }

    /** Custom iterator: only the java.util.Iterator interface is reused, not any built-in implementation. */
    private class MyLinkedListIterator implements Iterator<T> {
        private Node<T> cursor = head;

        @Override
        public boolean hasNext() {
            return cursor != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements in the list");
            }
            T value = cursor.value;
            cursor = cursor.next;
            return value;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.value);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        return sb.append("]").toString();
    }
}
