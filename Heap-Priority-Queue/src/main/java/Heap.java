import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Heap {

    private final ArrayList<CleaningRequest> heap;
    private final Map<String, Integer> requestIdToIndex;

    public Heap() {
        this.heap = new ArrayList<>();
        this.requestIdToIndex = new HashMap<>();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    public void insert(CleaningRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }
        if (requestIdToIndex.containsKey(request.getRequestId())) {
            throw new IllegalArgumentException("Duplicate request ID: " + request.getRequestId());
        }

        heap.add(request);
        int index = heap.size() - 1;
        requestIdToIndex.put(request.getRequestId(), index);
        siftUp(index);
    }

    public CleaningRequest peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        return heap.get(0);
    }

    public CleaningRequest extractHighestPriority() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }

        CleaningRequest highest = heap.get(0);
        CleaningRequest last = heap.remove(heap.size() - 1);
        requestIdToIndex.remove(highest.getRequestId());

        if (!heap.isEmpty()) {
            heap.set(0, last);
            requestIdToIndex.put(last.getRequestId(), 0);
            siftDown(0);
        }

        return highest;
    }

    public boolean updatePriority(String requestId, Priority newPriority) {
        if (newPriority == null) {
            throw new IllegalArgumentException("New priority cannot be null");
        }

        Integer index = requestIdToIndex.get(requestId);
        if (index == null) {
            return false;
        }

        CleaningRequest request = heap.get(index);
        Priority oldPriority = request.getPriority();
        request.setPriority(newPriority);

        if (newPriority.getLevel() > oldPriority.getLevel()) {
            siftUp(index);
        } else if (newPriority.getLevel() < oldPriority.getLevel()) {
            siftDown(index);
        }

        return true;
    }

    public void heapify(int index) {
        siftDown(index);
    }

    public boolean contains(String requestId) {
        return requestIdToIndex.containsKey(requestId);
    }

    public int getIndex(String requestId) {
        Integer index = requestIdToIndex.get(requestId);
        return index != null ? index : -1;
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            CleaningRequest current = heap.get(index);
            CleaningRequest parent = heap.get(parentIndex);

            if (current.compareTo(parent) > 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    private void siftDown(int index) {
        int n = heap.size();
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int largest = index;

            if (left < n && heap.get(left).compareTo(heap.get(largest)) > 0) {
                largest = left;
            }
            if (right < n && heap.get(right).compareTo(heap.get(largest)) > 0) {
                largest = right;
            }
            if (largest == index) {
                break;
            }
            swap(index, largest);
            index = largest;
        }
    }

    private void swap(int i, int j) {
        CleaningRequest temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);

        requestIdToIndex.put(heap.get(i).getRequestId(), i);
        requestIdToIndex.put(heap.get(j).getRequestId(), j);
    }

    public boolean validateHeapProperty() {
        for (int i = 0; i < heap.size(); i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < heap.size() && heap.get(i).compareTo(heap.get(left)) < 0) {
                return false;
            }
            if (right < heap.size() && heap.get(i).compareTo(heap.get(right)) < 0) {
                return false;
            }
        }
        return true;
    }
}
