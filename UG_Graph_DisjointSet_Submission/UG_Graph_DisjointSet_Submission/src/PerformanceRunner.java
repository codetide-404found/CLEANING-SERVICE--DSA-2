
import java.io.*;
import java.util.Random;

public class PerformanceRunner {
    static class Result {
        int n, m;
        long bfsNs, dfsNs, dijkstraNs, kruskalNs, primNs;
        Result(int n,int m,long b,long d,long dj,long k,long p){this.n=n;this.m=m;bfsNs=b;dfsNs=d;dijkstraNs=dj;kruskalNs=k;primNs=p;}
    }

    static GraphModule.Graph makeGraph(int n, int extraEdges, long seed) {
        int maxEdges = n + extraEdges + 20;
        GraphModule.Graph g = new GraphModule.Graph(n, maxEdges);
        int eid = 1;
        // Ensure connectivity with a chain
        for (int i=1;i<n;i++) {
            g.addUndirectedEdge(eid++, i, i+1, 50 + (i*37)%250);
        }
        Random r = new Random(seed);
        int target = n + extraEdges;
        while (eid <= target) {
            int u=1+r.nextInt(n), v=1+r.nextInt(n);
            if (u==v) continue;
            int w=50+r.nextInt(450);
            try { g.addUndirectedEdge(eid++,u,v,w); }
            catch (Exception ignored) {}
        }
        return g;
    }

    static long avgBfs(GraphModule.Graph g,int reps){
        long total=0; for(int i=0;i<reps;i++){long s=System.nanoTime();g.bfs(1);total+=System.nanoTime()-s;}return total/reps;
    }
    static long avgDfs(GraphModule.Graph g,int reps){
        long total=0; for(int i=0;i<reps;i++){long s=System.nanoTime();g.dfs(1);total+=System.nanoTime()-s;}return total/reps;
    }
    static long avgDijkstra(GraphModule.Graph g,int reps){
        long total=0; for(int i=0;i<reps;i++){long s=System.nanoTime();g.dijkstra(1);total+=System.nanoTime()-s;}return total/reps;
    }
    static long avgKruskal(GraphModule.Graph g,int reps){
        long total=0; for(int i=0;i<reps;i++){long s=System.nanoTime();g.kruskal();total+=System.nanoTime()-s;}return total/reps;
    }
    static long avgPrim(GraphModule.Graph g,int reps){
        long total=0; for(int i=0;i<reps;i++){long s=System.nanoTime();g.prim(1);total+=System.nanoTime()-s;}return total/reps;
    }

    public static void main(String[] args) throws Exception {
        int[] sizes={50,100,200,500};
        int repetitions=3;
        PrintWriter out=new PrintWriter(new FileWriter("performance/graph_runtime.csv"));
        out.println("inputSize,edgeCount,algorithm,averageTimeNs,repetitions");
        for(int n:sizes){
            int extra=Math.max(n, n*2);
            GraphModule.Graph g=makeGraph(n,extra,20260807L+n);
            out.printf("%d,%d,BFS,%d,%d%n",n,n+extra,avgBfs(g,repetitions),repetitions);
            out.printf("%d,%d,DFS,%d,%d%n",n,n+extra,avgDfs(g,repetitions),repetitions);
            out.printf("%d,%d,Dijkstra,%d,%d%n",n,n+extra,avgDijkstra(g,repetitions),repetitions);
            out.printf("%d,%d,Kruskal,%d,%d%n",n,n+extra,avgKruskal(g,repetitions),repetitions);
            out.printf("%d,%d,Prim,%d,%d%n",n,n+extra,avgPrim(g,repetitions),repetitions);
        }
        out.close();
        System.out.println("Wrote performance/graph_runtime.csv");
    }
}
