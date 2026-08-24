import java.util.Comparator;

/**
 * Custom Searching Algorithms Engine built from scratch without standard Java library utilities.
 * Implements Linear Search and Binary Search (Iterative & Recursive) with precondition validation,
 * operation metric tracking, and explicit counterexample handling.
 * 
 * Member 11: Searching, Sorting & Performance Analysis
 * Ghana Smart Service Operations Optimizer - University of Ghana
 */
public class SearchingAlgorithms {

    // =========================================================================
    // LINEAR SEARCH IMPLEMENTATIONS
    // =========================================================================

    /**
     * Performs a standard linear search on a Comparable array.
     * Complexity: O(N) Worst/Average, O(1) Best.
     * Space Complexity: O(1).
     * 
     * @param array The array to search through.
     * @param target The item to find.
     * @param <T> Type implementing Comparable.
     * @return Index of the target item, or -1 if not found.
     */
    public static <T extends Comparable<T>> int linearSearch(T[] array, T target) {
        if (array == null || target == null) {
            return -1;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && array[i].compareTo(target) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Linear search using a custom Comparator.
     */
    public static <T> int linearSearch(T[] array, Comparator<T> comparator, T target) {
        if (array == null || target == null || comparator == null) {
            return -1;
        }
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null && comparator.compare(array[i], target) == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Linear search with operation metrics instrumentation.
     */
    public static <T extends Comparable<T>> int linearSearchWithMetrics(T[] array, T target, SearchMetrics metrics) {
        if (array == null || target == null) {
            if (metrics != null) metrics.setTargetIndex(-1);
            return -1;
        }
        long startTime = System.nanoTime();
        int foundIndex = -1;

        for (int i = 0; i < array.length; i++) {
            if (metrics != null) metrics.incrementComparisons();
            if (array[i] != null && array[i].compareTo(target) == 0) {
                foundIndex = i;
                break;
            }
        }

        long endTime = System.nanoTime();
        if (metrics != null) {
            metrics.setExecutionTimeNs(endTime - startTime);
            metrics.setTargetIndex(foundIndex);
            metrics.setMemoryUsedKb(getApproxMemoryKb());
        }
        return foundIndex;
    }

    /**
     * Primitive int array Linear Search overload.
     */
    public static int linearSearch(int[] array, int target) {
        if (array == null) return -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) return i;
        }
        return -1;
    }

    /**
     * Primitive int array Linear Search with metrics.
     */
    public static int linearSearchWithMetrics(int[] array, int target, SearchMetrics metrics) {
        if (array == null) {
            if (metrics != null) metrics.setTargetIndex(-1);
            return -1;
        }
        long startTime = System.nanoTime();
        int foundIndex = -1;

        for (int i = 0; i < array.length; i++) {
            if (metrics != null) metrics.incrementComparisons();
            if (array[i] == target) {
                foundIndex = i;
                break;
            }
        }

        long endTime = System.nanoTime();
        if (metrics != null) {
            metrics.setExecutionTimeNs(endTime - startTime);
            metrics.setTargetIndex(foundIndex);
            metrics.setMemoryUsedKb(getApproxMemoryKb());
        }
        return foundIndex;
    }

    // =========================================================================
    // BINARY SEARCH IMPLEMENTATIONS (ITERATIVE & RECURSIVE)
    // =========================================================================

    /**
     * Standard Iterative Binary Search on a sorted Comparable array.
     * Precondition: The array MUST be sorted in ascending order.
     * Complexity: O(log N) Worst/Average, O(1) Best.
     * Space Complexity: O(1).
     * 
     * @param array Sorted array to search.
     * @param target Target key.
     * @param <T> Comparable type.
     * @return Target index or -1 if absent.
     */
    public static <T extends Comparable<T>> int binarySearch(T[] array, T target) {
        if (array == null || target == null || array.length == 0) {
            return -1;
        }
        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = array[mid].compareTo(target);

            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    /**
     * Recursive Binary Search implementation.
     */
    public static <T extends Comparable<T>> int binarySearchRecursive(T[] array, T target) {
        if (array == null || target == null || array.length == 0) {
            return -1;
        }
        return binarySearchRecursiveHelper(array, target, 0, array.length - 1);
    }

    private static <T extends Comparable<T>> int binarySearchRecursiveHelper(T[] array, T target, int low, int high) {
        if (low > high) {
            return -1;
        }
        int mid = low + (high - low) / 2;
        int cmp = array[mid].compareTo(target);

        if (cmp == 0) {
            return mid;
        } else if (cmp < 0) {
            return binarySearchRecursiveHelper(array, target, mid + 1, high);
        } else {
            return binarySearchRecursiveHelper(array, target, low, mid - 1);
        }
    }

    /**
     * Binary search using custom Comparator.
     */
    public static <T> int binarySearch(T[] array, Comparator<T> comparator, T target) {
        if (array == null || target == null || comparator == null || array.length == 0) {
            return -1;
        }
        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            int cmp = comparator.compare(array[mid], target);

            if (cmp == 0) {
                return mid;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    /**
     * Iterative Binary Search with operation metric instrumentation.
     */
    public static <T extends Comparable<T>> int binarySearchWithMetrics(T[] array, T target, SearchMetrics metrics) {
        if (array == null || target == null || array.length == 0) {
            if (metrics != null) metrics.setTargetIndex(-1);
            return -1;
        }
        long startTime = System.nanoTime();
        int low = 0;
        int high = array.length - 1;
        int foundIndex = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (metrics != null) metrics.incrementComparisons();
            int cmp = array[mid].compareTo(target);

            if (cmp == 0) {
                foundIndex = mid;
                break;
            } else if (cmp < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        long endTime = System.nanoTime();
        if (metrics != null) {
            metrics.setExecutionTimeNs(endTime - startTime);
            metrics.setTargetIndex(foundIndex);
            metrics.setMemoryUsedKb(getApproxMemoryKb());
        }
        return foundIndex;
    }

    /**
     * Primitive int array Binary Search overload.
     */
    public static int binarySearch(int[] array, int target) {
        if (array == null || array.length == 0) return -1;
        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (array[mid] == target) {
                return mid;
            } else if (array[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }

    /**
     * Primitive int array Binary Search with metrics instrumentation.
     */
    public static int binarySearchWithMetrics(int[] array, int target, SearchMetrics metrics) {
        if (array == null || array.length == 0) {
            if (metrics != null) metrics.setTargetIndex(-1);
            return -1;
        }
        long startTime = System.nanoTime();
        int low = 0;
        int high = array.length - 1;
        int foundIndex = -1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (metrics != null) metrics.incrementComparisons();
            if (array[mid] == target) {
                foundIndex = mid;
                break;
            } else if (array[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        long endTime = System.nanoTime();
        if (metrics != null) {
            metrics.setExecutionTimeNs(endTime - startTime);
            metrics.setTargetIndex(foundIndex);
            metrics.setMemoryUsedKb(getApproxMemoryKb());
        }
        return foundIndex;
    }

    // =========================================================================
    // PRECONDITION CHECKING & COUNTEREXAMPLE DEMONSTRATIONS
    // =========================================================================

    /**
     * Validates whether an array satisfies the sorted precondition required by Binary Search.
     */
    public static <T extends Comparable<T>> boolean isSorted(T[] array) {
        if (array == null || array.length <= 1) {
            return true;
        }
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] != null && array[i + 1] != null && array[i].compareTo(array[i + 1]) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates if a primitive int array is sorted.
     */
    public static boolean isSorted(int[] array) {
        if (array == null || array.length <= 1) {
            return true;
        }
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Strict Binary Search with precondition assertion. Throws IllegalStateException if unsorted.
     */
    public static <T extends Comparable<T>> int binarySearchStrict(T[] array, T target) {
        if (!isSorted(array)) {
            throw new IllegalStateException("BINARY SEARCH PRECONDITION FAILED: Input array is not sorted in ascending order!");
        }
        return binarySearch(array, target);
    }

    /**
     * Demonstrates Binary Search failure when precondition is violated (unsorted input counterexample).
     * @return Execution summary describing how binary search misleads on unsorted data.
     */
    public static String demonstrateUnsortedBinarySearchFailure(int[] unsortedArray, int target) {
        int binaryResult = binarySearch(unsortedArray, target);
        int linearResult = linearSearch(unsortedArray, target);
        boolean isSorted = isSorted(unsortedArray);

        StringBuilder sb = new StringBuilder();
        sb.append("===== BINARY SEARCH INVALID PRECONDITION COUNTEREXAMPLE =====\n");
        sb.append("Array sorted status: ").append(isSorted).append("\n");
        sb.append("Target element to find: ").append(target).append("\n");
        sb.append("Linear Search result (Ground Truth): ").append(linearResult).append("\n");
        sb.append("Binary Search result (Flawed): ").append(binaryResult).append("\n");
        if (linearResult != binaryResult) {
            sb.append("CONCLUSION: Binary search returned incorrect index (").append(binaryResult)
              .append(") because array was unsorted, proving the binary search precondition requirement!\n");
        } else {
            sb.append("CONCLUSION: Binary search coincidentally matched linear search, but algorithm assumption is violated.\n");
        }
        return sb.toString();
    }

    private static long getApproxMemoryKb() {
        Runtime rt = Runtime.getRuntime();
        return (rt.totalMemory() - rt.freeMemory()) / 1024;
    }
}
