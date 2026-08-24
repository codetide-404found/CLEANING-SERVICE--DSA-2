import java.util.Comparator;

/**
 * Custom Sorting Algorithms Engine built from scratch without standard Java library sorting utilities.
 * Implements Selection Sort, Insertion Sort, Merge Sort, and Quick Sort.
 * Includes operation metric instrumentation, stability tracking, recursion depth tracking,
 * and primitive/object generic overloads.
 * 
 * Member 11: Searching, Sorting & Performance Analysis
 * Ghana Smart Service Operations Optimizer - University of Ghana
 */
public class SortingAlgorithms {

    // =========================================================================
    // 1. SELECTION SORT
    // =========================================================================

    /**
     * In-place, unstable sorting algorithm.
     * Complexity: O(N^2) Best, Average, and Worst case comparisons.
     * Swaps: O(N) swaps.
     */
    public static <T extends Comparable<T>> void selectionSort(T[] array) {
        if (array == null || array.length <= 1) return;
        selectionSort(array, Comparable::compareTo);
    }

    public static <T> void selectionSort(T[] array, Comparator<T> comparator) {
        if (array == null || array.length <= 1 || comparator == null) return;
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (comparator.compare(array[j], array[minIdx]) < 0) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                T temp = array[i];
                array[i] = array[minIdx];
                array[minIdx] = temp;
            }
        }
    }

    public static <T> void selectionSortWithMetrics(T[] array, Comparator<T> comparator, SortMetrics metrics) {
        if (array == null || array.length <= 1) {
            if (metrics != null) metrics.setExecutionTimeNs(0);
            return;
        }
        long startTime = System.nanoTime();
        int n = array.length;

        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (metrics != null) metrics.incrementComparisons();
                if (comparator.compare(array[j], array[minIdx]) < 0) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                T temp = array[i];
                array[i] = array[minIdx];
                array[minIdx] = temp;
                if (metrics != null) metrics.incrementSwaps();
            }
        }

        long endTime = System.nanoTime();
        if (metrics != null) {
            metrics.setExecutionTimeNs(endTime - startTime);
            metrics.setMemoryUsedKb(getApproxMemoryKb());
        }
    }

    public static void selectionSort(int[] array) {
        if (array == null || array.length <= 1) return;
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j] < array[minIdx]) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                int temp = array[i];
                array[i] = array[minIdx];
                array[minIdx] = temp;
            }
        }
    }

    public static void selectionSortWithMetrics(int[] array, SortMetrics metrics) {
        if (array == null || array.length <= 1) return;
        long startTime = System.nanoTime();
        int n = array.length;

        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (metrics != null) metrics.incrementComparisons();
                if (array[j] < array[minIdx]) {
                    minIdx = j;
                }
            }
            if (minIdx != i) {
                int temp = array[i];
                array[i] = array[minIdx];
                array[minIdx] = temp;
                if (metrics != null) metrics.incrementSwaps();
            }
        }

        long endTime = System.nanoTime();
        if (metrics != null) {
            metrics.setExecutionTimeNs(endTime - startTime);
            metrics.setMemoryUsedKb(getApproxMemoryKb());
        }
    }

    // =========================================================================
    // 2. INSERTION SORT
    // =========================================================================

    /**
     * In-place, stable, adaptive sorting algorithm.
     * Complexity: O(N) Best case (nearly sorted), O(N^2) Average & Worst case.
     * Shifts: O(N^2) max shifts.
     */
    public static <T extends Comparable<T>> void insertionSort(T[] array) {
        if (array == null || array.length <= 1) return;
        insertionSort(array, Comparable::compareTo);
    }

    public static <T> void insertionSort(T[] array, Comparator<T> comparator) {
        if (array == null || array.length <= 1 || comparator == null) return;
        int n = array.length;
        for (int i = 1; i < n; i++) {
            T key = array[i];
            int j = i - 1;
            while (j >= 0 && comparator.compare(array[j], key) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    public static <T> void insertionSortWithMetrics(T[] array, Comparator<T> comparator, SortMetrics metrics) {
        if (array == null || array.length <= 1) {
            if (metrics != null) metrics.setExecutionTimeNs(0);
            return;
        }
        long startTime = System.nanoTime();
        int n = array.length;

        for (int i = 1; i < n; i++) {
            T key = array[i];
            int j = i - 1;
            while (j >= 0) {
                if (metrics != null) metrics.incrementComparisons();
                if (comparator.compare(array[j], key) > 0) {
                    array[j + 1] = array[j];
                    if (metrics != null) metrics.incrementShiftsOrCopies();
                    j--;
                } else {
                    break;
                }
            }
            array[j + 1] = key;
            if (metrics != null) metrics.incrementShiftsOrCopies();
        }

        long endTime = System.nanoTime();
        if (metrics != null) {
            metrics.setExecutionTimeNs(endTime - startTime);
            metrics.setMemoryUsedKb(getApproxMemoryKb());
        }
    }

    public static void insertionSort(int[] array) {
        if (array == null || array.length <= 1) return;
        int n = array.length;
        for (int i = 1; i < n; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    public static void insertionSortWithMetrics(int[] array, SortMetrics metrics) {
        if (array == null || array.length <= 1) return;
        long startTime = System.nanoTime();
        int n = array.length;

        for (int i = 1; i < n; i++) {
            int key = array[i];
            int j = i - 1;
            while (j >= 0) {
                if (metrics != null) metrics.incrementComparisons();
                if (array[j] > key) {
                    array[j + 1] = array[j];
                    if (metrics != null) metrics.incrementShiftsOrCopies();
                    j--;
                } else {
                    break;
                }
            }
            array[j + 1] = key;
            if (metrics != null) metrics.incrementShiftsOrCopies();
        }

        long endTime = System.nanoTime();
        if (metrics != null) {
            metrics.setExecutionTimeNs(endTime - startTime);
            metrics.setMemoryUsedKb(getApproxMemoryKb());
        }
    }

    // =========================================================================
    // 3. MERGE SORT
    // =========================================================================

    /**
     * Divide-and-conquer, stable sorting algorithm.
     * Complexity: O(N log N) Best, Average, and Worst case.
     * Auxiliary Space: O(N).
     */
    public static <T extends Comparable<T>> void mergeSort(T[] array) {
        if (array == null || array.length <= 1) return;
        mergeSort(array, Comparable::compareTo);
    }

    public static <T> void mergeSort(T[] array, Comparator<T> comparator) {
        if (array == null || array.length <= 1 || comparator == null) return;
        @SuppressWarnings("unchecked")
        T[] aux = (T[]) new Object[array.length];
        mergeSortHelper(array, aux, 0, array.length - 1, comparator, null, 1);
    }

    public static <T> void mergeSortWithMetrics(T[] array, Comparator<T> comparator, SortMetrics metrics) {
        if (array == null || array.length <= 1) {
            if (metrics != null) metrics.setExecutionTimeNs(0);
            return;
        }
        long startTime = System.nanoTime();
        @SuppressWarnings("unchecked")
        T[] aux = (T[]) new Object[array.length];
        if (metrics != null) metrics.addShiftsOrCopies(array.length);

        mergeSortHelper(array, aux, 0, array.length - 1, comparator, metrics, 1);

        long endTime = System.nanoTime();
        if (metrics != null) {
            metrics.setExecutionTimeNs(endTime - startTime);
            metrics.setMemoryUsedKb(getApproxMemoryKb());
        }
    }

    private static <T> void mergeSortHelper(T[] array, T[] aux, int low, int high, Comparator<T> comparator, SortMetrics metrics, int depth) {
        if (low >= high) return;
        if (metrics != null) metrics.updateRecursionDepth(depth);

        int mid = low + (high - low) / 2;
        mergeSortHelper(array, aux, low, mid, comparator, metrics, depth + 1);
        mergeSortHelper(array, aux, mid + 1, high, comparator, metrics, depth + 1);
        merge(array, aux, low, mid, high, comparator, metrics);
    }

    private static <T> void merge(T[] array, T[] aux, int low, int mid, int high, Comparator<T> comparator, SortMetrics metrics) {
        for (int k = low; k <= high; k++) {
            aux[k] = array[k];
            if (metrics != null) metrics.incrementShiftsOrCopies();
        }

        int i = low;
        int j = mid + 1;

        for (int k = low; k <= high; k++) {
            if (i > mid) {
                array[k] = aux[j++];
                if (metrics != null) metrics.incrementShiftsOrCopies();
            } else if (j > high) {
                array[k] = aux[i++];
                if (metrics != null) metrics.incrementShiftsOrCopies();
            } else {
                if (metrics != null) metrics.incrementComparisons();
                if (comparator.compare(aux[j], aux[i]) < 0) {
                    array[k] = aux[j++];
                    if (metrics != null) metrics.incrementShiftsOrCopies();
                } else {
                    array[k] = aux[i++];
                    if (metrics != null) metrics.incrementShiftsOrCopies();
                }
            }
        }
    }

    public static void mergeSort(int[] array) {
        if (array == null || array.length <= 1) return;
        int[] aux = new int[array.length];
        mergeSortHelper(array, aux, 0, array.length - 1, null, 1);
    }

    public static void mergeSortWithMetrics(int[] array, SortMetrics metrics) {
        if (array == null || array.length <= 1) return;
        long startTime = System.nanoTime();
        int[] aux = new int[array.length];
        if (metrics != null) metrics.addShiftsOrCopies(array.length);

        mergeSortHelper(array, aux, 0, array.length - 1, metrics, 1);

        long endTime = System.nanoTime();
        if (metrics != null) {
            metrics.setExecutionTimeNs(endTime - startTime);
            metrics.setMemoryUsedKb(getApproxMemoryKb());
        }
    }

    private static void mergeSortHelper(int[] array, int[] aux, int low, int high, SortMetrics metrics, int depth) {
        if (low >= high) return;
        if (metrics != null) metrics.updateRecursionDepth(depth);

        int mid = low + (high - low) / 2;
        mergeSortHelper(array, aux, low, mid, metrics, depth + 1);
        mergeSortHelper(array, aux, mid + 1, high, metrics, depth + 1);
        merge(array, aux, low, mid, high, metrics);
    }

    private static void merge(int[] array, int[] aux, int low, int mid, int high, SortMetrics metrics) {
        for (int k = low; k <= high; k++) {
            aux[k] = array[k];
            if (metrics != null) metrics.incrementShiftsOrCopies();
        }

        int i = low;
        int j = mid + 1;

        for (int k = low; k <= high; k++) {
            if (i > mid) {
                array[k] = aux[j++];
                if (metrics != null) metrics.incrementShiftsOrCopies();
            } else if (j > high) {
                array[k] = aux[i++];
                if (metrics != null) metrics.incrementShiftsOrCopies();
            } else {
                if (metrics != null) metrics.incrementComparisons();
                if (aux[j] < aux[i]) {
                    array[k] = aux[j++];
                    if (metrics != null) metrics.incrementShiftsOrCopies();
                } else {
                    array[k] = aux[i++];
                    if (metrics != null) metrics.incrementShiftsOrCopies();
                }
            }
        }
    }

    // =========================================================================
    // 4. QUICK SORT
    // =========================================================================

    /**
     * Divide-and-conquer, in-place, unstable sorting algorithm.
     * Complexity: O(N log N) Best and Average case, O(N^2) Worst case (sorted input with bad pivot).
     * Auxiliary Space: O(log N) stack space.
     */
    public static <T extends Comparable<T>> void quickSort(T[] array) {
        if (array == null || array.length <= 1) return;
        quickSort(array, Comparable::compareTo);
    }

    public static <T> void quickSort(T[] array, Comparator<T> comparator) {
        if (array == null || array.length <= 1 || comparator == null) return;
        quickSortHelper(array, 0, array.length - 1, comparator, null, 1);
    }

    public static <T> void quickSortWithMetrics(T[] array, Comparator<T> comparator, SortMetrics metrics) {
        if (array == null || array.length <= 1) {
            if (metrics != null) metrics.setExecutionTimeNs(0);
            return;
        }
        long startTime = System.nanoTime();
        quickSortHelper(array, 0, array.length - 1, comparator, metrics, 1);

        long endTime = System.nanoTime();
        if (metrics != null) {
            metrics.setExecutionTimeNs(endTime - startTime);
            metrics.setMemoryUsedKb(getApproxMemoryKb());
        }
    }

    private static <T> void quickSortHelper(T[] array, int low, int high, Comparator<T> comparator, SortMetrics metrics, int depth) {
        if (low >= high) return;
        if (metrics != null) metrics.updateRecursionDepth(depth);

        int pivotIndex = partition(array, low, high, comparator, metrics);
        quickSortHelper(array, low, pivotIndex - 1, comparator, metrics, depth + 1);
        quickSortHelper(array, pivotIndex + 1, high, comparator, metrics, depth + 1);
    }

    private static <T> int partition(T[] array, int low, int high, Comparator<T> comparator, SortMetrics metrics) {
        // Median-of-three pivot selection to mitigate worst-case scenario
        int mid = low + (high - low) / 2;
        if (comparator.compare(array[mid], array[low]) < 0) swap(array, low, mid, metrics);
        if (comparator.compare(array[high], array[low]) < 0) swap(array, low, high, metrics);
        if (comparator.compare(array[high], array[mid]) < 0) swap(array, mid, high, metrics);
        swap(array, mid, high, metrics); // Move median pivot to end

        T pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (metrics != null) metrics.incrementComparisons();
            if (comparator.compare(array[j], pivot) <= 0) {
                i++;
                swap(array, i, j, metrics);
            }
        }
        swap(array, i + 1, high, metrics);
        return i + 1;
    }

    private static <T> void swap(T[] array, int i, int j, SortMetrics metrics) {
        if (i == j) return;
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        if (metrics != null) metrics.incrementSwaps();
    }

    public static void quickSort(int[] array) {
        if (array == null || array.length <= 1) return;
        quickSortHelper(array, 0, array.length - 1, null, 1);
    }

    public static void quickSortWithMetrics(int[] array, SortMetrics metrics) {
        if (array == null || array.length <= 1) return;
        long startTime = System.nanoTime();
        quickSortHelper(array, 0, array.length - 1, metrics, 1);

        long endTime = System.nanoTime();
        if (metrics != null) {
            metrics.setExecutionTimeNs(endTime - startTime);
            metrics.setMemoryUsedKb(getApproxMemoryKb());
        }
    }

    private static void quickSortHelper(int[] array, int low, int high, SortMetrics metrics, int depth) {
        if (low >= high) return;
        if (metrics != null) metrics.updateRecursionDepth(depth);

        int pivotIndex = partition(array, low, high, metrics);
        quickSortHelper(array, low, pivotIndex - 1, metrics, depth + 1);
        quickSortHelper(array, pivotIndex + 1, high, metrics, depth + 1);
    }

    private static int partition(int[] array, int low, int high, SortMetrics metrics) {
        int mid = low + (high - low) / 2;
        if (array[mid] < array[low]) swap(array, low, mid, metrics);
        if (array[high] < array[low]) swap(array, low, high, metrics);
        if (array[high] < array[mid]) swap(array, mid, high, metrics);
        swap(array, mid, high, metrics);

        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (metrics != null) metrics.incrementComparisons();
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j, metrics);
            }
        }
        swap(array, i + 1, high, metrics);
        return i + 1;
    }

    private static void swap(int[] array, int i, int j, SortMetrics metrics) {
        if (i == j) return;
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        if (metrics != null) metrics.incrementSwaps();
    }

    private static long getApproxMemoryKb() {
        Runtime rt = Runtime.getRuntime();
        return (rt.totalMemory() - rt.freeMemory()) / 1024;
    }
}
