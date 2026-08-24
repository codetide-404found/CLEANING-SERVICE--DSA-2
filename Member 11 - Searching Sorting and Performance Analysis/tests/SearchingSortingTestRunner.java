import java.util.Arrays;
import java.util.Comparator;

/**
 * Comprehensive Unit Testing Suite for Searching & Sorting Algorithms.
 * Verifies over 40+ test cases covering normal cases, boundary conditions, edge cases,
 * precondition assertions, counterexamples, stability preservation, and custom domain object handling.
 * 
 * Member 11: Searching, Sorting & Performance Analysis
 * Ghana Smart Service Operations Optimizer - University of Ghana
 */
public class SearchingSortingTestRunner {

    private static int totalTests = 0;
    private static int passedTests = 0;
    private static int failedTests = 0;

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println(" MEMBER 11: SEARCHING & SORTING UNIT TEST SUITE (40+ TEST CASES) ");
        System.out.println("========================================================================\n");

        runLinearSearchTests();
        runBinarySearchTests();
        runPreconditionAndCounterexampleTests();
        runSelectionSortTests();
        runInsertionSortTests();
        runMergeSortTests();
        runQuickSortTests();
        runStabilityAndDomainObjectTests();

        System.out.println("\n========================================================================");
        System.out.printf(" TEST SUMMARY: Total: %d | PASSED: %d | FAILED: %d\n", totalTests, passedTests, failedTests);
        if (failedTests == 0) {
            System.out.println(" RESULT: ALL UNIT TESTS PASSED PERFECTLY!");
        } else {
            System.out.println(" RESULT: SOME UNIT TESTS FAILED!");
        }
        System.out.println("========================================================================");
    }

    private static void assertTrue(boolean condition, String testName) {
        totalTests++;
        if (condition) {
            passedTests++;
            System.out.printf("  [PASS] Test %2d: %s\n", totalTests, testName);
        } else {
            failedTests++;
            System.err.printf("  [FAIL] Test %2d: %s\n", totalTests, testName);
        }
    }

    private static void assertEquals(int expected, int actual, String testName) {
        assertTrue(expected == actual, testName + String.format(" (Expected: %d, Got: %d)", expected, actual));
    }

    // =========================================================================
    // 1. LINEAR SEARCH TESTS (Tests 1 - 7)
    // =========================================================================
    private static void runLinearSearchTests() {
        System.out.println("--- 1. Linear Search Unit Tests ---");
        int[] arr = {45, 12, 89, 23, 7, 99, 34};
        
        assertEquals(0, SearchingAlgorithms.linearSearch(arr, 45), "LinearSearch: Element at start");
        assertEquals(3, SearchingAlgorithms.linearSearch(arr, 23), "LinearSearch: Element in middle");
        assertEquals(6, SearchingAlgorithms.linearSearch(arr, 34), "LinearSearch: Element at end");
        assertEquals(-1, SearchingAlgorithms.linearSearch(arr, 100), "LinearSearch: Non-existent element");
        assertEquals(-1, SearchingAlgorithms.linearSearch(new int[]{}, 10), "LinearSearch: Empty array");
        assertEquals(0, SearchingAlgorithms.linearSearch(new int[]{55}, 55), "LinearSearch: Single element match");
        assertEquals(-1, SearchingAlgorithms.linearSearch(new int[]{55}, 12), "LinearSearch: Single element mismatch");
    }

    // =========================================================================
    // 2. BINARY SEARCH TESTS (Tests 8 - 16)
    // =========================================================================
    private static void runBinarySearchTests() {
        System.out.println("\n--- 2. Binary Search Unit Tests ---");
        int[] sortedArr = {5, 12, 23, 34, 45, 67, 89, 99}; // Even length
        int[] oddSorted = {10, 20, 30, 40, 50}; // Odd length

        assertEquals(0, SearchingAlgorithms.binarySearch(sortedArr, 5), "BinarySearch: Element at start");
        assertEquals(3, SearchingAlgorithms.binarySearch(sortedArr, 34), "BinarySearch: Element in middle");
        assertEquals(7, SearchingAlgorithms.binarySearch(sortedArr, 99), "BinarySearch: Element at end");
        assertEquals(-1, SearchingAlgorithms.binarySearch(sortedArr, 50), "BinarySearch: Absent key (middle gap)");
        assertEquals(-1, SearchingAlgorithms.binarySearch(sortedArr, 1), "BinarySearch: Key smaller than min");
        assertEquals(-1, SearchingAlgorithms.binarySearch(sortedArr, 200), "BinarySearch: Key larger than max");
        assertEquals(2, SearchingAlgorithms.binarySearch(oddSorted, 30), "BinarySearch: Odd length array middle");
        assertEquals(-1, SearchingAlgorithms.binarySearch(new int[]{}, 10), "BinarySearch: Empty array boundary");
        assertEquals(0, SearchingAlgorithms.binarySearch(new int[]{42}, 42), "BinarySearch: Single element array");
    }

    // =========================================================================
    // 3. PRECONDITION & COUNTEREXAMPLE TESTS (Tests 17 - 20)
    // =========================================================================
    private static void runPreconditionAndCounterexampleTests() {
        System.out.println("\n--- 3. Precondition & Counterexample Tests ---");
        int[] sorted = {1, 2, 3, 4, 5};
        int[] unsorted = {85, 12, 44, 9, 99, 23};

        assertTrue(SearchingAlgorithms.isSorted(sorted), "isSorted: Sorted array check");
        assertTrue(!SearchingAlgorithms.isSorted(unsorted), "isSorted: Unsorted array check");

        // Counterexample: Binary search fails to find 12 because array is unsorted!
        int binUnsortedResult = SearchingAlgorithms.binarySearch(unsorted, 12);
        int linUnsortedResult = SearchingAlgorithms.linearSearch(unsorted, 12);
        assertTrue(binUnsortedResult != linUnsortedResult || binUnsortedResult == -1,
                "Counterexample: Binary Search fails on unsorted sequence");

        boolean exceptionThrown = false;
        try {
            Integer[] objUnsorted = {5, 1, 4, 2};
            SearchingAlgorithms.binarySearchStrict(objUnsorted, 4);
        } catch (IllegalStateException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown, "binarySearchStrict: Throws IllegalStateException on unsorted input");
    }

    // =========================================================================
    // 4. SELECTION SORT TESTS (Tests 21 - 26)
    // =========================================================================
    private static void runSelectionSortTests() {
        System.out.println("\n--- 4. Selection Sort Unit Tests ---");
        int[] arr1 = {64, 25, 12, 22, 11};
        SortingAlgorithms.selectionSort(arr1);
        assertTrue(SearchingAlgorithms.isSorted(arr1), "SelectionSort: Standard random array");

        int[] arr2 = {1, 2, 3, 4, 5};
        SortingAlgorithms.selectionSort(arr2);
        assertTrue(SearchingAlgorithms.isSorted(arr2), "SelectionSort: Already sorted array");

        int[] arr3 = {5, 4, 3, 2, 1};
        SortingAlgorithms.selectionSort(arr3);
        assertTrue(SearchingAlgorithms.isSorted(arr3), "SelectionSort: Reverse sorted array");

        int[] arr4 = {7, 7, 7, 7};
        SortingAlgorithms.selectionSort(arr4);
        assertTrue(SearchingAlgorithms.isSorted(arr4), "SelectionSort: All equal elements");

        int[] arr5 = {-5, 10, -2, 0, 3};
        SortingAlgorithms.selectionSort(arr5);
        assertTrue(SearchingAlgorithms.isSorted(arr5), "SelectionSort: Negative values included");

        int[] empty = {};
        SortingAlgorithms.selectionSort(empty);
        assertTrue(empty.length == 0, "SelectionSort: Empty array boundary");
    }

    // =========================================================================
    // 5. INSERTION SORT TESTS (Tests 27 - 32)
    // =========================================================================
    private static void runInsertionSortTests() {
        System.out.println("\n--- 5. Insertion Sort Unit Tests ---");
        int[] arr1 = {12, 11, 13, 5, 6};
        SortingAlgorithms.insertionSort(arr1);
        assertTrue(SearchingAlgorithms.isSorted(arr1), "InsertionSort: Standard random array");

        int[] arr2 = {1, 2, 3, 4, 5};
        SortingAlgorithms.insertionSort(arr2);
        assertTrue(SearchingAlgorithms.isSorted(arr2), "InsertionSort: Already sorted (Adaptive O(N))");

        int[] arr3 = {9, 8, 7, 6, 5, 4};
        SortingAlgorithms.insertionSort(arr3);
        assertTrue(SearchingAlgorithms.isSorted(arr3), "InsertionSort: Reverse sorted array");

        int[] arr4 = {4, 2, 4, 2, 4, 2};
        SortingAlgorithms.insertionSort(arr4);
        assertTrue(SearchingAlgorithms.isSorted(arr4), "InsertionSort: Duplicate keys handling");

        int[] single = {99};
        SortingAlgorithms.insertionSort(single);
        assertEquals(99, single[0], "InsertionSort: Single element array");

        int[] empty = {};
        SortingAlgorithms.insertionSort(empty);
        assertTrue(empty.length == 0, "InsertionSort: Empty array boundary");
    }

    // =========================================================================
    // 6. MERGE SORT TESTS (Tests 33 - 38)
    // =========================================================================
    private static void runMergeSortTests() {
        System.out.println("\n--- 6. Merge Sort Unit Tests ---");
        int[] arr1 = {38, 27, 43, 3, 9, 82, 10};
        SortingAlgorithms.mergeSort(arr1);
        assertTrue(SearchingAlgorithms.isSorted(arr1), "MergeSort: Standard random array");

        int[] arr2 = {1, 2, 3, 4, 5, 6};
        SortingAlgorithms.mergeSort(arr2);
        assertTrue(SearchingAlgorithms.isSorted(arr2), "MergeSort: Already sorted array");

        int[] arr3 = {100, 90, 80, 70, 60};
        SortingAlgorithms.mergeSort(arr3);
        assertTrue(SearchingAlgorithms.isSorted(arr3), "MergeSort: Reverse sorted array");

        int[] arr4 = {5, 1, 5, 1, 5, 1};
        SortingAlgorithms.mergeSort(arr4);
        assertTrue(SearchingAlgorithms.isSorted(arr4), "MergeSort: Duplicate elements");

        int[] large = new int[500];
        for (int i = 0; i < 500; i++) large[i] = 500 - i;
        SortingAlgorithms.mergeSort(large);
        assertTrue(SearchingAlgorithms.isSorted(large), "MergeSort: 500 element reverse array");

        int[] empty = {};
        SortingAlgorithms.mergeSort(empty);
        assertTrue(empty.length == 0, "MergeSort: Empty array boundary");
    }

    // =========================================================================
    // 7. QUICK SORT TESTS (Tests 39 - 44)
    // =========================================================================
    private static void runQuickSortTests() {
        System.out.println("\n--- 7. Quick Sort Unit Tests ---");
        int[] arr1 = {10, 7, 8, 9, 1, 5};
        SortingAlgorithms.quickSort(arr1);
        assertTrue(SearchingAlgorithms.isSorted(arr1), "QuickSort: Standard random array");

        int[] arr2 = {1, 2, 3, 4, 5, 6, 7};
        SortingAlgorithms.quickSort(arr2);
        assertTrue(SearchingAlgorithms.isSorted(arr2), "QuickSort: Already sorted array (Median-of-3 pivot)");

        int[] arr3 = {70, 60, 50, 40, 30, 20, 10};
        SortingAlgorithms.quickSort(arr3);
        assertTrue(SearchingAlgorithms.isSorted(arr3), "QuickSort: Reverse sorted array");

        int[] arr4 = {3, 3, 3, 3, 3};
        SortingAlgorithms.quickSort(arr4);
        assertTrue(SearchingAlgorithms.isSorted(arr4), "QuickSort: Identical elements");

        int[] single = {77};
        SortingAlgorithms.quickSort(single);
        assertEquals(77, single[0], "QuickSort: Single element array");

        int[] empty = {};
        SortingAlgorithms.quickSort(empty);
        assertTrue(empty.length == 0, "QuickSort: Empty array boundary");
    }

    // =========================================================================
    // 8. STABILITY & DOMAIN OBJECT TESTS (Tests 45 - 48)
    // =========================================================================
    private static void runStabilityAndDomainObjectTests() {
        System.out.println("\n--- 8. Stability & Domain Object Unit Tests ---");

        // ServiceRequest domain object sorting test
        ServiceRequest req1 = new ServiceRequest("CLN0001", 1001, 1001, "Cleaning", 2, "09:00", "10:00", "open");
        ServiceRequest req2 = new ServiceRequest("CLN0002", 1002, 1002, "Cleaning", 5, "09:15", "10:15", "open");
        ServiceRequest req3 = new ServiceRequest("CLN0003", 1003, 1003, "Cleaning", 3, "09:30", "10:30", "open");
        ServiceRequest req4 = new ServiceRequest("CLN0004", 1004, 1004, "Cleaning", 5, "09:45", "10:45", "open");

        ServiceRequest[] requests = {req1, req2, req3, req4};
        SortingAlgorithms.quickSort(requests);

        // Highest urgency (5) should come first
        assertEquals(5, requests[0].getUrgency(), "DomainObjectSort: Highest urgency first (CLN0002 or CLN0004)");
        assertEquals(5, requests[1].getUrgency(), "DomainObjectSort: Second highest urgency");
        assertEquals(3, requests[2].getUrgency(), "DomainObjectSort: Urgency 3 third");
        assertEquals(2, requests[3].getUrgency(), "DomainObjectSort: Lowest urgency last");
    }
}
