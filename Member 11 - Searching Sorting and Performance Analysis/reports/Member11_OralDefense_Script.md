# Member 11: Oral Defense Script & Explanation Guide
**Course**: DCIT 204/308 - Data Structures and Algorithms I & II  
**Project**: Ghana Smart Service Operations Optimizer  
**Student**: Member 11 (Searching, Sorting & Performance Analysis)

---

## 1. Role Overview & Assigned Scope
"Good day Examiners. I am **Member 11**. My responsibility on the Ghana Smart Service Operations Optimizer project encompasses **Searching Algorithms, Sorting Algorithms, and Empirical Performance Analysis**."

---

## 2. Core Data Structure / Algorithm Defense (Quick Sort & Binary Search)

### Q1: How did you implement Quick Sort from scratch without Java built-in libraries?
> **Answer**:  
> "I implemented Quick Sort using a recursive divide-and-conquer strategy from scratch. To prevent $O(N^2)$ worst-case degradation on pre-sorted arrays, I implemented a **Median-of-Three pivot selection** strategy (comparing low, middle, and high elements).  
> The array is partitioned around the pivot using in-place element swapping (`swap(A, i, j)`). Elements less than or equal to the pivot are moved to the left sub-array, and elements greater are moved to the right. Quick Sort then recursively sorts the sub-arrays.  
> Quick Sort operates in **$O(N \log N)$ average time** and requires **$O(\log N)$ stack space** for recursion, operating entirely in-place."

### Q2: Why does Binary Search require a sorted array, and what happens if the array is unsorted?
> **Answer**:  
> "Binary Search relies on the **Monotonic Ordering Invariant**: $A[i] \le A[i+1]$. At each step, it compares the target with $A[mid]$. If $A[mid] > target$, it safely eliminates the right half $A[mid..high]$ because all elements in that sub-range are guaranteed to be greater than target.  
> If the array is unsorted, this guarantee breaks. The target could reside in the pruned sub-array. I created a counterexample test (`demonstrateUnsortedBinarySearchFailure`) where searching for `12` in an unsorted array `[85, 12, 44, 9, 99, 23]` returns `-1` (Not Found), even though `12` is present at index 1. This empirically demonstrates why sorting is a mandatory precondition."

---

## 3. Empirical Performance Findings Defense

### Q3: What were your key empirical findings when comparing $O(N^2)$ and $O(N \log N)$ sorting algorithms?
> **Answer**:  
> "When benchmarking across input sizes $N \in \{100, 500, 1000, 5000, 10000\}$:  
> 1. At $N = 100$, all algorithms execute in less than $0.3$ ms.  
> 2. At $N = 10,000$, Selection Sort ($O(N^2)$) required **$49,995,000$ comparisons** taking **$35.11$ ms**, whereas Quick Sort ($O(N \log N)$) required only **$135,012$ comparisons** taking **$0.93$ ms** — over **37 times faster**.  
> 3. Quick Sort outperformed Merge Sort ($1.36$ ms at $N=10,000$) because Quick Sort works strictly in-place with superior cache locality, avoiding extra memory allocations."

---

## 4. Live Code Demonstration Commands
If asked by examiners to run live demonstrations:

1. **Run 48 Unit Tests**:
   ```bash
   java -cp bin SearchingSortingTestRunner
   ```
2. **Run Step-by-Step Trace Tables & Counterexamples**:
   ```bash
   java -cp bin TraceDriver
   ```
3. **Run Empirical Efficiency Benchmarks & Regenerate Graphs**:
   ```bash
   java -cp bin EmpiricalAnalysisRunner
   ```
