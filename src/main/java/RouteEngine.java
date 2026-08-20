import java.util.List;

public interface RouteEngine {
    int resolveLocationIndex(String locationId);
    RouteEngineResult findShortestPath(int sourceIndex);
    List<Integer> reconstructPath(int sourceIndex, int destinationIndex, int[] predecessor);

    public record RouteEngineResult(long[] distance, int[] predecessor) {
    }
}
