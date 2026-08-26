import java.util.List;
import java.util.Map;

public class Dispatcher {

    private final PriorityQueueManager queueManager;
    private final RouteEngine routeEngine;
    private final int depotLocationIndex;

    public Dispatcher(PriorityQueueManager queueManager, RouteEngine routeEngine, int depotLocationIndex) {
        if (queueManager == null) {
            throw new IllegalArgumentException("Queue manager cannot be null");
        }
        if (routeEngine == null) {
            throw new IllegalArgumentException("Route engine cannot be null");
        }
        if (depotLocationIndex < 0) {
            throw new IllegalArgumentException("Depot location index cannot be negative");
        }

        this.queueManager = queueManager;
        this.routeEngine = routeEngine;
        this.depotLocationIndex = depotLocationIndex;
    }

    public DispatchResult dispatchNextRequest() {
        if (queueManager.isEmpty()) {
            throw new IllegalStateException("No requests to dispatch");
        }

        CleaningRequest request = queueManager.getNextRequest();
        request.setStatus(RequestStatus.ASSIGNED);

        Integer destIndex = routeEngine.resolveLocationIndex(request.getCampusLocation().getLocationId());
        if (destIndex == null) {
            return new DispatchResult(request, null, -1, "Location not found in routing graph");
        }

        RouteEngine.RouteEngineResult result = routeEngine.findShortestPath(depotLocationIndex);
        List<Integer> path = routeEngine.reconstructPath(depotLocationIndex, destIndex, result.predecessor());

        long travelTime = -1;
        if (!path.isEmpty()) {
            travelTime = result.distance()[destIndex];
        }

        String message = path.isEmpty() ? "Destination unreachable from depot" : "Route calculated successfully";

        return new DispatchResult(request, path, travelTime, message);
    }

    public CleaningRequest peekNextRequest() {
        if (queueManager.isEmpty()) {
            throw new IllegalStateException("No requests to peek");
        }
        return queueManager.peekNextRequest();
    }

    public boolean hasPendingRequests() {
        return !queueManager.isEmpty();
    }

    public static class DispatchResult {
        private final CleaningRequest request;
        private final List<Integer> path;
        private final long travelTimeMinutes;
        private final String message;

        public DispatchResult(CleaningRequest request, List<Integer> path, long travelTimeMinutes, String message) {
            this.request = request;
            this.path = path;
            this.travelTimeMinutes = travelTimeMinutes;
            this.message = message;
        }

        public CleaningRequest getRequest() {
            return request;
        }

        public List<Integer> getPath() {
            return path;
        }

        public long getTravelTimeMinutes() {
            return travelTimeMinutes;
        }

        public String getMessage() {
            return message;
        }

        public boolean isSuccessful() {
            return path != null && !path.isEmpty();
        }
    }
}
