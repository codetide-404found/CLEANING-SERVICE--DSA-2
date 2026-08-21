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
