
import java.io.*;
import java.util.*;

/**
 * UG Campus Graph + Disjoint Set module.
 *
 * Core graph logic uses arrays and custom linked adjacency nodes.
 * No HashMap, TreeMap, PriorityQueue, ArrayDeque or Stack is used.
 */
public class GraphModule {

    public static final int INF = Integer.MAX_VALUE / 4;

    public static class Edge {
        public final int id, from, to, weight;
        public Edge(int id, int from, int to, int weight) {
            this.id = id; this.from = from; this.to = to; this.weight = weight;
        }
        public Edge reversed() { return new Edge(id, to, from, weight); }
        @Override public String toString() {
            return "E" + id + ": " + from + " - " + to + " (" + weight + "m)";
        }
    }

    static class AdjNode {
        int to, weight, edgeId;
        AdjNode next;
        AdjNode(int to, int weight, int edgeId, AdjNode next) {
            this.to = to; this.weight = weight; this.edgeId = edgeId; this.next = next;
        }
    }

    public static class Graph {
        private final int n;
        private final AdjNode[] adj;
        private final int[][] matrix;
        private final Edge[] edges;
        private int edgeCount = 0;

        public Graph(int n, int maxEdges) {
            this.n = n;
            this.adj = new AdjNode[n + 1];
            this.matrix = new int[n + 1][n + 1];
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) matrix[i][j] = (i == j) ? 0 : INF;
            }
            this.edges = new Edge[maxEdges];
        }

        public int size() { return n; }
        public void addUndirectedEdge(int id, int u, int v, int weight) {
            validateVertex(u); validateVertex(v);
            if (weight < 0) throw new IllegalArgumentException("Negative weight not allowed");
            edges[edgeCount++] = new Edge(id, u, v, weight);
            adj[u] = new AdjNode(v, weight, id, adj[u]);
            adj[v] = new AdjNode(u, weight, id, adj[v]);
            matrix[u][v] = Math.min(matrix[u][v], weight);
            matrix[v][u] = Math.min(matrix[v][u], weight);
        }

        public Edge[] getEdges() {
            Edge[] copy = new Edge[edgeCount];
            for (int i = 0; i < edgeCount; i++) copy[i] = edges[i];
            return copy;
        }

        public void printAdjacencyList() {
            for (int u = 1; u <= n; u++) {
                System.out.print(u + ": ");
                AdjNode p = adj[u];
                while (p != null) {
                    System.out.print(p.to + "(" + p.weight + "m) ");
                    p = p.next;
                }
                System.out.println();
            }
        }

        public void printAdjacencyMatrix() {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    System.out.print((matrix[i][j] >= INF ? "∞" : matrix[i][j]) + "\t");
                }
                System.out.println();
            }
        }

        public int[] bfs(int start) {
            validateVertex(start);
            int[] order = new int[n];
            boolean[] visited = new boolean[n + 1];
            int[] q = new int[n];
            int front = 0, rear = 0, count = 0;
            q[rear++] = start; visited[start] = true;
            while (front < rear) {
                int u = q[front++];
                order[count++] = u;
                for (AdjNode p = adj[u]; p != null; p = p.next) {
                    if (!visited[p.to]) {
                        visited[p.to] = true;
                        q[rear++] = p.to;
                    }
                }
            }
            int[] result = new int[count];
            for (int i = 0; i < count; i++) result[i] = order[i];
            return result;
        }

        public int[] dfs(int start) {
            validateVertex(start);
            int[] order = new int[n];
            boolean[] visited = new boolean[n + 1];
            int[] stack = new int[n * 2 + 10];
            int top = 0, count = 0;
            stack[top++] = start;
            while (top > 0) {
                int u = stack[--top];
                if (visited[u]) continue;
                visited[u] = true;
                order[count++] = u;
                for (AdjNode p = adj[u]; p != null; p = p.next) {
                    if (!visited[p.to]) stack[top++] = p.to;
                }
            }
            int[] result = new int[count];
            for (int i = 0; i < count; i++) result[i] = order[i];
            return result;
        }

        public DijkstraResult dijkstra(int source) {
            validateVertex(source);
            int[] dist = new int[n + 1];
            int[] prev = new int[n + 1];
            boolean[] done = new boolean[n + 1];
            for (int i = 1; i <= n; i++) { dist[i] = INF; prev[i] = -1; }
            dist[source] = 0;
            MinHeap heap = new MinHeap(n * 4 + 10);
            heap.insert(source, 0);

            while (!heap.isEmpty()) {
                HeapItem item = heap.extractMin();
                int u = item.vertex;
                if (done[u]) continue;
                done[u] = true;
                for (AdjNode p = adj[u]; p != null; p = p.next) {
                    if (dist[u] + p.weight < dist[p.to]) {
                        dist[p.to] = dist[u] + p.weight;
                        prev[p.to] = u;
                        heap.insert(p.to, dist[p.to]);
                    }
                }
            }
            return new DijkstraResult(dist, prev);
        }

        public MstResult kruskal() {
            Edge[] sorted = getEdges();
            mergeSort(sorted, 0, sorted.length - 1);
            DisjointSet ds = new DisjointSet(n);
            Edge[] chosen = new Edge[n - 1];
            int count = 0, total = 0;
            for (Edge e : sorted) {
                int a = ds.find(e.from), b = ds.find(e.to);
                if (a != b) {
                    ds.union(a, b);
                    chosen[count++] = e;
                    total += e.weight;
                    if (count == n - 1) break;
                }
            }
            return new MstResult(chosen, count, total, count == n - 1);
        }

        public MstResult prim(int start) {
            validateVertex(start);
            boolean[] inTree = new boolean[n + 1];
            int[] best = new int[n + 1];
            int[] parent = new int[n + 1];
            for (int i = 1; i <= n; i++) { best[i] = INF; parent[i] = -1; }
            best[start] = 0;
            MinHeap heap = new MinHeap(n * 4 + 10);
            heap.insert(start, 0);
            Edge[] chosen = new Edge[n - 1];
            int count = 0, total = 0;

            while (!heap.isEmpty()) {
                HeapItem item = heap.extractMin();
                int u = item.vertex;
                if (inTree[u]) continue;
                inTree[u] = true;
                if (parent[u] != -1) {
                    chosen[count++] = new Edge(-1, parent[u], u, best[u]);
                    total += best[u];
                }
                for (AdjNode p = adj[u]; p != null; p = p.next) {
                    if (!inTree[p.to] && p.weight < best[p.to]) {
                        best[p.to] = p.weight;
                        parent[p.to] = u;
                        heap.insert(p.to, p.weight);
                    }
                }
            }
            return new MstResult(chosen, count, total, count == n - 1);
        }

        private void validateVertex(int v) {
            if (v < 1 || v > n) throw new IllegalArgumentException("Invalid vertex: " + v);
        }

        private static void mergeSort(Edge[] a, int l, int r) {
            if (l >= r) return;
            int m = (l + r) / 2;
            mergeSort(a, l, m);
            mergeSort(a, m + 1, r);
            Edge[] tmp = new Edge[r - l + 1];
            int i = l, j = m + 1, k = 0;
            while (i <= m && j <= r) {
                if (a[i].weight <= a[j].weight) tmp[k++] = a[i++];
                else tmp[k++] = a[j++];
            }
            while (i <= m) tmp[k++] = a[i++];
            while (j <= r) tmp[k++] = a[j++];
            for (int t = 0; t < tmp.length; t++) a[l + t] = tmp[t];
        }
    }

    public static class DisjointSet {
        private final int[] parent;
        private final int[] rank;
        private final int[] size;

        public DisjointSet(int n) {
            parent = new int[n + 1];
            rank = new int[n + 1];
            size = new int[n + 1];
            makeSet();
        }

        public void makeSet() {
            for (int i = 0; i < parent.length; i++) {
                parent[i] = i; rank[i] = 0; size[i] = 1;
            }
        }

        public int find(int x) {
            if (x < 0 || x >= parent.length) throw new IllegalArgumentException("Invalid set element");
            if (parent[x] != x) parent[x] = find(parent[x]); // path compression
            return parent[x];
        }

        public void union(int a, int b) {
            int ra = find(a), rb = find(b);
            if (ra == rb) return;
            // union by rank; size is maintained for optional inspection
            if (rank[ra] < rank[rb]) {
                parent[ra] = rb; size[rb] += size[ra];
            } else if (rank[ra] > rank[rb]) {
                parent[rb] = ra; size[ra] += size[rb];
            } else {
                parent[rb] = ra; size[ra] += size[rb]; rank[ra]++;
            }
        }

        public int componentSize(int x) { return size[find(x)]; }
        public boolean connected(int a, int b) { return find(a) == find(b); }
    }

    static class HeapItem { int vertex, key; HeapItem(int v, int k){vertex=v;key=k;} }

    static class MinHeap {
        HeapItem[] heap; int size=0;
        MinHeap(int capacity){ heap=new HeapItem[Math.max(4,capacity+1)]; }
        boolean isEmpty(){ return size==0; }
        void insert(int v,int key){
            if(size+1==heap.length) grow();
            heap[++size]=new HeapItem(v,key);
            int i=size;
            while(i>1 && heap[i].key<heap[i/2].key){ swap(i,i/2); i/=2; }
        }
        HeapItem extractMin(){
            if(size==0) throw new IllegalStateException("Heap is empty");
            HeapItem min=heap[1]; heap[1]=heap[size--];
            int i=1;
            while(true){
                int l=i*2,r=l+1,small=i;
                if(l<=size && heap[l].key<heap[small].key) small=l;
                if(r<=size && heap[r].key<heap[small].key) small=r;
                if(small==i) break;
                swap(i,small); i=small;
            }
            return min;
        }
        void swap(int a,int b){ HeapItem t=heap[a];heap[a]=heap[b];heap[b]=t; }
        void grow(){ HeapItem[] b=new HeapItem[heap.length*2]; for(int i=0;i<heap.length;i++) b[i]=heap[i]; heap=b; }
    }

    public static class DijkstraResult {
        public final int[] distance, previous;
        DijkstraResult(int[] d,int[] p){distance=d;previous=p;}
        public int[] pathTo(int target){
            if(distance[target] >= INF) return new int[0];
            int count=0;
            for(int x=target;x!=-1;x=previous[x]) count++;
            int[] path=new int[count];
            int i=count-1;
            for(int x=target;x!=-1;x=previous[x]) path[i--]=x;
            return path;
        }
    }

    public static class MstResult {
        public final Edge[] edges; public final int count,totalWeight; public final boolean complete;
        MstResult(Edge[] e,int c,int t,boolean complete){edges=e;count=c;totalWeight=t;this.complete=complete;}
    }

    // Convenience loader for the supplied roads.csv: edgeId,fromLocationId,toLocationId,distance_m,...
    public static Graph loadRoadsCsv(String filename, int locationCount, int maxEdges) throws IOException {
        Graph g = new Graph(locationCount, maxEdges);
        BufferedReader br = new BufferedReader(new FileReader(filename));
        String line = br.readLine(); // header
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] p = line.split(",");
            int id = Integer.parseInt(p[0].trim());
            int u = Integer.parseInt(p[1].trim());
            int v = Integer.parseInt(p[2].trim());
            int distance = Integer.parseInt(p[3].trim());
            g.addUndirectedEdge(id,u,v,distance);
        }
        br.close();
        return g;
    }

    public static void main(String[] args) throws Exception {
        String csv = args.length > 0 ? args[0] : "data/roads.csv";
        Graph g = loadRoadsCsv(csv, 55, 200);

        System.out.println("UG Campus Graph loaded: " + g.size() + " locations.");
        System.out.println("BFS from Main Gate: " + Arrays.toString(g.bfs(1)));

        DijkstraResult d = g.dijkstra(1);
        System.out.println("Shortest distance Main Gate -> Computer Science (11): " + d.distance[11] + "m");
        System.out.println("Path: " + Arrays.toString(d.pathTo(11)));

        MstResult k = g.kruskal();
        System.out.println("Kruskal MST complete=" + k.complete + ", edges=" + k.count + ", total=" + k.totalWeight + "m");

        MstResult p = g.prim(1);
        System.out.println("Prim MST complete=" + p.complete + ", edges=" + p.count + ", total=" + p.totalWeight + "m");

        DisjointSet ds = new DisjointSet(55);
        ds.union(1,2);
        ds.union(2,3);
        System.out.println("Union-Find: connected(1,3)=" + ds.connected(1,3));
    }
}
