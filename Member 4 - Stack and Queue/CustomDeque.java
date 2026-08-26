public class CustomDeque<T> {

    private static class Node<T> {
        T data;
        Node<T> prev;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> front;
    private Node<T> rear;
    private int size;

    public CustomDeque() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    public void addFront(T item) {
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            newNode.next = front;
            front.prev = newNode;
            front = newNode;
        }
        size++;
    }

    public void addRear(T item) {
        Node<T> newNode = new Node<>(item);
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            newNode.prev = rear;
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public T removeFront() {
        if (isEmpty()) {
            throw new EmptyStructureException("Cannot removeFront: deque is empty.");
        }
        T data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        } else {
            front.prev = null;
        }
        size--;
        return data;
    }

    public T removeRear() {
        if (isEmpty()) {
            throw new EmptyStructureException("Cannot removeRear: deque is empty.");
        }
        T data = rear.data;
        rear = rear.prev;
        if (rear == null) {
            front = null;
        } else {
            rear.next = null;
        }
        size--;
        return data;
    }

    public T peekFront() {
        if (isEmpty()) {
            throw new EmptyStructureException("Cannot peek: deque is empty.");
        }
        return front.data;
    }

    public T peekRear() {
        if (isEmpty()) {
            throw new EmptyStructureException("Cannot peek: deque is empty.");
        }
        return rear.data;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int size() {
        return size;
    }

    public String printFrontToRear() {
        StringBuilder sb = new StringBuilder("front [ ");
        Node<T> current = front;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(" , ");
            }
            current = current.next;
        }
        sb.append(" ] rear");
        return sb.toString();
    }
}
