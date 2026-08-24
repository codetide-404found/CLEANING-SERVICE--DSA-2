
public class GraphModuleTest {
    static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
        System.out.println("PASS: " + message);
    }

    public static void main(String[] args) throws Exception {
        GraphModule.Graph g = GraphModule.loadRoadsCsv("data/roads.csv", 55, 200);

        int[] bfs = g.bfs(1);
        check(bfs.length == 55, "BFS reaches all 55 locations");

        int[] dfs = g.dfs(1);
        check(dfs.length == 55, "DFS reaches all 55 locations");

        GraphModule.DijkstraResult d = g.dijkstra(1);
        check(d.distance[1] == 0, "Dijkstra source distance is zero");
        check(d.distance[11] < GraphModule.INF, "Computer Science is reachable from Main Gate");
        check(d.pathTo(11).length > 0, "Dijkstra reconstructs a path");

        GraphModule.MstResult k = g.kruskal();
        check(k.complete, "Kruskal produces a spanning tree");
        check(k.count == 54, "Kruskal selects V-1 edges");

        GraphModule.MstResult p = g.prim(1);
        check(p.complete, "Prim produces a spanning tree");
        check(p.count == 54, "Prim selects V-1 edges");
        check(p.totalWeight == k.totalWeight, "Prim and Kruskal have the same MST cost");

        GraphModule.DisjointSet ds = new GraphModule.DisjointSet(5);
        check(ds.find(1) == 1, "Initial representative is itself");
        ds.union(1,2);
        ds.union(2,3);
        check(ds.connected(1,3), "Union connects components");
        check(!ds.connected(1,4), "Separate components remain separate");
        ds.union(3,4);
        check(ds.connected(1,4), "Second union joins components");
        check(ds.componentSize(1) == 4, "Component size is maintained");

        boolean caught = false;
        try { g.dijkstra(0); } catch (IllegalArgumentException e) { caught = true; }
        check(caught, "Invalid source vertex is rejected");

        caught = false;
        try { ds.find(99); } catch (IllegalArgumentException e) { caught = true; }
        check(caught, "Invalid disjoint-set element is rejected");

        System.out.println("All graph/disjoint-set tests passed.");
    }
}
