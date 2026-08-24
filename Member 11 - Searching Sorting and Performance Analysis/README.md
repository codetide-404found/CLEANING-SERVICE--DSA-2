# SEARCHING, SORTING & PERFORMANCE ANALYSIS MODULE
### Member 11 — Ghana Smart Service Operations Optimizer
**University of Ghana | Department of Computer Science**  
**DCIT 204/308: Data Structures and Algorithms I & II**

---

## 1. Module Overview
This module implements the custom **Searching, Sorting, and Empirical Performance Analysis Engine** for the group's Ghana Smart Service Operations Optimizer. All algorithms are written completely from scratch without reliance on Java's built-in sorting/searching utility libraries (`java.util.Arrays.sort`, `java.util.Collections.sort`, etc.).

---

## 2. Implemented Algorithms & Scope

### Searching Algorithms
- **Linear Search**: $O(N)$ time, $O(1)$ space. Operates on Comparable objects and primitive arrays with operation counting.
- **Binary Search (Iterative & Recursive)**: $O(\log N)$ time, $O(1)$ space. Includes sorted precondition validation (`isSorted`) and strict assertion mode (`binarySearchStrict`).

### Sorting Algorithms
- **Selection Sort**: $O(N^2)$ comparisons, $O(N)$ swaps, unstable, in-place.
- **Insertion Sort**: $O(N^2)$ worst/average, $O(N)$ best (adaptive), stable, in-place.
- **Merge Sort**: $\Theta(N \log N)$ time, $O(N)$ auxiliary space, stable divide-and-conquer.
- **Quick Sort**: $O(N \log N)$ average time, Median-of-Three pivot selection, unstable, in-place divide-and-conquer.

### Empirical Efficiency Lab & Visual Graph Generator
- Benchmarks algorithms across input sizes $N \in \{100, 500, 1000, 5000, 10000\}$.
- Exports metrics to `graphs/performance_results.csv`.
- Renders standalone SVG line charts (`graphs/search_performance.svg` & `graphs/sorting_performance.svg`) and an interactive HTML report dashboard (`graphs/performance_dashboard.html`).

---

## 3. Directory & File Structure

```
Member 11 - Searching Sorting and Performance Analysis/
├── src/
│   ├── SearchingAlgorithms.java   # Custom Linear & Binary Search engine
│   ├── SortingAlgorithms.java     # Custom Selection, Insertion, Merge & Quick Sort
│   ├── ServiceRequest.java        # Campus service job domain model + Index weighting
│   ├── LocationRecord.java        # Campus location domain model
│   ├── SearchMetrics.java         # Searching comparison & runtime metrics tracker
│   ├── SortMetrics.java           # Sorting comparison, swap & depth metrics tracker
│   ├── EmpiricalAnalysisRunner.java # Section 9 empirical benchmark lab runner
│   └── GraphPlotter.java          # SVG & HTML performance chart generator
├── tests/
│   └── SearchingSortingTestRunner.java # 48 comprehensive unit tests
├── trace/
│   └── TraceDriver.java           # Trace table generator & counterexample demo
├── reports/
│   ├── TraceTables.md             # Trace tables, proof sketches & loop invariants
│   ├── PerformanceAnalysisReport.md # Asymptotic analysis & empirical report
│   └── Member11_OralDefense_Script.md # Oral defense preparation script
├── graphs/
│   ├── performance_results.csv   # Raw benchmark measurements
│   ├── search_performance.svg    # Search runtime SVG line chart
│   ├── sorting_performance.svg   # Sorting runtime SVG line chart
│   └── performance_dashboard.html # HTML visual dashboard
└── README.md
```

---

## 4. How to Compile & Run

### Step 1: Compile All Java Files
```bash
powershell -Command "$files = Get-ChildItem -Path 'Member 11 - Searching Sorting and Performance Analysis' -Filter '*.java' -Recurse | Select-Object -ExpandProperty FullName; & 'C:\Program Files\Java\jdk-26.0.2.1\bin\javac.exe' -d 'Member 11 - Searching Sorting and Performance Analysis/bin' $files"
```

### Step 2: Run Unit Tests (48 Test Cases)
```bash
& "C:\Program Files\Java\jdk-26.0.2.1\bin\java.exe" -cp "Member 11 - Searching Sorting and Performance Analysis/bin" SearchingSortingTestRunner
```

### Step 3: Run Trace Table Generator & Counterexample Demo
```bash
& "C:\Program Files\Java\jdk-26.0.2.1\bin\java.exe" -cp "Member 11 - Searching Sorting and Performance Analysis/bin" TraceDriver
```

### Step 4: Run Empirical Benchmarks & Generate Graphs
```bash
& "C:\Program Files\Java\jdk-26.0.2.1\bin\java.exe" -cp "Member 11 - Searching Sorting and Performance Analysis/bin" EmpiricalAnalysisRunner
```

---

## 5. AI-Resistance & Academic Integrity Declaration
Per Section 15 of the project brief:
- All custom data structures and algorithm logic were authored by Member 11 from fundamental principles.
- Dataset parameters are derived using Member 11's index-number-derived parameter (`INDEX_PARAM_WEIGHT = 21`, Random Seed = `10954321L`).
- Step-by-step trace tables, loop invariant proofs, induction proofs, and counterexamples are fully documented in `reports/TraceTables.md`.
