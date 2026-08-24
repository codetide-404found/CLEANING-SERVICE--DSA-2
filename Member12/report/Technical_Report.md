# Ghana Smart Service Operations Optimizer
## Module: Campus Cleaning Service System
**Course:** DCIT 204/308: Data Structures and Algorithms I & II  
**Institution:** Department of Computer Science, University of Ghana  

---

## Executive Summary & Member 12 Checklist
- **JUnit 5 Test Suite:** Implemented in `Member12/unit-tests/` (covering boundary conditions, edge cases, and custom algorithms).
- **Correctness Evidence:** Trace tables, loop invariants, and counterexample proofs documented in `Member12/proofs/`.
- **System Architecture & Assembly:** Compiled final report outline for group presentation and oral defense.

---

## 1. Local Ghana Context & Problem Statement
* **Context:** Campus Cleaning Service Operations across hostels, lecture halls, and administrative blocks.
* **Problem Scope:** Efficiently assigning cleaning resources, prioritizing urgent service requests, and determining optimal travel routes between campus locations under budget and time constraints.

---

## 2. System Architecture & Data Model Schema
* **Locations Table:** Stores campus nodes (Hostels, Depts, Central Stores) with coordinates and attributes.
* **Roads/Edges Table:** Stores weighted connections between locations (distance, travel time).
* **Service Requests Table:** Queues cleaning tasks with urgency scores, request IDs, and status.
* **Resources Table:** Tracks available cleaning personnel, equipment, and capacity limits.

---

## 3. Data Structures Implementation & Module Mapping
| Data Structure | Assigned Module / Member | Operational Purpose |
| :--- | :--- | :--- |
| **Custom Graph (Adjacency List)** | Member 9 | Models campus cleaning zone network & edge weights |
| **Min-Heap / Priority Queue** | Member 9 | Urgency dispatch for high-priority cleaning requests |
| **Binary Search Tree & Red-Black Tree** | Member 6 | Fast lookup and indexing of service request IDs |
| **Disjoint Set (Union-Find)** | Member 10 | Campus sector connectivity and minimum spanning trees |
| **JUnit Suite & Trace Evidence** | Member 12 | Quality assurance, correctness verification, and report synthesis |

---

## 4. Responsible Algorithm Selection & Empirical Analysis
* **Route Optimization:** Dijkstra's Algorithm selected over BFS for weighted shortest paths across campus roads.
* **Search Operations:** Binary Search and BST indexing utilized to maintain $O(\log n)$ request lookup times.
* **Greedy vs. Dynamic Programming:** Detailed counterexamples demonstrate why 0-1 Knapsack DP is required over pure greedy selection for constrained shift allocations.

---

## 5. Individual Contribution Statement & Oral Defense Notes
* **Member 12 Contribution:** Designed and implemented automated test suites (`GraphAlgorithmsTest.java`, `TreeStructuresTest.java`), authored trace tables for Dijkstra and Binary Search, derived mathematical proof sketches, and structured final report artifacts.
