import java.util.Arrays;

/**
 * Runnable Trace Table Generator (Section 10 & 15 Compliance).
 * Produces step-by-step algorithm trace execution logs for Binary Search, Insertion Sort,
 * Selection Sort, Merge Sort, Quick Sort, and Unsorted Binary Search Failure Counterexample.
 * 
 * Member 11: Searching, Sorting & Performance Analysis
 * Ghana Smart Service Operations Optimizer - University of Ghana
 */
public class TraceDriver {

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println(" MEMBER 11: ALGORITHM TRACE TABLE GENERATOR ");
        System.out.println("========================================================================\n");

        traceBinarySearch();
        traceInsertionSort();
        traceSelectionSort();
        traceMergeSort();
        traceQuickSort();
        traceUnsortedBinarySearchCounterexample();

        System.out.println("========================================================================");
        System.out.println(" ALL ALGORITHM TRACE TABLES GENERATED SUCCESSFULLY!");
        System.out.println("========================================================================");
    }

    private static void traceBinarySearch() {
        System.out.println("--- 1. BINARY SEARCH STEP-BY-STEP TRACE ---");
        int[] arr = {11, 23, 34, 45, 56, 67, 78, 89, 90, 99}; // Sorted array N=10
        int target = 67;

        System.out.println("Target: " + target + " | Array: " + Arrays.toString(arr));
        System.out.println("+------+-----+------+-----+-----------+-------------------------+");
        System.out.println("| Step | Low | High | Mid | Array[Mid]| Comparison / Action     |");
        System.out.println("+------+-----+------+-----+-----------+-------------------------+");

        int low = 0;
        int high = arr.length - 1;
        int step = 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int val = arr[mid];
            String action;

            if (val == target) {
                action = "Found! Return Index " + mid;
                System.out.printf("| %-4d | %-3d | %-4d | %-3d | %-9d | %-23s |\n", step, low, high, mid, val, action);
                break;
            } else if (val < target) {
                action = val + " < " + target + " -> Low = " + (mid + 1);
                System.out.printf("| %-4d | %-3d | %-4d | %-3d | %-9d | %-23s |\n", step, low, high, mid, val, action);
                low = mid + 1;
            } else {
                action = val + " > " + target + " -> High = " + (mid - 1);
                System.out.printf("| %-4d | %-3d | %-4d | %-3d | %-9d | %-23s |\n", step, low, high, mid, val, action);
                high = mid - 1;
            }
            step++;
        }
        System.out.println("+------+-----+------+-----+-----------+-------------------------+\n");
    }

    private static void traceInsertionSort() {
        System.out.println("--- 2. INSERTION SORT STEP-BY-STEP TRACE ---");
        int[] arr = {45, 12, 89, 23, 7};
        System.out.println("Initial Array: " + Arrays.toString(arr));
        System.out.println("+------+-----+-------+------------------------+-------------------------+");
        System.out.println("| Pass | i   | Key   | Comparisons & Shifts   | Array State after Pass  |");
        System.out.println("+------+-----+-------+------------------------+-------------------------+");

        int n = arr.length;
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            int shifts = 0;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
                shifts++;
            }
            arr[j + 1] = key;
            String details = String.format("Shifted %d elements", shifts);
            System.out.printf("| %-4d | %-3d | %-5d | %-22s | %-23s |\n",
                    i, i, key, details, Arrays.toString(arr));
        }
        System.out.println("+------+-----+-------+------------------------+-------------------------+\n");
    }

    private static void traceSelectionSort() {
        System.out.println("--- 3. SELECTION SORT STEP-BY-STEP TRACE ---");
        int[] arr = {64, 25, 12, 22, 11};
        System.out.println("Initial Array: " + Arrays.toString(arr));
        System.out.println("+------+-------+-------------+---------------+-------------------------+");
        System.out.println("| Pass | i     | Min Found   | Swap Action   | Array State after Pass  |");
        System.out.println("+------+-------+-------------+---------------+-------------------------+");

        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIdx]) {
                    minIdx = j;
                }
            }
            String swapAction = "No Swap";
            if (minIdx != i) {
                swapAction = String.format("Swap(%d,%d)", arr[i], arr[minIdx]);
                int temp = arr[i];
                arr[i] = arr[minIdx];
                arr[minIdx] = temp;
            }
            System.out.printf("| %-4d | %-5d | Val %-7d | %-13s | %-23s |\n",
                    i + 1, i, arr[i], swapAction, Arrays.toString(arr));
        }
        System.out.println("+------+-------+-------------+---------------+-------------------------+\n");
    }

    private static void traceMergeSort() {
        System.out.println("--- 4. MERGE SORT RECURSION TRACE ---");
        int[] arr = {38, 27, 43, 3, 9, 82, 10};
        System.out.println("Initial Array: " + Arrays.toString(arr));
        System.out.println("Execution trace:");
        int[] aux = new int[arr.length];
        traceMergeSortHelper(arr, aux, 0, arr.length - 1, 1);
        System.out.println("Final Sorted Array: " + Arrays.toString(arr) + "\n");
    }

    private static void traceMergeSortHelper(int[] arr, int[] aux, int low, int high, int depth) {
        if (low >= high) return;
        int mid = low + (high - low) / 2;
        String indent = "  ".repeat(depth);
        System.out.printf("%sSplit [%d..%d] into [%d..%d] and [%d..%d]\n", indent, low, high, low, mid, mid + 1, high);

        traceMergeSortHelper(arr, aux, low, mid, depth + 1);
        traceMergeSortHelper(arr, aux, mid + 1, high, depth + 1);

        // Merge
        for (int k = low; k <= high; k++) aux[k] = arr[k];
        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) arr[k] = aux[j++];
            else if (j > high) arr[k] = aux[i++];
            else if (aux[j] < aux[i]) arr[k] = aux[j++];
            else arr[k] = aux[i++];
        }
        System.out.printf("%sMerged [%d..%d] -> %s\n", indent, low, high, Arrays.toString(Arrays.copyOfRange(arr, low, high + 1)));
    }

    private static void traceQuickSort() {
        System.out.println("--- 5. QUICK SORT PARTITION TRACE ---");
        int[] arr = {10, 80, 30, 90, 40, 50, 70};
        System.out.println("Initial Array: " + Arrays.toString(arr));
        traceQuickSortHelper(arr, 0, arr.length - 1, 1);
        System.out.println("Final Sorted Array: " + Arrays.toString(arr) + "\n");
    }

    private static void traceQuickSortHelper(int[] arr, int low, int high, int depth) {
        if (low >= high) return;
        String indent = "  ".repeat(depth);
        int pivotVal = arr[high];
        System.out.printf("%sPartition range [%d..%d] with Pivot = %d\n", indent, low, high, pivotVal);

        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivotVal) {
                i++;
                int temp = arr[i]; arr[i] = arr[j]; arr[j] = temp;
            }
        }
        int pIndex = i + 1;
        int temp = arr[pIndex]; arr[pIndex] = arr[high]; arr[high] = temp;
        System.out.printf("%sResult after partition at index %d: %s\n", indent, pIndex, Arrays.toString(arr));

        traceQuickSortHelper(arr, low, pIndex - 1, depth + 1);
        traceQuickSortHelper(arr, pIndex + 1, high, depth + 1);
    }

    private static void traceUnsortedBinarySearchCounterexample() {
        System.out.println("--- 6. COUNTEREXAMPLE: UNSORTED BINARY SEARCH FAILURE ---");
        int[] unsorted = {85, 12, 44, 9, 99, 23};
        int target = 12;
        System.out.println(SearchingAlgorithms.demonstrateUnsortedBinarySearchFailure(unsorted, target));
    }
}
