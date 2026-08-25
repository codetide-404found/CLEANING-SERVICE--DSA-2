# Member 3 — Oral Defense Script

Plain-language explanation for defending `DynamicArray` and `MyLinkedList`.

## What I built

Two custom data structures, built from scratch without using
`java.util.ArrayList` or `java.util.LinkedList`:

1. **DynamicArray** — a resizable array-backed list. Holds the full catalog
   of cleaning-service locations that Member 6's BST/RBT then indexes by
   `locationId`.
2. **MyLinkedList** — a doubly linked list with a custom iterator. Models one
   cleaning crew's ordered route for the day.

## Why an array for the catalog, but a linked list for a route?

The location catalog is mostly read and indexed by ID — an array gives
O(1) access once you know the position, and it's simple and cache-friendly
to scan sequentially when loading data or rebuilding an index. A single
day's cleaning route, by contrast, gets edited constantly during the day:
rush jobs get slotted in mid-route, cancellations get pulled out, and stops
get reordered — all of which a linked list handles in O(1) once you've
found the right node, without shifting the rest of the route the way an
array would.

## How resizing works

The array starts at a small capacity and doubles whenever it fills up. I
can explain, using the trace table, exactly which insert triggers each
resize, and why doubling (rather than adding a fixed number of slots each
time) keeps the *average* cost of each insert at O(1) even though any
individual resize is O(n) — I can walk through the aggregate-method proof
sketch in `Member3_TraceTables.md` if asked to justify this formally.

The array also shrinks, but only when usage drops to a quarter of capacity,
and never below a default minimum — this is deliberately asymmetric to
the 100%-full grow trigger, so the structure can't "thrash" (resize back
and forth) if the crew count near a threshold fluctuates by one.

## How the index-number parameter fits in

My initial array capacity isn't just a constant — it's derived from my own
student index number (22241883): `4 + (22241883 % 5) = 7`. This is one of
the required index-derived parameters (Section 2.iii); Member 6's
`CampusTraceDriver.java` derives a different parameter (a priority
threshold) from a different formula, so the two don't overlap in purpose.

## How the iterator works

`MyLinkedList` implements `Iterable`/`Iterator` directly — `hasNext()` and
`next()` walk the node chain manually, and `next()` on an empty iterator
throws `NoSuchElementException` rather than failing silently or crashing
with a null pointer. This lets a route be printed with a plain Java
for-each loop even though it's a fully custom structure underneath.

## Anticipated questions

**Q: Why not just use `ArrayList`/`LinkedList` from the standard library?**
A: The project brief requires custom implementations for all assessed core
logic — reusing the built-in classes wouldn't demonstrate understanding of
how resizing or node-linking actually works underneath.

**Q: What happens if you remove a location that isn't on the route?**
A: `remove()` returns `false` rather than throwing, so calling code can
check the result instead of needing a try/catch for what's often a normal
outcome (e.g. a stop already completed and removed earlier in the day).

**Q: What breaks if another member changes `CampusLocation`'s fields?**
A: Both `DynamicArrayTraceDriver` and `CampusRouteTraceDriver` depend on
`getLocationId()`, `getName()`, `getZone()`, `getCleaningPriority()`, and
`getEstimatedMinutes()`. Any rename would need coordinating with me and
Member 6 before merging.
