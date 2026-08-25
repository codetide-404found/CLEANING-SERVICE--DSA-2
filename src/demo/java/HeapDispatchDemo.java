import java.time.LocalDateTime;
import java.util.*;

/**
 * Manual test runner and demo for Member 5's Heap & Priority Queue Dispatch module.
 *
 * This class exercises all key operations and prints pass/fail results.
 * Run with: java HeapDispatchDemo
 */
public class HeapDispatchDemo {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=== Member 5: Heap & Priority Queue Dispatch Demo ===\n");

        testEmptyHeap();
        testSingleElement();
        testMultipleInsertion();
        testPeek();
        testExtractHighestPriority();
        testEqualPriorityTieBreaking();
        testUpdatePriority();
        testDuplicateRequestId();
        testInvalidPriority();
        testUpdateNonexistentRequest();
        testDispatcherIntegration();
        testComplexScenario();

        System.out.println("\n=== SUMMARY ===");
        System.out.println("Passed: " + testsPassed);
        System.out.println("Failed: " + testsFailed);
        if (testsFailed == 0) {
            System.out.println("All tests passed!");
        } else {
            System.out.println("Some tests failed!");
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (condition) {
            testsPassed++;
            System.out.println("  [PASS] " + message);
        } else {
            testsFailed++;
            System.out.println("  [FAIL] " + message);
        }
    }

    private static void assertEquals(Object expected, Object actual, String message) {
        if (Objects.equals(expected, actual)) {
            testsPassed++;
            System.out.println("  [PASS] " + message);
        } else {
            testsFailed++;
            System.out.println("  [FAIL] " + message + " (expected: " + expected + ", got: " + actual + ")");
        }
    }

    private static void assertThrows(Runnable action, String message) {
        try {
            action.run();
            testsFailed++;
            System.out.println("  [FAIL] " + message + " (expected exception, but none thrown)");
        } catch (Exception e) {
            testsPassed++;
            System.out.println("  [PASS] " + message + " (threw " + e.getClass().getSimpleName() + ")");
        }
    }

    // ============================================================
    // TEST CASES
    // ============================================================

    private static void testEmptyHeap() {
        System.out.println("--- Empty Heap Tests ---");
        PriorityQueueManager manager = new PriorityQueueManager();

        assertTrue(manager.isEmpty(), "New manager is empty");
        assertEquals(0, manager.size(), "New manager has size 0");

        assertThrows(() -> manager.peekNextRequest(), "peek on empty throws");
        assertThrows(() -> manager.getNextRequest(), "extract on empty throws");
    }

    private static void testSingleElement() {
        System.out.println("\n--- Single Element Tests ---");
        PriorityQueueManager manager = new PriorityQueueManager();
        CampusLocation loc = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        CleaningRequest req = new CleaningRequest(
                "REQ-1", "Alice", loc, "Sanitization", Priority.CRITICAL,
                LocalDateTime.of(2025, 1, 1, 10, 0)
        );

        manager.addRequest(req);

        assertTrue(!manager.isEmpty(), "Manager not empty after insert");
        assertEquals(1, manager.size(), "Size is 1 after insert");
        assertEquals(req, manager.peekNextRequest(), "Peek returns the request");
        assertEquals(req, manager.getNextRequest(), "Extract returns the request");
        assertTrue(manager.isEmpty(), "Manager empty after extraction");
        assertEquals(0, manager.size(), "Size is 0 after extraction");
    }

    private static void testMultipleInsertion() {
        System.out.println("\n--- Multiple Insertion Tests ---");
        PriorityQueueManager manager = new PriorityQueueManager();
        CampusLocation loc1 = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        CampusLocation loc2 = new CampusLocation("LOC-2", "Engineering Lab", "East", 4, 45);
        CampusLocation loc3 = new CampusLocation("LOC-3", "Hostel B", "South", 3, 30);
        CampusLocation loc4 = new CampusLocation("LOC-4", "Admin Block", "Central", 3, 25);
        CampusLocation loc5 = new CampusLocation("LOC-5", "Library", "West", 2, 20);

        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc1, "Sanitization", Priority.CRITICAL, LocalDateTime.of(2025, 1, 1, 10, 0)));
        manager.addRequest(new CleaningRequest("REQ-2", "Bob", loc2, "Lab Cleaning", Priority.HIGH, LocalDateTime.of(2025, 1, 1, 10, 5)));
        manager.addRequest(new CleaningRequest("REQ-3", "Carol", loc3, "Room Cleaning", Priority.MEDIUM, LocalDateTime.of(2025, 1, 1, 10, 10)));
        manager.addRequest(new CleaningRequest("REQ-4", "Dave", loc4, "Office Cleaning", Priority.MEDIUM, LocalDateTime.of(2025, 1, 1, 9, 0)));
        manager.addRequest(new CleaningRequest("REQ-5", "Eve", loc5, "General", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 15)));

        assertEquals(5, manager.size(), "Size is 5 after inserting 5 requests");
        assertTrue(manager.validateHeapProperty(), "Heap property maintained after insertion");
    }

    private static void testPeek() {
        System.out.println("\n--- Peek Tests ---");
        PriorityQueueManager manager = new PriorityQueueManager();
        CampusLocation loc1 = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        CampusLocation loc2 = new CampusLocation("LOC-2", "Engineering Lab", "East", 4, 45);

        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc1, "Sanitization", Priority.HIGH, LocalDateTime.of(2025, 1, 1, 10, 0)));
        manager.addRequest(new CleaningRequest("REQ-2", "Bob", loc2, "Lab Cleaning", Priority.CRITICAL, LocalDateTime.of(2025, 1, 1, 10, 5)));

        CleaningRequest peeked = manager.peekNextRequest();
        assertEquals("REQ-2", peeked.getRequestId(), "Peek returns CRITICAL request");
        assertEquals(2, manager.size(), "Peek does not remove element");
    }

    private static void testExtractHighestPriority() {
        System.out.println("\n--- Extract Highest Priority Tests ---");
        PriorityQueueManager manager = new PriorityQueueManager();
        CampusLocation loc1 = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        CampusLocation loc2 = new CampusLocation("LOC-2", "Engineering Lab", "East", 4, 45);
        CampusLocation loc3 = new CampusLocation("LOC-3", "Hostel B", "South", 3, 30);
        CampusLocation loc4 = new CampusLocation("LOC-4", "Library", "West", 2, 20);

        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc1, "Sanitization", Priority.CRITICAL, LocalDateTime.of(2025, 1, 1, 10, 0)));
        manager.addRequest(new CleaningRequest("REQ-2", "Bob", loc2, "Lab Cleaning", Priority.HIGH, LocalDateTime.of(2025, 1, 1, 10, 5)));
        manager.addRequest(new CleaningRequest("REQ-3", "Carol", loc3, "Room Cleaning", Priority.MEDIUM, LocalDateTime.of(2025, 1, 1, 10, 10)));
        manager.addRequest(new CleaningRequest("REQ-4", "Dave", loc4, "General", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 15)));

        assertTrue(manager.validateHeapProperty(), "Heap property valid before extraction");

        CleaningRequest first = manager.getNextRequest();
        assertEquals("REQ-1", first.getRequestId(), "First extract is CRITICAL");
        assertEquals(3, manager.size(), "Size decremented after extract");
        assertTrue(manager.validateHeapProperty(), "Heap property valid after first extraction");

        CleaningRequest second = manager.getNextRequest();
        assertEquals("REQ-2", second.getRequestId(), "Second extract is HIGH");
        assertEquals(2, manager.size(), "Size decremented after second extract");

        CleaningRequest third = manager.getNextRequest();
        assertEquals("REQ-3", third.getRequestId(), "Third extract is MEDIUM");
        assertEquals(1, manager.size(), "Size decremented after third extract");

        CleaningRequest fourth = manager.getNextRequest();
        assertEquals("REQ-4", fourth.getRequestId(), "Fourth extract is LOW");
        assertTrue(manager.isEmpty(), "Manager empty after all extractions");
    }

    private static void testEqualPriorityTieBreaking() {
        System.out.println("\n--- Equal Priority Tie-Breaking Tests ---");
        PriorityQueueManager manager = new PriorityQueueManager();
        CampusLocation loc1 = new CampusLocation("LOC-1", "Hostel B", "South", 3, 30);
        CampusLocation loc2 = new CampusLocation("LOC-2", "Admin Block", "Central", 3, 25);
        CampusLocation loc3 = new CampusLocation("LOC-3", "Another Medium", "East", 3, 20);

        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc1, "Room", Priority.MEDIUM, LocalDateTime.of(2025, 1, 1, 10, 10)));
        manager.addRequest(new CleaningRequest("REQ-2", "Bob", loc2, "Office", Priority.MEDIUM, LocalDateTime.of(2025, 1, 1, 9, 0)));
        manager.addRequest(new CleaningRequest("REQ-3", "Carol", loc3, "Room", Priority.MEDIUM, LocalDateTime.of(2025, 1, 1, 9, 30)));

        // Tie-breaking: earlier request time first, then request ID
        CleaningRequest first = manager.getNextRequest();
        assertEquals("REQ-2", first.getRequestId(), "Earlier request (09:00) extracted first");

        CleaningRequest second = manager.getNextRequest();
        assertEquals("REQ-3", second.getRequestId(), "Second earlier request (09:30) extracted second");

        CleaningRequest third = manager.getNextRequest();
        assertEquals("REQ-1", third.getRequestId(), "Latest request (10:10) extracted last");
    }

    private static void testUpdatePriority() {
        System.out.println("\n--- Update Priority Tests ---");
        PriorityQueueManager manager = new PriorityQueueManager();
        CampusLocation loc1 = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        CampusLocation loc2 = new CampusLocation("LOC-2", "Engineering Lab", "East", 4, 45);
        CampusLocation loc3 = new CampusLocation("LOC-3", "Library", "West", 2, 20);

        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc1, "Sanitization", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 0)));
        manager.addRequest(new CleaningRequest("REQ-2", "Bob", loc2, "Lab", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 5)));
        manager.addRequest(new CleaningRequest("REQ-3", "Carol", loc3, "General", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 10)));

        assertTrue(manager.validateHeapProperty(), "Heap property valid before update");

        // Upgrade REQ-1 from LOW to CRITICAL
        boolean updated = manager.updateRequestPriority("REQ-1", Priority.CRITICAL);
        assertTrue(updated, "Update returns true for existing request");
        assertTrue(manager.validateHeapProperty(), "Heap property valid after upgrade");

        CleaningRequest top = manager.peekNextRequest();
        assertEquals("REQ-1", top.getRequestId(), "Updated request is now highest priority");
        assertEquals(Priority.CRITICAL, top.getPriority(), "Updated request has CRITICAL priority");

        // Downgrade REQ-1 from CRITICAL to MEDIUM
        updated = manager.updateRequestPriority("REQ-1", Priority.MEDIUM);
        assertTrue(updated, "Update returns true for downgrade");
        assertTrue(manager.validateHeapProperty(), "Heap property valid after downgrade");

        top = manager.peekNextRequest();
        assertEquals(Priority.MEDIUM, top.getPriority(), "After downgrade to MEDIUM, MEDIUM is highest");
    }

    private static void testDuplicateRequestId() {
        System.out.println("\n--- Duplicate Request ID Tests ---");
        PriorityQueueManager manager = new PriorityQueueManager();
        CampusLocation loc = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);

        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc, "Sanitization", Priority.CRITICAL, LocalDateTime.of(2025, 1, 1, 10, 0)));

        assertThrows(() -> manager.addRequest(
                new CleaningRequest("REQ-1", "Bob", loc, "Cleaning", Priority.HIGH, LocalDateTime.of(2025, 1, 1, 11, 0))),
                "Duplicate request ID throws exception");
    }

    private static void testInvalidPriority() {
        System.out.println("\n--- Invalid Priority Tests ---");
        CampusLocation loc = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);

        assertThrows(() -> new CleaningRequest("REQ-1", "Alice", loc, "Sanitization", null, LocalDateTime.now()),
                "Null priority in constructor throws");
    }

    private static void testUpdateNonexistentRequest() {
        System.out.println("\n--- Update Nonexistent Request Tests ---");
        PriorityQueueManager manager = new PriorityQueueManager();
        CampusLocation loc = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);

        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc, "Sanitization", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 0)));

        boolean updated = manager.updateRequestPriority("REQ-999", Priority.CRITICAL);
        assertTrue(!updated, "Updating nonexistent request returns false");
    }

    private static void testDispatcherIntegration() {
        System.out.println("\n--- Dispatcher Integration Tests ---");

        // Mock route engine that simulates simple routing
        MockRouteEngine routeEngine = new MockRouteEngine();
        routeEngine.addLocation("UG-N-01", 1);   // Medical Centre
        routeEngine.addLocation("UG-E-01", 2);   // Engineering Lab
        routeEngine.addLocation("UG-W-01", 3);   // Library

        PriorityQueueManager manager = new PriorityQueueManager();
        CampusLocation loc1 = new CampusLocation("UG-N-01", "Medical Centre", "North", 5, 60);
        CampusLocation loc2 = new CampusLocation("UG-E-01", "Engineering Lab", "East", 4, 45);
        CampusLocation loc3 = new CampusLocation("UG-W-01", "Library", "West", 2, 20);

        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc1, "Sanitization", Priority.HIGH, LocalDateTime.of(2025, 1, 1, 10, 0)));
        manager.addRequest(new CleaningRequest("REQ-2", "Bob", loc2, "Lab", Priority.CRITICAL, LocalDateTime.of(2025, 1, 1, 10, 5)));
        manager.addRequest(new CleaningRequest("REQ-3", "Carol", loc3, "General", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 10)));

        Dispatcher dispatcher = new Dispatcher(manager, routeEngine, 0);

        assertTrue(dispatcher.hasPendingRequests(), "Dispatcher has pending requests");

        Dispatcher.DispatchResult result = dispatcher.dispatchNextRequest();
        assertTrue(result.isSuccessful(), "Dispatch result is successful");
        assertEquals("REQ-2", result.getRequest().getRequestId(), "CRITICAL request dispatched first");
        assertTrue(!result.getPath().isEmpty(), "Path is not empty");

        CleaningRequest peeked = dispatcher.peekNextRequest();
        assertEquals("REQ-1", peeked.getRequestId(), "Next pending is HIGH request");

        result = dispatcher.dispatchNextRequest();
        assertEquals("REQ-1", result.getRequest().getRequestId(), "Next dispatched is HIGH request");
    }

    private static void testComplexScenario() {
        System.out.println("\n--- Complex Scenario (Example Data) ---");
        PriorityQueueManager manager = new PriorityQueueManager();

        // Example data from role report
        CampusLocation medical = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        CampusLocation engineering = new CampusLocation("LOC-2", "Engineering Lab", "East", 4, 45);
        CampusLocation hostel = new CampusLocation("LOC-3", "Hostel B", "South", 3, 30);
        CampusLocation admin = new CampusLocation("LOC-4", "Admin Block", "Central", 3, 25);
        CampusLocation library = new CampusLocation("LOC-5", "Library", "West", 2, 20);

        manager.addRequest(new CleaningRequest("R1", "Alice", medical, "Sanitization", Priority.CRITICAL, LocalDateTime.of(2025, 1, 1, 10, 0)));
        manager.addRequest(new CleaningRequest("R2", "Bob", engineering, "Lab", Priority.HIGH, LocalDateTime.of(2025, 1, 1, 10, 5)));
        manager.addRequest(new CleaningRequest("R3", "Carol", hostel, "Room", Priority.MEDIUM, LocalDateTime.of(2025, 1, 1, 10, 10)));
        manager.addRequest(new CleaningRequest("R4", "Dave", admin, "Office", Priority.MEDIUM, LocalDateTime.of(2025, 1, 1, 9, 0)));
        manager.addRequest(new CleaningRequest("R5", "Eve", library, "General", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 15)));

        System.out.println("  Expected order: Medical Centre -> Engineering Lab -> Admin Block -> Hostel B -> Library");

        String[] expectedOrder = {"R1", "R2", "R4", "R3", "R5"};
        boolean correct = true;
        for (int i = 0; i < expectedOrder.length; i++) {
            if (manager.isEmpty()) {
                correct = false;
                break;
            }
            CleaningRequest next = manager.getNextRequest();
            if (!expectedOrder[i].equals(next.getRequestId())) {
                correct = false;
                System.out.println("  [FAIL] Step " + (i + 1) + ": expected " + expectedOrder[i] + ", got " + next.getRequestId());
                testsFailed++;
                break;
            }
        }
        if (correct) {
            System.out.println("  [PASS] Dispatch order matches expected: R1, R2, R4, R3, R5");
            testsPassed++;
        }
    }

    // ============================================================
    // MOCK ROUTE ENGINE for testing Dispatcher without Graph
    // ============================================================

    private static class MockRouteEngine implements RouteEngine {
        private final Map<String, Integer> locationMap = new HashMap<>();
        private int nextIndex = 0;

        void addLocation(String locationId, int graphIndex) {
            locationMap.put(locationId, graphIndex);
            if (graphIndex >= nextIndex) {
                nextIndex = graphIndex + 1;
            }
        }

        @Override
        public int resolveLocationIndex(String locationId) {
            return locationMap.getOrDefault(locationId, -1);
        }

        @Override
        public RouteEngineResult findShortestPath(int sourceIndex) {
            int n = nextIndex;
            long[] dist = new long[n];
            int[] pred = new int[n];
            java.util.Arrays.fill(dist, Long.MAX_VALUE / 2);
            java.util.Arrays.fill(pred, -1);
            dist[sourceIndex] = 0;

            // Simple mock: predefined distances for test data
            if (sourceIndex == 0) {
                if (n > 1) dist[1] = 10;
                if (n > 2) dist[2] = 5;
                if (n > 3) dist[3] = 18;
                pred[1] = 0;
                pred[2] = 0;
                pred[3] = 2;
            }

            return new RouteEngineResult(dist, pred);
        }

        @Override
        public List<Integer> reconstructPath(int sourceIndex, int destinationIndex, int[] predecessor) {
            List<Integer> path = new ArrayList<>();
            int current = destinationIndex;
            while (current != -1) {
                path.addFirst(current);
                if (current == sourceIndex) break;
                current = predecessor[current];
            }
            if (path.isEmpty() || path.getFirst() != sourceIndex) {
                return new ArrayList<>();
            }
            return path;
        }
    }
}
