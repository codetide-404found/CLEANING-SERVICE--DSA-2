public class PriorityQueueManager {

    private final Heap heap;

    public PriorityQueueManager() {
        this.heap = new Heap();
    }

    public void addRequest(CleaningRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        heap.insert(request);
    }

    public CleaningRequest getNextRequest() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }
        return heap.extractHighestPriority();
    }

    public CleaningRequest peekNextRequest() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty");
        }
        return heap.peek();
    }

    public boolean updateRequestPriority(String requestId, Priority newPriority) {
        if (requestId == null || requestId.isBlank()) {
            throw new IllegalArgumentException("Request ID cannot be null or blank");
        }
        if (newPriority == null) {
            throw new IllegalArgumentException("New priority cannot be null");
        }
        return heap.updatePriority(requestId, newPriority);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    public boolean contains(String requestId) {
        return heap.contains(requestId);
    }

    public boolean validateHeapProperty() {
        return heap.validateHeapProperty();
    }
}
