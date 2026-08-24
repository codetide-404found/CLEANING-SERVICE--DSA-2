import java.util.List;
public class Main {

    static final String[] LOCATION_NAMES = {
        "Cleaning Depot",     // 0  <- crew starts here every day
        "Balme Library",      // 1  <- customer
        "Commonwealth Hall",  // 2  <- customer
        "Legon Hall",         // 3  <- customer
        "Night Market Block", // 4  <- customer
        "Bush Canteen",       // 5  <- customer
        "JQB (CS Dept.)",     // 6  <- customer
        "Sarbah Hall"         // 7  <- customer
    };

    public static void main(String[] args) {
        Graph graph = new Graph(LOCATION_NAMES.length);

        // fromId, toId, travelTime (minutes), undirected (two-way path)
        graph.addEdge(0, 1, 4, true);
        graph.addEdge(0, 2, 6, true);
        graph.addEdge(1, 6, 3, true);
        graph.addEdge(2, 3, 2, true);
        graph.addEdge(2, 7, 5, true);
        graph.addEdge(3, 4, 7, true);
        graph.addEdge(4, 5, 1, true);
        graph.addEdge(6, 4, 8, true);
        graph.addEdge(7, 5, 3, true);
        
        System.out.println("=== Adjacency Matrix (travel time in minutes) ===");
        printMatrix(graph.getAdjMatrix());

        System.out.println("\n=== BFS from the Depot: which customers can we reach? ===");
        java.util.List<String> bfsTrace = new java.util.ArrayList<>();
        List<Integer> bfsOrder = graph.bfs(0, bfsTrace);
        bfsTrace.forEach(System.out::println);
        System.out.println("Reachable customer locations: " + namesOf(bfsOrder));

        System.out.println("\n=== DFS from the Depot: full network exploration ===");
        java.util.List<String> dfsTrace = new java.util.ArrayList<>();
        List<Integer> dfsOrder = graph.dfs(0, dfsTrace);
        dfsTrace.forEach(System.out::println);
        System.out.println("DFS visit order: " + namesOf(dfsOrder));

        System.out.println("\n=== Dijkstra from the Depot: fastest route to every customer ===");
        Graph.DijkstraResult result = graph.dijkstra(0);
        result.trace.forEach(System.out::println);

        System.out.println("\n--- Distance / Predecessor table (this is your job-dispatch table) ---");
        System.out.printf("%-20s %-16s %-15s%n", "Customer Location", "Travel Time (min)", "Via");
        for (int i = 0; i < LOCATION_NAMES.length; i++) {
            String dist = result.distance[i] >= Graph.INFINITY ? "UNREACHABLE" : String.valueOf(result.distance[i]);
            String pred = result.predecessor[i] == -1 ? "-" : LOCATION_NAMES[result.predecessor[i]];
            System.out.printf("%-20s %-16s %-15s%n", LOCATION_NAMES[i], dist, pred);
        }

        // Simulate: dispatcher needs the crew to get to a booked job at Bush Canteen
        int customerJob = 5; // Bush Canteen
        List<Integer> path = graph.reconstructPath(0, customerJob, result.predecessor);
        System.out.println("\nDispatch route: Depot -> " + LOCATION_NAMES[customerJob] + ": " + namesOf(path)
                + " (total travel time = " + result.distance[customerJob] + " minutes)");

        // Edge case: a customer location that isn't connected to the depot at all
        // (e.g. a newly added building with no road mapped in yet)
        Graph disconnected = new Graph(3);
        disconnected.addEdge(0, 1, 5, true); // Depot -> one customer
        // node 2 (another customer) has no road mapped -> unreachable
        Graph.DijkstraResult dcResult = disconnected.dijkstra(0);
        List<Integer> unreachablePath = disconnected.reconstructPath(0, 2, dcResult.predecessor);
        System.out.println("\nEdge case (customer with no mapped road), Depot -> Customer 2: "
                + (unreachablePath.isEmpty() ? "UNREACHABLE (as expected - flag for dispatcher)" : unreachablePath));
    }

    static String namesOf(List<Integer> ids) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            sb.append(LOCATION_NAMES[ids.get(i)]);
            if (i < ids.size() - 1) sb.append(" -> ");
        }
        return sb.toString();
    }

    static void printMatrix(long[][] matrix) {
        System.out.printf("%-16s", "");
        for (String name : LOCATION_NAMES) System.out.printf("%-6s", name.substring(0, Math.min(4, name.length())));
        System.out.println();
        for (int i = 0; i < matrix.length; i++) {
            System.out.printf("%-16s", LOCATION_NAMES[i]);
            for (int j = 0; j < matrix.length; j++) {
                System.out.printf("%-6d", matrix[i][j]);
            }
            System.out.println();
        }
    }
}
