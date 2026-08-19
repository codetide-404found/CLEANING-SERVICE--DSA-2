# Member 9 — Graph Route Engine (Campus Cleaning Service)

This module answers three routing questions for the cleaning service's
dispatch system:

1. Which customer locations can a crew actually reach from the depot? (BFS)
2. Is the whole campus road network connected, or are parts cut off? (DFS)
3. What's the fastest route from the depot to a specific customer, and what
   is the exact turn-by-turn path? (Dijkstra + route tracing)

## Files

| File | Purpose |
| --- | --- |
| `Graph.java` | The core data structure. Stores campus locations and roads as **both** an adjacency list and an adjacency matrix (Section 6). Implements `bfs()`, `dfs()`, `dijkstra()`, and `reconstructPath()` (route tracing). Each traversal method can log a step-by-step trace, used as evidence for the trace tables. |
| `MinHeap.java` | A custom binary min-heap used by `dijkstra()` to always pick the closest unvisited location next. Built from scratch so Dijkstra does not depend on `java.util.PriorityQueue`, per the brief's rule against built-in structures for assessed core logic. |
| `Main.java` | Demo / smoke test. Builds a small sample network (1 depot + 7 customer locations) and runs BFS, DFS, and Dijkstra against it, printing the adjacency matrix, trace logs, the distance/predecessor ("job-dispatch") table, a sample dispatch route, and an unreachable-customer edge case. |
| `README.md` | This file. |

## How to run it

You need a Java Development Kit (JDK 17+) installed — `javac` must be
available, not just `java`.

**Option A — compile then run (recommended):**

```bash
javac *.java
java Main
```

**Option B — single-file launch (no separate compile step, JDK 11+):**

```bash
java Main.java
```

Both options print the same output: the adjacency matrix, BFS trace, DFS
trace, Dijkstra's step-by-step relaxation log, the job-dispatch table, a
sample route reconstruction, and the disconnected-location edge case.

## Note on AI assistance

Parts of this module — the initial Java implementation (`Graph.java`,
`MinHeap.java`, `Main.java`), the trace table template, and this README —
were drafted with the help of  AI , based on
the project brief and the cleaning-service scenario. The code was test-run
in a sandboxed environment to confirm it compiles and produces correct BFS,
DFS, and Dijkstra output before being shared.
