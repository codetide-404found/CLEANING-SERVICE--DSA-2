# Member 3 — Trace Tables & Proof Sketch

Custom `DynamicArray<T>` and `MyLinkedList<T>` implementations for the Campus
Cleaning Service Project. Companion file to `DynamicArrayTraceDriver.java`
(generic integer keys) and `CampusRouteTraceDriver.java` (real cleaning-service
location data, using the same sample dataset as `CampusTraceDriver.java`).

## 1. DynamicArray resize trace — generic integer keys

Starting capacity 2, inserting integers 1–20 in order (captured output of
`DynamicArrayTraceDriver.java`):

| Insert # | Size After | Capacity Before | Capacity After | Resized? |
|---|---|---|---|---|
| 1 | 1 | 2 | 2 | — |
| 2 | 2 | 2 | 2 | — |
| 3 | 3 | 2 | 4 | YES |
| 4 | 4 | 4 | 4 | — |
| 5 | 5 | 4 | 8 | YES |
| 8 | 8 | 8 | 8 | — |
| 9 | 9 | 8 | 16 | YES |
| 16 | 16 | 16 | 16 | — |
| 17 | 17 | 16 | 32 | YES |
| 20 | 20 | 32 | 32 | — |

Capacity doubles exactly when `size == capacity` at the start of an insert:
2 → 4 → 8 → 16 → 32.

## 2. DynamicArray resize trace — real cleaning-service catalog

Starting capacity 7, derived from index number 22241883 as
`4 + (22241883 % 5) = 7` (Section 2.iii index-derived parameter). Inserting
the 8-location sample from `CampusTraceDriver.java`:

| Insert # | Location ID | Capacity Before | Capacity After | Resized? |
|---|---|---|---|---|
| 1 | UG-N-01 | 7 | 7 | — |
| 7 | UG-W-01 | 7 | 7 | — |
| 8 | UG-S-02 | 7 | 14 | YES |

## 3. MyLinkedList operation trace — cleaning crew daily route

Using the same 8-location dataset, building one crew's route
(`CampusRouteTraceDriver.java`):

| Step | Operation | Resulting Route |
|---|---|---|
| 1 | `addLast(Balme Library)` | `[UG-N-01]` |
| 2 | `addLast(Legon Hall)` | `[UG-N-01, UG-S-01]` |
| 3 | `addLast(Computer Science Dept.)` | `[UG-N-01, UG-S-01, UG-C-01]` |
| 4 | `addFirst(Volta Hall)` — urgent, priority 5 | `[UG-S-02, UG-N-01, UG-S-01, UG-C-01]` |
| 5 | `insertAfter(Legon Hall, JQB)` — rush job | `[UG-S-02, UG-N-01, UG-S-01, UG-C-02, UG-C-01]` |
| 6 | `remove(Computer Science Dept.)` — cancelled | `[UG-S-02, UG-N-01, UG-S-01, UG-C-02]` |

Final iterator traversal: `UG-S-02 -> UG-N-01 -> UG-S-01 -> UG-C-02`
Total estimated cleaning time for this route: **265 minutes**.

## 4. Proof sketch — amortized O(1) insertion under doubling

**Claim:** appending `n` elements to a `DynamicArray` that doubles its
capacity whenever full costs `O(n)` total, so each insert is `O(1)` amortized.

**Argument (aggregate method):** each element is copied at most once per
resize it survives. An element inserted when the array first reaches
capacity `c` may be copied during the resize to `2c`, then possibly again at
`4c`, `8c`, etc. Summing the total copying work across all `n` inserts:

```
Total copies ≤ n + n/2 + n/4 + n/8 + ... < 2n
```

This is a geometric series bounded by `2n`, which is `O(n)`. Since the `n`
insert operations themselves are also `O(n)` in total (`O(1)` each, ignoring
resizes), the combined cost of `n` inserts is `O(n) + O(n) = O(n)`, giving an
average (amortized) cost of `O(n) / n = O(1)` per insert.

**Why this matters for the cleaning-service dispatch system:** the location
catalog can grow (new buildings added mid-semester) without the system
paying an `O(n)` penalty on every single insert — the expensive `O(n)` copy
only happens on the rare resize step, not on every add.

## 5. Shrink guard — avoiding thrashing

`shrinkIfNeeded()` only halves capacity when `size <= capacity / 4`, and
never below the default minimum capacity (4). This asymmetry (grow at 100%
full, shrink at 25% full) means an application that repeatedly inserts and
removes near a single threshold cannot trigger a resize on every operation —
there is always a buffer zone between the grow and shrink thresholds.
