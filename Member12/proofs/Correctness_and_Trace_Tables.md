# Member 12: Algorithmic Correctness Evidence & Trace Tables

**Context:** Campus Cleaning Service Operations (Ghana Smart Service Operations Optimizer)

---

## 1. Algorithmic Trace Tables

### Trace Table 1: Dijkstra Shortest Path (Campus Zone Routing)
* **Goal:** Calculate shortest travel paths between cleaning supply hubs and hostel blocks.
* **Nodes:** A (Central Stores), B (Volta Hall), C (Legon Hall), D (Akuafo Hall).

| Step | Current Node | Unvisited Set | Distance Array [A, B, C, D] | Predecessor Array | Action / Edge Relaxed |
| :--- | :--- | :--- | :--- | :--- | :--- |
| 0 | - | {A, B, C, D} | [0, ∞, ∞, ∞] | [null, null, null, null] | Initialize starting node A |
| 1 | A | {B, C, D} | [0, 4, 2, ∞] | [A, A, A, null] | Relax edges (A,B)=4, (A,C)=2 |
| 2 | C | {B, D} | [0, 3, 2, 7] | [A, C, A, C] | Pick C; relax edge (C,B)=1 (dist=3), (C,D)=5 (dist=7) |
| 3 | B | {D} | [0, 3, 2, 6] | [A, C, A, B] | Pick B; relax edge (B,D)=3 (dist=6) |
| 4 | D | {} | [0, 3, 2, 6] | [A, C, A, B] | Pick D; all nodes visited |

---

### Trace Table 2: Binary Search (Cleaning Request Priority Lookup)
* **Target:** Request ID 305 in sorted request array `[101, 204, 305, 412, 550, 601]`.

| Iteration | Low | High | Mid | Array[Mid] | Comparison | Next Action |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| 1 | 0 | 5 | 2 | 305 | 305 == 305 | Target found at Index 2 |

---

## 2. Mathematical Proof Sketches

### Proof Sketch 1: Loop Invariant for Insertion Sort (Cleaning Emergency Logs)
* **Initialization:** Before the first iteration ($i = 1$), the subarray $A[0 \dots i-1]$ consists of a single element, which is trivially sorted.
* **Maintenance:** In each iteration, the key $A[i]$ is compared against elements in $A[0 \dots i-1]$. Elements greater than $A[i]$ are shifted right by one position. Inserting $A[i]$ into its correct position maintains the invariant that $A[0 \dots i]$ remains sorted.
* **Termination:** The loop terminates when $i = n$. By the invariant, $A[0 \dots n-1]$ contains all original elements in fully sorted order.

---

## 3. Counterexamples & Edge Cases

### Counterexample 1: Failure of Greedy Choice for Cleaning Crew Shifts (Knapsack Problem)
* **Scenario:** Budget = 10 Hours. 
  * Job 1: Duration = 6 hrs, Urgency Score = 12 (Ratio = 2.0)
  * Job 2: Duration = 5 hrs, Urgency Score = 9 (Ratio = 1.8)
  * Job 3: Duration = 5 hrs, Urgency Score = 9 (Ratio = 1.8)
* **Greedy Decision:** Pick Job 1 first (Ratio 2.0). Remaining capacity = 4 hrs. Job 2 and 3 cannot fit. Total Urgency Score = **12**.
* **Optimal Decision:** Pick Job 2 + Job 3. Duration = 10 hrs. Total Urgency Score = **18**.
* **Conclusion:** Proves that a local greedy choice fails for fractional/0-1 capacity constraints, requiring Dynamic Programming.
