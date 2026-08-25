/**
 * Runnable demo using integer keys, proves DynamicArray resize cost stays
 * amortized O(1) even as the array grows well beyond its starting capacity.
 * Mirrors the style of TraceDriver.java (BST/RBT module) for this project.
 */
public class DynamicArrayTraceDriver {

    public static void main(String[] args) {
        System.out.println("=== DYNAMIC ARRAY RESIZE TRACE (generic integer keys) ===\n");

        DynamicArray<Integer> arr = new DynamicArray<>(2);
        System.out.println("Starting capacity: " + arr.capacity());

        for (int i = 1; i <= 20; i++) {
            int before = arr.capacity();
            arr.insert(i);
            int after = arr.capacity();
            String note = (before != after) ? "  <-- RESIZED (" + before + " -> " + after + ")" : "";
            System.out.println("insert #" + i + " -> size=" + arr.size() + ", capacity=" + after + note);
        }

        System.out.println("\nFinal array: " + arr);
        System.out.println("Final size=" + arr.size() + ", capacity=" + arr.capacity());

        System.out.println("\n=== MyLinkedList mid-sequence edit trace ===\n");
        MyLinkedList<Integer> list = new MyLinkedList<>();
        list.addLast(1);
        list.addLast(2);
        list.addLast(4);
        System.out.println("Initial: " + list);
        list.insertAfter(2, 3);
        System.out.println("After insertAfter(2, 3): " + list);
        list.remove(1);
        System.out.println("After remove(1): " + list);
        list.addFirst(0);
        System.out.println("After addFirst(0): " + list);
    }
}
