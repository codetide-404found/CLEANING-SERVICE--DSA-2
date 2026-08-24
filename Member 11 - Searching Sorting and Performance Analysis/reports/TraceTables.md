# Member 11: Correctness Evidence, Trace Tables & Proof Sketches
**Course**: DCIT 204/308 - Data Structures and Algorithms I & II  
**Project**: Ghana Smart Service Operations Optimizer  
**Local Context**: University of Ghana Campus Cleaning & Maintenance Service  
**Student**: Member 11 (Searching, Sorting & Performance Analysis)

---

## 1. Trace Tables

### Table 1.1: Binary Search Trace Table
- **Target Key**: `67`
- **Sorted Dataset**: `[11, 23, 34, 45, 56, 67, 78, 89, 90, 99]` ($N = 10$)

| Step | `low` | `high` | `mid` | `array[mid]` | Comparison / State Evaluation | Action Taken |
| :---: | :---: | :---: | :---: | :---: | :--- | :--- |
| **1** | 0 | 9 | 4 | 56 | $56 < 67$ (Target is greater) | Search right half: `low = mid + 1` (5) |
| **2** | 5 | 9 | 7 | 89 | $89 > 67$ (Target is smaller) | Search left half: `high = mid - 1` (6) |
| **3** | 5 | 6 | 5 | 67 | $67 == 67$ (Match Found!) | Terminate & Return Index `5` |

---

### Table 1.2: Insertion Sort Trace Table
- **Initial Unsorted Array**: `[45, 12, 89, 23, 7]` ($N = 5$)

| Pass ($i$) | Key ($A[i]$) | Elements Shifted | Comparisons Made | Array State after Pass |
| :---: | :---: | :---: | :---: | :--- |
| **Initial** | - | - | - | `[45, 12, 89, 23, 7]` |
| **1** | 12 | `45` shifted right | 1 | `[12, 45, 89, 23, 7]` |
| **2** | 89 | None (89 > 45) | 1 | `[12, 45, 89, 23, 7]` |
| **3** | 23 | `89, 45` shifted right | 3 | `[12, 23, 45, 89, 7]` |
| **4** | 7 | `89, 45, 23, 12` shifted right | 4 | `[7, 12, 23, 45, 89]` |

---

### Table 1.3: Selection Sort Trace Table
- **Initial Unsorted Array**: `[64, 25, 12, 22, 11]` ($N = 5$)

| Pass ($i$) | Target Index | Minimum Found | Min Index | Swap Action Executed | Array State after Pass |
| :---: | :---: | :---: | :---: | :--- | :--- |
| **1** | 0 | 11 | 4 | Swap `A[0]` (64) with `A[4]` (11) | `[11, 25, 12, 22, 64]` |
| **2** | 1 | 12 | 2 | Swap `A[1]` (25) with `A[2]` (12) | `[11, 12, 25, 22, 64]` |
| **3** | 2 | 22 | 3 | Swap `A[2]` (25) with `A[3]` (22) | `[11, 12, 22, 25, 64]` |
| **4** | 3 | 25 | 3 | No Swap (Element already in place) | `[11, 12, 22, 25, 64]` |

---

### Table 1.4: Merge Sort Decomposition & Merge Trace
- **Input Array**: `[38, 27, 43, 3, 9, 82, 10]`

```
Split [0..6] into [0..3] and [4..6]
  Split [0..3] into [0..1] and [2..3]
    Split [0..1] into [0..0] [38] and [1..1] [27] -> Merge -> [27, 38]
    Split [2..3] into [2..2] [43] and [3..3] [3]  -> Merge -> [3, 43]
  Merge [0..1] and [2..3] -> [3, 27, 38, 43]
  Split [4..6] into [4..5] and [6..6]
    Split [4..5] into [4..4] [9] and [5..5] [82]  -> Merge -> [9, 82]
  Merge [4..5] and [6..6] -> [9, 10, 82]
Merge Left [0..3] and Right [4..6] -> [3, 9, 10, 27, 38, 43, 82]
```

---

## 2. Loop Invariants & Proof Sketches

### Proof 2.1: Loop Invariant for Binary Search
**Algorithm**: `binarySearch(A, key)`

**Invariant**: At the start of each iteration of the `while (low <= high)` loop, if `key` exists in the original array `A[0..N-1]`, then it must reside within the sub-range `A[low .. high]`.

1. **Initialization**: Before the loop begins, `low = 0` and `high = N - 1`. The range `A[low .. high]` spans the entire array `A[0 .. N-1]`. Thus, if `key` is present, it trivially lies within `A[low .. high]`.
2. **Maintenance**: Suppose the invariant holds at the start of an iteration. We compute `mid = low + (high - low) / 2`.
   - If `A[mid] == key`, the algorithm returns `mid` (correct).
   - If `A[mid] < key`, since `A` is sorted in ascending order, all elements `A[0 .. mid]` are strictly less than `key`. Therefore `key` cannot exist in `A[0 .. mid]`. Updating `low = mid + 1` shrinks the search space to `A[mid+1 .. high]`, preserving the invariant.
   - If `A[mid] > key`, all elements `A[mid .. N-1]` are strictly greater than `key`. Updating `high = mid - 1` shrinks the search space to `A[low .. mid-1]`, preserving the invariant.
3. **Termination**: The loop terminates when `low > high`. By the invariant, `key` cannot reside in `A[low .. high]` because the interval is empty ($0$ elements). Therefore, returning `-1` correctly indicates that `key` is absent.

---

### Proof 2.2: Loop Invariant for Insertion Sort
**Algorithm**: `insertionSort(A)`

**Invariant**: At the start of iteration $i$ of the outer loop (`1 <= i < N`), the sub-array `A[0 .. i-1]` consists of the elements originally in `A[0 .. i-1]`, but in sorted ascending order.

1. **Initialization**: For $i = 1$, the sub-array `A[0 .. 0]` contains a single element. A single-element array is trivially sorted.
2. **Maintenance**: In iteration $i$, `key = A[i]`. The inner `while` loop shifts elements of `A[0 .. i-1]` that are strictly greater than `key` one position to the right. Inserting `key` into the vacated spot places it in its correct relative sorted position among `A[0 .. i]`. Thus `A[0 .. i]` is sorted, maintaining the invariant for iteration $i + 1$.
3. **Termination**: The loop terminates when $i = N$. By the invariant, the sub-array `A[0 .. N-1]` is sorted, which is the entire array. $\blacksquare$

---

### Proof 2.3: Mathematical Induction Proof for Merge Sort Recurrence
**Recurrence Relation**:
$$T(n) = \begin{cases} \Theta(1) & \text{if } n = 1 \\ 2T(n/2) + \Theta(n) & \text{if } n > 1 \end{cases}$$

**Theorem**: $T(n) = \Theta(n \log_2 n)$.

**Induction Proof**:
We prove $T(n) \le c \cdot n \log_2 n$ for some constant $c > 0$.
Assume $T(k) \le c \cdot k \log_2 k$ holds for all $k < n$.

Substitute inductive hypothesis into recurrence:
$$T(n) = 2 T(n/2) + d \cdot n \le 2 \left( c \frac{n}{2} \log_2 \frac{n}{2} \right) + d \cdot n$$
$$= c \cdot n (\log_2 n - \log_2 2) + d \cdot n = c \cdot n \log_2 n - c \cdot n + d \cdot n$$
$$= c \cdot n \log_2 n - (c - d) n$$

For $c \ge d$, $(c - d) n \ge 0$, giving:
$$T(n) \le c \cdot n \log_2 n$$
Thus $T(n) = O(n \log_2 n)$. Similarly, $T(n) = \Omega(n \log_2 n)$, concluding $T(n) = \Theta(n \log_2 n)$. $\blacksquare$

---

## 3. Precondition Violation & Counterexample

### Counterexample 3.1: Binary Search on Unsorted Sequence
- **Precondition Requirement**: Binary Search assumes input array `A` is sorted in non-decreasing order ($A[i] \le A[i+1]$).
- **Unsorted Sequence**: `A = [85, 12, 44, 9, 99, 23]`
- **Target to Search**: `12` (Actual position: Index `1`)

**Binary Search Execution Trace**:
1. `low = 0`, `high = 5`, `mid = 2`, `A[2] = 44`.
2. Compare `A[mid]` (44) with target (12): $44 > 12$.
3. Binary search assumes all elements right of index 2 are $\ge 44$, so it eliminates index 2..5 and sets `high = mid - 1` (1).
4. `low = 0`, `high = 1`, `mid = 0`, `A[0] = 85`.
5. Compare `A[mid]` (85) with target (12): $85 > 12$.
6. Set `high = mid - 1` (-1).
7. Loop terminates (`low > high`). Returns `-1` (**NOT FOUND**).

**Result Analysis**: Target `12` exists at index `1`, but Binary Search returned `-1` because the unsorted ordering caused the algorithm to prune the wrong sub-array. This proves that sorting is an indispensable precondition for Binary Search correctness.
