import java.util.Arrays;

/**
 * Member 10 - Minimum Spanning Tree & Optimization
 *
 * Covers:
 * 1. Prim's Algorithm
 * 2. Kruskal's Algorithm
 * 3. Greedy Resource Selection
 * 4. 0/1 Knapsack using Dynamic Programming
 * 5. Greedy failure counterexample for 0/1 Knapsack
 *
 * No Java PriorityQueue, HashMap, TreeMap, Stack, ArrayDeque, etc.
 * are used for the assessed core logic.
 */
public class Member10Optimization {

    // ============================================================
    // EDGE
    // ============================================================
    static class Edge {
        int source;
        int destination;
        int weight;

        Edge(int source, int destination, int weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return source + " - " + destination + " (" + weight + ")";
        }
    }

    // ============================================================
    // PRIM'S ALGORITHM
    // ============================================================
    public static void primMST(int[][] graph) {
        int n = graph.length;

        int[] key = new int[n];
        int[] parent = new int[n];
        boolean[] inMST = new boolean[n];

        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        // Start from vertex 0
        key[0] = 0;

        int totalCost = 0;

        System.out.println("\n===== PRIM'S ALGORITHM =====");
        System.out.println("Selected edges:");

        for (int count = 0; count < n; count++) {

            // Find the vertex with the smallest key value
            int u = minKeyVertex(key, inMST);

            if (u == -1) {
                System.out.println("Graph is disconnected. MST cannot cover all vertices.");
                return;
            }

            inMST[u] = true;

            // Print the selected edge
            if (parent[u] != -1) {
                System.out.println(
                        parent[u] + " - " + u + " = " + graph[parent[u]][u]
                );
                totalCost += graph[parent[u]][u];
            }

            // Update adjacent vertices
            for (int v = 0; v < n; v++) {
                if (graph[u][v] > 0 && !inMST[v]
                        && graph[u][v] < key[v]) {
                    key[v] = graph[u][v];
                    parent[v] = u;
                }
            }
        }

        System.out.println("Total MST cost = " + totalCost);
    }

    private static int minKeyVertex(int[] key, boolean[] inMST) {
        int min = Integer.MAX_VALUE;
        int minIndex = -1;

        for (int v = 0; v < key.length; v++) {
            if (!inMST[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }

        return minIndex;
    }

    // ============================================================
    // KRUSKAL'S ALGORITHM
    // ============================================================
    public static void kruskalMST(int vertices, Edge[] edges) {
        // Make a copy so that the original edge array is not changed.
        Edge[] sortedEdges = Arrays.copyOf(edges, edges.length);

        // Sort edges by increasing weight using custom insertion sort.
        insertionSortEdges(sortedEdges);

        DisjointSet ds = new DisjointSet(vertices);

        int edgesUsed = 0;
        int totalCost = 0;

        System.out.println("\n===== KRUSKAL'S ALGORITHM =====");
        System.out.println("Selected edges:");

        for (Edge edge : sortedEdges) {
            int rootSource = ds.find(edge.source);
            int rootDestination = ds.find(edge.destination);

            // Add edge only if it does not create a cycle.
            if (rootSource != rootDestination) {
                ds.union(rootSource, rootDestination);

                System.out.println(edge);
                totalCost += edge.weight;
                edgesUsed++;

                if (edgesUsed == vertices - 1) {
                    break;
                }
            }
        }

        if (edgesUsed != vertices - 1) {
            System.out.println("Graph is disconnected. MST cannot be formed.");
        } else {
            System.out.println("Total MST cost = " + totalCost);
        }
    }

    private static void insertionSortEdges(Edge[] edges) {
        for (int i = 1; i < edges.length; i++) {
            Edge current = edges[i];
            int j = i - 1;

            while (j >= 0 && edges[j].weight > current.weight) {
                edges[j + 1] = edges[j];
                j--;
            }

            edges[j + 1] = current;
        }
    }

    // ============================================================
    // DISJOINT SET / UNION-FIND
    // Used by Kruskal's algorithm.
    // ============================================================
    static class DisjointSet {
        int[] parent;
        int[] rank;

        DisjointSet(int size) {
            parent = new int[size];
            rank = new int[size];

            for (int i = 0; i < size; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        int find(int x) {
            // Path compression
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            if (rootX == rootY) {
                return;
            }

            // Union by rank
            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }

    // ============================================================
    // GREEDY ALGORITHM
    // ============================================================
    /**
     * Greedy resource selection:
     *
     * Selects jobs according to value/weight ratio.
     * This is useful as a greedy strategy, but for 0/1 knapsack
     * it is not guaranteed to produce the optimal answer.
     */
    static class Item {
        String name;
        int weight;
        int value;

        Item(String name, int weight, int value) {
            this.name = name;
            this.weight = weight;
            this.value = value;
        }

        double ratio() {
            return (double) value / weight;
        }

        @Override
        public String toString() {
            return name + " (weight=" + weight
                    + ", value=" + value
                    + ", ratio=" + String.format("%.2f", ratio()) + ")";
        }
    }

    public static void greedyKnapsack(Item[] items, int capacity) {
        Item[] sorted = Arrays.copyOf(items, items.length);

        // Sort by value/weight ratio from highest to lowest.
        for (int i = 1; i < sorted.length; i++) {
            Item current = sorted[i];
            int j = i - 1;

            while (j >= 0 && sorted[j].ratio() < current.ratio()) {
                sorted[j + 1] = sorted[j];
                j--;
            }

            sorted[j + 1] = current;
        }

        int remaining = capacity;
        int totalValue = 0;

        System.out.println("\n===== GREEDY KNAPSACK =====");
        System.out.println("Capacity = " + capacity);
        System.out.println("Selection:");

        for (Item item : sorted) {
            if (item.weight <= remaining) {
                System.out.println("Selected: " + item);
                remaining -= item.weight;
                totalValue += item.value;
            }
        }

        System.out.println("Greedy total value = " + totalValue);
        System.out.println("Unused capacity = " + remaining);
    }

    // ============================================================
    // DYNAMIC PROGRAMMING - 0/1 KNAPSACK
    // ============================================================
    public static void dynamicProgrammingKnapsack(Item[] items, int capacity) {
        int n = items.length;

        int[][] dp = new int[n + 1][capacity + 1];

        // Build the DP table.
        for (int i = 1; i <= n; i++) {
            int weight = items[i - 1].weight;
            int value = items[i - 1].value;

            for (int w = 0; w <= capacity; w++) {

                // Item cannot fit.
                if (weight > w) {
                    dp[i][w] = dp[i - 1][w];
                } else {
                    // Maximum of:
                    // 1. Not taking item
                    // 2. Taking item
                    dp[i][w] = Math.max(
                            dp[i - 1][w],
                            value + dp[i - 1][w - weight]
                    );
                }
            }
        }

        System.out.println("\n===== DYNAMIC PROGRAMMING: 0/1 KNAPSACK =====");
        System.out.println("Capacity = " + capacity);

        // Print DP table.
        printDPTable(dp, items, capacity);

        // Reconstruct selected items.
        int w = capacity;
        int totalWeight = 0;

        System.out.println("\nSelected items:");

        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                Item item = items[i - 1];

                System.out.println("- " + item.name
                        + " (weight=" + item.weight
                        + ", value=" + item.value + ")");

                totalWeight += item.weight;
                w -= item.weight;
            }
        }

        System.out.println("Optimal value = " + dp[n][capacity]);
        System.out.println("Total weight = " + totalWeight);
        System.out.println("Unused capacity = " + (capacity - totalWeight));
    }

    private static void printDPTable(int[][] dp, Item[] items, int capacity) {
        System.out.println("\nDP Table:");

        System.out.print("Items/Capacity\t");
        for (int w = 0; w <= capacity; w++) {
            System.out.print(w + "\t");
        }
        System.out.println();

        for (int i = 0; i <= items.length; i++) {
            if (i == 0) {
                System.out.print("0\t\t");
            } else {
                System.out.print(items[i - 1].name + "\t\t");
            }

            for (int w = 0; w <= capacity; w++) {
                System.out.print(dp[i][w] + "\t");
            }
            System.out.println();
        }
    }

    // ============================================================
    // GREEDY FAILURE COUNTEREXAMPLE
    // ============================================================
    /**
     * Demonstrates why ratio-based greedy selection can fail
     * for the 0/1 knapsack problem.
     *
     * Capacity = 50
     *
     * Item A: weight 10, value 60  -> ratio 6.00
     * Item B: weight 20, value 100 -> ratio 5.00
     * Item C: weight 30, value 120 -> ratio 4.00
     *
     * Greedy chooses A + B = value 160.
     * Optimal DP chooses B + C = value 220.
     */
    public static void greedyFailureExample() {
        Item[] items = {
                new Item("A", 10, 60),
                new Item("B", 20, 100),
                new Item("C", 30, 120)
        };

        int capacity = 50;

        System.out.println("\n===== GREEDY FAILURE COUNTEREXAMPLE =====");

        greedyKnapsack(items, capacity);
        dynamicProgrammingKnapsack(items, capacity);

        System.out.println("\nConclusion:");
        System.out.println(
                "Greedy gives value 160, while Dynamic Programming gives 220."
        );
        System.out.println(
                "Therefore, ratio-based greedy is not always optimal for 0/1 knapsack."
        );
    }

    // ============================================================
    // MAIN DEMONSTRATION
    // ============================================================
    public static void main(String[] args) {

        /*
         * Ghana Smart Service Operations Optimizer example:
         *
         * 0 = Legon Gate
         * 1 = Balme Library
         * 2 = JQB
         * 3 = UGCS
         * 4 = Commonwealth Hall
         *
         * Edge weights represent route cost/distance.
         */

        int[][] campusGraph = {
                {0, 4, 2, 0, 0},
                {4, 0, 1, 5, 0},
                {2, 1, 0, 8, 10},
                {0, 5, 8, 0, 2},
                {0, 0, 10, 2, 0}
        };

        Edge[] edges = {
                new Edge(0, 1, 4),
                new Edge(0, 2, 2),
                new Edge(1, 2, 1),
                new Edge(1, 3, 5),
                new Edge(2, 3, 8),
                new Edge(2, 4, 10),
                new Edge(3, 4, 2)
        };

        // 1. Minimum Spanning Tree
        primMST(campusGraph);
        kruskalMST(campusGraph.length, edges);

        // 2. Greedy and Dynamic Programming
        Item[] serviceRequests = {
                new Item("Request-1", 10, 60),
                new Item("Request-2", 20, 100),
                new Item("Request-3", 30, 120)
        };

        greedyKnapsack(serviceRequests, 50);
        dynamicProgrammingKnapsack(serviceRequests, 50);

        // 3. Explicit greedy failure demonstration
        greedyFailureExample();
    }
}
