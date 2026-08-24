import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * M7: Graph route engine.
 *
 * Represents the local service network (locations + weighted roads) using
 * BOTH an adjacency list and an adjacency matrix, as required by Section 6
 * of the brief. Implements BFS, DFS, and Dijkstra, plus route reconstruction
 * from Dijkstra's predecessor array.
 *
 * Nodes are identified by a dense integer id in [0, numLocations).
 * Map your database locationId -> this dense index in your loader (M2),
 * and keep the reverse mapping so you can print real location names.
 */
public class Graph {

    /** One directed edge in the adjacency list. Roads are added both ways
     *  if the network is undirected (typical for a road network). */
    static class Edge {
        int to;
        long weight; // e.g. distance, travelTime, or roadConditionWeight
        Edge(int to, long weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    private final int numLocations;
    private final List<List<Edge>> adjList;
    private final long[][] adjMatrix; // 0 = no direct edge (use -1 if 0 is a valid weight)

    public Graph(int numLocations) {
        this.numLocations = numLocations;
        this.adjList = new ArrayList<>();
        for (int i = 0; i < numLocations; i++) adjList.add(new ArrayList<>());
        this.adjMatrix = new long[numLocations][numLocations];
    }

    public int size() {
        return numLocations;
    }

    /** Adds a road between two locations. Set undirected=true for two-way roads. */
    public void addEdge(int from, int to, long weight, boolean undirected) {
        adjList.get(from).add(new Edge(to, weight));
        adjMatrix[from][to] = weight;
        if (undirected) {
            adjList.get(to).add(new Edge(from, weight));
            adjMatrix[to][from] = weight;
        }
    }

    public List<Edge> neighbors(int locationId) {
        return adjList.get(locationId);
    }

    public long[][] getAdjMatrix() {
        return adjMatrix;
    }

    // ------------------------------------------------------------------
    // BFS — answers: "which locations are reachable from a dispatch point?"
    // ------------------------------------------------------------------

    /**
     * Returns the visit order for a breadth-first traversal from startId.
     * Also fills traceLog with one line per step for your trace table
     * (queue contents at each pop, in the order matching Section 6/10).
     */
    public List<Integer> bfs(int startId, List<String> traceLog) {
        boolean[] visited = new boolean[numLocations];
        List<Integer> order = new ArrayList<>();
        LinkedList<Integer> queue = new LinkedList<>(); // stand-in for your custom Queue (M3)

        visited[startId] = true;
        queue.add(startId);
        if (traceLog != null) traceLog.add("Enqueue " + startId + " | queue=" + queue);

        while (!queue.isEmpty()) {
            int current = queue.poll();
            order.add(current);
            if (traceLog != null) traceLog.add("Dequeue " + current + " -> visit | queue=" + queue);

            for (Edge e : adjList.get(current)) {
                if (!visited[e.to]) {
                    visited[e.to] = true;
                    queue.add(e.to);
                    if (traceLog != null) traceLog.add("  Enqueue " + e.to + " | queue=" + queue);
                }
            }
        }
        return order; // = every location reachable from startId
    }

    // ------------------------------------------------------------------
    // DFS — traversal order / connectivity check
    // ------------------------------------------------------------------

    public List<Integer> dfs(int startId, List<String> traceLog) {
        boolean[] visited = new boolean[numLocations];
        List<Integer> order = new ArrayList<>();
        java.util.Deque<Integer> stack = new java.util.ArrayDeque<>(); // stand-in for your custom Stack (M3)

        stack.push(startId);
        while (!stack.isEmpty()) {
            int current = stack.pop();
            if (visited[current]) continue;
            visited[current] = true;
            order.add(current);
            if (traceLog != null) traceLog.add("Visit " + current + " | stack=" + stack);

            // Push in reverse so lower-id neighbors are explored first (deterministic trace)
            List<Edge> neigh = adjList.get(current);
            for (int i = neigh.size() - 1; i >= 0; i--) {
                Edge e = neigh.get(i);
                if (!visited[e.to]) {
                    stack.push(e.to);
                    if (traceLog != null) traceLog.add("  Push " + e.to + " | stack=" + stack);
                }
            }
        }
        return order;
    }

    // ------------------------------------------------------------------
    // Dijkstra — answers: "fastest route between two locations?"
    // ------------------------------------------------------------------

    public static class DijkstraResult {
        public long[] distance;      // distance[v] = shortest distance from source to v
        public int[] predecessor;    // predecessor[v] = previous node on shortest path to v (-1 if none)
        public List<String> trace;   // step-by-step trace table rows
    }

    public static final long INFINITY = Long.MAX_VALUE / 2;

    public DijkstraResult dijkstra(int sourceId) {
        long[] distance = new long[numLocations];
        int[] predecessor = new int[numLocations];
        boolean[] finalized = new boolean[numLocations];
        java.util.Arrays.fill(distance, INFINITY);
        java.util.Arrays.fill(predecessor, -1);
        distance[sourceId] = 0;

        MinHeap heap = new MinHeap();
        heap.push(sourceId, 0);

        List<String> trace = new ArrayList<>();
        trace.add(String.format("Init: distance[%d]=0, all others=INF", sourceId));

        while (!heap.isEmpty()) {
            long[] top = heap.popMin();
            int u = (int) top[0];
            long d = top[1];

            if (finalized[u]) continue; // stale entry from decrease-key push, skip
            finalized[u] = true;
            trace.add(String.format("Finalize node %d with distance %d", u, d));

            for (Edge e : adjList.get(u)) {
                if (finalized[e.to]) continue;
                long candidate = distance[u] + e.weight;
                if (candidate < distance[e.to]) {
                    long old = distance[e.to];
                    distance[e.to] = candidate;
                    predecessor[e.to] = u;
                    heap.push(e.to, candidate);
                    trace.add(String.format("  Relax edge %d->%d: distance[%d] %s -> %d, predecessor=%d",
                            u, e.to, e.to, (old == INFINITY ? "INF" : String.valueOf(old)), candidate, u));
                }
            }
        }

        DijkstraResult result = new DijkstraResult();
        result.distance = distance;
        result.predecessor = predecessor;
        result.trace = trace;
        return result;
    }

    /** Reconstructs the shortest path from source to destination using the
     *  predecessor array produced by dijkstra(). Returns an empty list if
     *  destination is unreachable. This is your "route tracing" evidence. */
    public List<Integer> reconstructPath(int sourceId, int destId, int[] predecessor) {
        LinkedList<Integer> path = new LinkedList<>();
        int current = destId;
        while (current != -1) {
            path.addFirst(current);
            if (current == sourceId) break;
            current = predecessor[current];
        }
        if (path.isEmpty() || path.getFirst() != sourceId) {
            return new ArrayList<>(); // unreachable
        }
        return path;
    }
}
