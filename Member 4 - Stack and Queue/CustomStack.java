public class CustomStack<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> top;
    private int size;

    public CustomStack() {
        this.top = null;
        this.size = 0;
    }

    public void push(T item) {
        Node<T> newNode = new Node<>(item);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public T pop() {
        if (isEmpty()) {
            throw new EmptyStructureException("Cannot pop: audit stack is empty.");
        }
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    public T peek() {
        if (isEmpty()) {
            throw new EmptyStructureException("Cannot peek: audit stack is empty.");
        }
        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public int size() {
        return size;
    }

    public String printFromTop() {
        StringBuilder sb = new StringBuilder("[top] ");
        Node<T> current = top;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append("  ->  ");
            }
            current = current.next;
        }
        return sb.toString();
    }
}
