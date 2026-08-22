import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

class DispatcherTest {

    private PriorityQueueManager manager;
    private MockRouteEngine routeEngine;
    private Dispatcher dispatcher;

    @BeforeEach
    void setUp() {
        manager = new PriorityQueueManager();
        routeEngine = new MockRouteEngine();
        routeEngine.addLocation("UG-N-01", 1);
        routeEngine.addLocation("UG-E-01", 2);
        routeEngine.addLocation("UG-W-01", 3);
        dispatcher = new Dispatcher(manager, routeEngine, 0);
    }

    @Test
    void dispatchNextRequest_returnsHighestPriority() {
        CampusLocation loc1 = new CampusLocation("UG-N-01", "Medical Centre", "North", 5, 60);
        CampusLocation loc2 = new CampusLocation("UG-E-01", "Engineering Lab", "East", 4, 45);

        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc1, "Sanitization", Priority.HIGH, LocalDateTime.now()));
        manager.addRequest(new CleaningRequest("REQ-2", "Bob", loc2, "Lab", Priority.CRITICAL, LocalDateTime.now()));

        Dispatcher.DispatchResult result = dispatcher.dispatchNextRequest();
        assertTrue(result.isSuccessful());
        assertEquals("REQ-2", result.getRequest().getRequestId());
    }

    @Test
    void dispatchNextRequest_setsStatusAssigned() {
        CampusLocation loc = new CampusLocation("UG-N-01", "Medical Centre", "North", 5, 60);
        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc, "Sanitization", Priority.LOW, LocalDateTime.now()));

        Dispatcher.DispatchResult result = dispatcher.dispatchNextRequest();
        assertEquals(RequestStatus.ASSIGNED, result.getRequest().getStatus());
    }

    @Test
    void dispatchNextRequest_onEmpty_throwsException() {
        assertThrows(IllegalStateException.class, () -> dispatcher.dispatchNextRequest());
    }

    @Test
    void dispatchNextRequest_unknownLocation_returnsFailure() {
        CampusLocation loc = new CampusLocation("UNKNOWN", "Unknown Place", "Unknown", 1, 10);
        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc, "General", Priority.LOW, LocalDateTime.now()));

        Dispatcher.DispatchResult result = dispatcher.dispatchNextRequest();
        assertFalse(result.isSuccessful());
    }

    @Test
    void peekNextRequest_doesNotDispatch() {
        CampusLocation loc = new CampusLocation("UG-N-01", "Medical Centre", "North", 5, 60);
        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc, "Sanitization", Priority.LOW, LocalDateTime.now()));

        CleaningRequest peeked = dispatcher.peekNextRequest();
        assertEquals("REQ-1", peeked.getRequestId());
        assertTrue(dispatcher.hasPendingRequests());
    }

    @Test
    void hasPendingRequests_reflectsQueueState() {
        assertFalse(dispatcher.hasPendingRequests());

        CampusLocation loc = new CampusLocation("UG-N-01", "Medical Centre", "North", 5, 60);
        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc, "Sanitization", Priority.LOW, LocalDateTime.now()));

        assertTrue(dispatcher.hasPendingRequests());

        dispatcher.dispatchNextRequest();
        assertFalse(dispatcher.hasPendingRequests());
    }

    @Test
    void dispatchResult_hasCorrectTravelTime() {
        CampusLocation loc = new CampusLocation("UG-E-01", "Engineering Lab", "East", 4, 45);
        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc, "Lab", Priority.LOW, LocalDateTime.now()));

        Dispatcher.DispatchResult result = dispatcher.dispatchNextRequest();
        assertTrue(result.isSuccessful());
        assertEquals(5, result.getTravelTimeMinutes());
    }

    // Simple mock route engine for testing
    private static class MockRouteEngine implements RouteEngine {
        private final Map<String, Integer> locationMap = new java.util.HashMap<>();
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
            if (n > 1) { dist[1] = 10; pred[1] = 0; }
            if (n > 2) { dist[2] = 5; pred[2] = 0; }
            if (n > 3) { dist[3] = 18; pred[3] = 2; }
            return new RouteEngineResult(dist, pred);
        }

        @Override
        public List<Integer> reconstructPath(int sourceIndex, int destinationIndex, int[] predecessor) {
            List<Integer> path = new java.util.ArrayList<>();
            int current = destinationIndex;
            while (current != -1) {
                path.addFirst(current);
                if (current == sourceIndex) break;
                current = predecessor[current];
            }
            if (path.isEmpty() || path.getFirst() != sourceIndex) {
                return new java.util.ArrayList<>();
            }
            return path;
        }
    }
}
