import java.util.ArrayList;

/**
 * Custom binary min-heap keyed on (nodeId, distance).
 * Built from scratch so Dijkstra does not depend on java.util.PriorityQueue,
 * per the project brief's rule that built-in PriorityQueue/HashMap/etc.
 * are not allowed for assessed core logic (Section 8i).
 *
 * Supports the classic "decrease-key" pattern used by Dijkstra: instead of
 * removing and re-inserting, we simply push a new (node, smallerDistance)
 * pair and let stale, larger-distance entries get skipped when popped
 * (checked against the "finalized" set in Graph.dijkstra).
 */
public class MinHeap {

    // Each heap entry: [0] = locationId, [1] = distance
    private final ArrayList<long[]> heap = new ArrayList<>();

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    public void push(int locationId, long distance) {
        heap.add(new long[]{locationId, distance});
        siftUp(heap.size() - 1);
    }

    /** Removes and returns the entry with the smallest distance. */
    public long[] popMin() {
        if (heap.isEmpty()) throw new IllegalStateException("MinHeap is empty");
        long[] min = heap.get(0);
        long[] last = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, last);
            siftDown(0);
        }
        return min; // {locationId, distance}
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap.get(i)[1] < heap.get(parent)[1]) {
                swap(i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }

    private void siftDown(int i) {
        int n = heap.size();
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int smallest = i;
            if (left < n && heap.get(left)[1] < heap.get(smallest)[1]) smallest = left;
            if (right < n && heap.get(right)[1] < heap.get(smallest)[1]) smallest = right;
            if (smallest == i) break;
            swap(i, smallest);
            i = smallest;
        }
    }

    private void swap(int i, int j) {
        long[] tmp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, tmp);
    }
}
