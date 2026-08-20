# BINARY TREE & RED TREE IMPLEMENTATIONS
### Member 6 — Campus Cleaning Service Project

This folder contains the Binary Search Tree and Red-Black Tree module for the
group's campus cleaning service dispatch system.

## Files

| File | Purpose |
|---|---|
| `BinarySearchTree.java` | Custom BST — insert, delete, search, min/max, traversals |
| `RedBlackTree.java` | Custom self-balancing Red-Black Tree — insert with rotation/recoloring, search, traversal |
| `CampusLocation.java` | Domain key object (location ID, name, zone, cleaning priority, estimated time) |
| `BinarySearchTreeTest.java` | JUnit 5 tests for BST — normal, boundary, invalid input |
| `RedBlackTreeTest.java` | JUnit 5 tests for RBT — normal, boundary, invalid input |
| `TraceDriver.java` | Runnable demo using integer keys, proves RBT height stays low vs a degenerate BST |
| `CampusTraceDriver.java` | Runnable demo using real campus location data + index-number-derived parameter |
| `TraceTables.md` | Required trace tables, proof sketch, and captured program output |
| `Member6_OralDefense_Script.md` | Plain-language explanation for the oral defense |

## How to run

```bash
javac CampusLocation.java BinarySearchTree.java RedBlackTree.java CampusTraceDriver.java
java CampusTraceDriver
```

## Note on AI assistance

Portions of this module were drafted with AI assistance and reviewed/understood
before submission, per Section 15 of the project brief.

---

# HEAP & PRIORITY QUEUE DISPATCH MODULE
### Member 5 — Campus Cleaning Service Project

This module implements a Binary Max Heap-based priority queue for priority-based
dispatch of cleaning requests, plus a Dispatcher component that integrates with
the routing module (Member 9).

## Files

| File | Purpose |
|---|---|
| `Priority.java` | Enum: CRITICAL > HIGH > MEDIUM > LOW |
| `RequestStatus.java` | Enum: PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED |
| `CleaningRequest.java` | Domain model — request ID, customer, location, category, priority, time, status |
| `Heap.java` | Binary Max Heap — O(log n) insert, extract, update; O(1) peek; O(n) build |
| `PriorityQueueManager.java` | Clean API over Heap — addRequest, getNextRequest, peekNextRequest, updateRequestPriority |
| `RouteEngine.java` | Interface for routing integration point (decouples Dispatcher from Graph implementation) |
| `Dispatcher.java` | Retrieves highest-priority request, delegates routing to RouteEngine, returns DispatchResult |
| `HeapDispatchDemo.java` | Manual test runner (45 assertions, 0 failures) |
| `HeapTest.java` | JUnit 5 tests for Heap (14 tests) |
| `PriorityQueueManagerTest.java` | JUnit 5 tests for PriorityQueueManager (10 tests) |
| `DispatcherTest.java` | JUnit 5 tests for Dispatcher (7 tests) |

## Priority Ordering

```
CRITICAL (4) > HIGH (3) > MEDIUM (2) > LOW (1)
```

## Tie-Breaking Rule

When priorities are equal:
1. Earlier request time first
2. If times are equal, lexicographically smaller request ID first

## Supported Operations & Complexities

| Operation | Complexity | Description |
|---|---|---|
| insert() | O(log n) | Add request to heap, sift up |
| extractHighestPriority() | O(log n) | Remove root, move last to root, sift down |
| peek() | O(1) | Return root without removal |
| updatePriority() | O(log n)* | Find + reorganize (*finding is O(1) with index map) |
| heapify() | O(log n) | Restore heap from given index downward |
| buildHeap() | O(n) | Build from array |

## Dispatcher Integration Flow

```
Customer Request
       ↓
Priority Assignment
       ↓
PriorityQueueManager (Heap)
       ↓
Highest Priority Request
       ↓
Dispatcher
       ↓
RouteEngine (interface)
       ↓
Routing Module (Member 9's Graph/Dijkstra)
       ↓
Shortest Route
       ↓
Cleaning Team
```

The `Dispatcher` depends on the `RouteEngine` interface, not directly on `Graph`.
Member 9 can implement `RouteEngine` by wrapping their `Graph` class.

## How to run

### Compile all module files:
```bash
javac Priority.java RequestStatus.java CampusLocation.java CleaningRequest.java Heap.java PriorityQueueManager.java RouteEngine.java Dispatcher.java
```

### Run the demo/test runner:
```bash
java HeapDispatchDemo
```

### Run JUnit 5 tests (requires JUnit 5 jars in `lib/`):
```bash
# Compile
javac -cp "lib/*" HeapTest.java PriorityQueueManagerTest.java DispatcherTest.java

# Run via JUnit Console
java -cp "lib/junit-platform-console-standalone-1.10.0.jar;." org.junit.platform.console.ConsoleLauncher --class-path="lib/*;." --select-class=HeapTest --select-class=PriorityQueueManagerTest --select-class=DispatcherTest
```

## Test Results

- **HeapDispatchDemo**: 45/45 assertions passed
- **JUnit 5 (HeapTest)**: 14/14 tests passed
- **JUnit 5 (PriorityQueueManagerTest)**: 10/10 tests passed
- **JUnit 5 (DispatcherTest)**: 7/7 tests passed
- **Total JUnit**: 31/31 tests passed
