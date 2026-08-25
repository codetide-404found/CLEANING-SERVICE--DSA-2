---

## Member 3 — Dynamic Array & Linked List

This section covers the `DynamicArray` and `MyLinkedList` module for the
group's campus cleaning service dispatch system.

### Files

| File | Purpose |
|---|---|
| `DynamicArray.java` | Custom resizable array — insert, get, set, remove, auto-resize |
| `MyLinkedList.java` | Custom doubly linked list — addFirst, addLast, insertAfter, remove, custom Iterator |
| `DynamicArrayTest.java` | JUnit 5 tests for DynamicArray — normal, boundary, invalid input |
| `MyLinkedListTest.java` | JUnit 5 tests for MyLinkedList — normal, boundary, invalid input |
| `DynamicArrayTraceDriver.java` | Runnable demo using integer keys, proves resize cost stays amortized O(1) |
| `CampusRouteTraceDriver.java` | Runnable demo using the same real campus location data as `CampusTraceDriver.java`, plus an index-number-derived parameter |
| `Member3_TraceTables.md` | Required trace tables and proof sketch |
| `Member3_OralDefense_Script.md` | Plain-language explanation for the oral defense |

### How this connects to the rest of the system

- `DynamicArray<CampusLocation>` holds the full location catalog — the same
  flat roster that Member 6's `BinarySearchTree`/`RedBlackTree` then indexes
  by `locationId`.
- `MyLinkedList<CampusLocation>` models a single cleaning crew's ordered
  route for the day — stops can be appended, inserted mid-route for a rush
  job, or removed for a cancellation, all in O(1) once the target node is
  found.
- No built-in `java.util.ArrayList` or `java.util.LinkedList` is used
  internally — only the `Iterator`/`Iterable` interfaces are implemented.
