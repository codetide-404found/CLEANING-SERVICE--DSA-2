public class CircularQueue<T> {

    private final Object[] items;
    private final int capacity;
    private int front;
    private int rear;
    private int size;

    public CircularQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }
        this.capacity = capacity;
        this.items = new Object[capacity];
        this.front = 0;
        this.rear = 0;
        this.size = 0;
    }

    public void enqueue(T item) {
        if (isFull()) {
            throw new StructureFullException(
                    "Cannot enqueue: circular queue is full (capacity = " + capacity + ").");
        }
        items[rear] = item;
        rear = (rear + 1) % capacity;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new EmptyStructureException("Cannot dequeue: circular queue is empty.");
        }
        T data = (T) items[front];
        items[front] = null;
        front = (front + 1) % capacity;
        size--;
        return data;
    }

    @SuppressWarnings("unchecked")
    public T peekFront() {
        if (isEmpty()) {
            throw new EmptyStructureException("Cannot peek: circular queue is empty.");
        }
        return (T) items[front];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public int size() {
        return size;
    }

    public int frontIndex() {
        return front;
    }

    public int rearIndex() {
        return rear;
    }

    public String printTraceState() {
        StringBuilder sb = new StringBuilder();
        sb.append("front=").append(front).append(", rear=").append(rear)
          .append(", size=").append(size).append("/").append(capacity).append("  contents: [ ");
        for (int i = 0; i < size; i++) {
            int index = (front + i) % capacity;
            sb.append(items[index]);
            if (i < size - 1) {
                sb.append(" , ");
            }
        }
        sb.append(" ]");
        return sb.toString();
    }
}
