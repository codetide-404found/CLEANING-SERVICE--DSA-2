# Member 2 Database Module — Guide

Ghana Smart Service Operations Optimizer (University campus service hub context)
DCIT 204/308 Joint DSA Project

This document explains the database module in `src/db/`, `sql/schema.sql`, and
`src/DatabaseLoader.java`, and prepares you (Member 2) to defend it.

---

## 1. What was built

| File | Purpose |
|---|---|
| `sql/schema.sql` | Creates the six required tables with primary/foreign keys and CHECK constraints |
| `sql/sample_inserts.sql` | A handful of hand-written rows for quick manual testing/screenshots |
| `src/db/DatabaseConnection.java` | Opens JDBC connections to the SQLite file; one place to change the DB path |
| `src/db/DatabaseInitializer.java` | Executes `schema.sql`; safe to re-run (uses `IF NOT EXISTS`) |
| `src/db/model/*.java` | Plain data classes (POJOs) — one per table |
| `src/db/LocationDAO.java`, `RoadDAO`, `ServiceRequestDAO`, `ResourceDAO`, `AlgorithmRunDAO`, `AuditEventDAO` | Full CRUD access to each table via `PreparedStatement` |
| `src/db/CsvImportExport.java` | Validates and imports the four seed CSVs; exports locations/requests back to CSV |
| `src/DatabaseLoader.java` | **Default package** (same as `BTree`/`HashTable`) — the class other members actually call. Ties DAOs to the custom structures |
| `src/db/DatabaseTest.java` | JUnit 4 tests covering all ten required categories |
| `src/db/DatabaseSmokeTest.java` | Same checks, no JUnit jar required — useful before your test runner is set up |

**Why `DatabaseLoader` has no package declaration:** `BTree`, `HashTable`,
`Main`, etc. were written in Java's *default (unnamed) package*. Java does
not allow a named package (like `db`) to `import` a class from the default
package — so the one class that needs to reference both `db.*` DAOs *and*
`BTree`/`HashTable` has to live in the default package too. This is a
genuine Java language rule, not a project requirement — worth mentioning if
asked why `DatabaseLoader.java` looks structured differently from the DAOs.

---

## 2. Schema design decisions

- **locations.locationId is TEXT, not INTEGER** — the seed data uses IDs like
  `"1001"`; keeping it TEXT avoids silent type coercion and matches every
  foreign key that references it.
- **roads.roadId is an AUTOINCREMENT surrogate key** — the CSV doesn't supply
  a natural unique ID for a road, so the database generates one.
- **CHECK constraints on `service_requests.status` and
  `resources.availabilityStatus`** were written to match the actual
  vocabulary already used in the team's seed CSVs (`open`/`pending`/`closed`
  for requests, `available`/`unavailable` for resources) rather than an
  invented generic set — the schema should describe the real data, not the
  other way round. If your team later adds new status values, add them to
  the CHECK constraint in `schema.sql`.
- **`ON DELETE CASCADE` on roads, `ON DELETE RESTRICT` on service_requests
  and resources** — deleting a location automatically cleans up the roads
  touching it (roads are meaningless without both endpoints), but a location
  referenced by an open request or a resource's home base cannot be deleted
  by accident.
- **`audit_events` has no foreign keys** — `relatedEntityId` can point at a
  location, request, or resource depending on the event, so it is left as a
  plain TEXT column rather than three separate nullable foreign keys.
- This is a **design choice, not a brief requirement**: nothing in the brief
  mandates these exact constraint choices — say so plainly if asked, and be
  ready to justify the trade-off (data integrity vs. flexibility).

---

## 3. Integration — what each teammate gets from this module

| Member | What they call | What they get back |
|---|---|---|
| M1 (Team Leader) | `DatabaseLoader.initializeDatabase()` once at program start | Tables guaranteed to exist before the console menu runs |
| M3 (Dynamic array/linked list) | `LocationDAO.findAll()` / `ServiceRequestDAO.findAll()` | `List<Location>` / `List<ServiceRequestRecord>` to copy into their own array/list structure |
| M4 (Stack/Queue) | `ServiceRequestDAO.findByStatus("open")`, `AuditEventDAO.logEvent(...)` | Open requests to enqueue; a place to persist every push/pop as an audit row |
| M5 (Heap/Priority Queue) | `ServiceRequestDAO.findByStatus("open")` (already sorted by urgency DESC) | Requests ready to insert into a heap keyed on `urgency` |
| M6 (BST/Red-Black Tree) | `LocationDAO.findAll()` | Locations to index by `locationId` or `name` |
| M7 (B-Tree/Hash Table) | `DatabaseLoader.loadLocationsIntoStructures(btree, hashTable)` | Already wired — see `DatabaseLoader.main` for a working example |
| M8 (Graph/Disjoint Set) | `RoadDAO.findAll()` or `RoadDAO.findByFromLocation(id)` | Edges to build an adjacency list/matrix |
| M9 (Graph Algorithms) | Same `RoadDAO` calls as M8, plus `AlgorithmRunDAO.recordRun(...)` | Edge weights for Dijkstra/BFS/DFS; a place to log each run's timing |
| M10 (MST/DP) | `RoadDAO.findAll()`, `ResourceDAO.findAll()` | Edges for Prim/Kruskal; resources/capacities for the knapsack-style DP |
| M11 (Search/Sort) | `ServiceRequestDAO.findAll()`, `AlgorithmRunDAO.recordRun(...)` | Records to sort/search at scale; a place to log every timed experiment |
| M12 (Testing/Report) | `sql/schema.sql`, `src/db/DatabaseTest.java`, this guide | Schema diagrams, evidence checklist, and Section 8/9 report content below |

**Boundary to respect:** the `db` package only ever returns plain
`db.model.*` objects or Java `List`s. It never contains a `BTree`, `Heap`,
`Graph`, or any other custom structure — that conversion happens in each
member's own module (or in `DatabaseLoader` for the one example already
wired up). This keeps the database module swappable (SQLite → MySQL, say)
without touching anyone else's algorithm code.

---

## 4. Evidence checklist for your individual contribution

- [ ] Screenshot of `sql/schema.sql` and a terminal running
      `sqlite3 database/campus_operations.db ".schema"`
- [ ] Screenshot of row counts per table (`SELECT COUNT(*) FROM locations;` etc.)
      showing the brief's minimums are met: 50 locations, 100 roads,
      300 service requests, 30 resources
- [ ] Console output of `DatabaseLoader` running import (shows "Imported N
      row(s), 0 failed" per table)
- [ ] A deliberately broken CSV row (missing field / bad number) and the
      console output showing it was rejected with a clear reason, not
      silently ignored or crashed
- [ ] `db.DatabaseTest` (or `db.DatabaseSmokeTest`) run output, all green/PASS
- [ ] A before/after screenshot proving persistence: run the program, close
      the terminal, reopen it, run again, show the data is still there and
      wasn't re-imported (duplicate-skip messages are good evidence of this)
- [ ] Exported CSV file from `CsvImportExport.exportLocations(...)` /
      `exportServiceRequests(...)` opened in a spreadsheet program
- [ ] A one-paragraph note on how the CSV seed data was constructed
      (University of Ghana campus locations/buildings, synthetic cleaning
      service requests) — needed for the brief's AI-resistance/localisation
      requirement

---

## 5. Oral defense preparation

**Q: Why did you use SQLite instead of MySQL?**
SQLite needs no separate server process — the whole database is one file
(`database/campus_operations.db`), which makes the project trivially
portable between laptops and for submission. The brief allows SQLite,
MySQL, or PostgreSQL; SQLite was chosen for zero-setup grading. This is a
team design choice, not a brief requirement.

**Q: Why JDBC and not an ORM (Hibernate, JPA)?**
JDBC is explicitly allowed by the brief ("Built-in Java utilities may be
used for … JDBC/database support") and keeps every SQL statement visible
and explainable line-by-line during the oral defense — an ORM would hide
the actual queries behind generated code.

**Q: Why `PreparedStatement` instead of `Statement`?**
Two reasons: it prevents SQL injection by treating user/CSV input as data,
never as executable SQL, and SQLite can cache and reuse the compiled query
plan across repeated calls with different parameters (e.g. every row of a
CSV import uses the same `INSERT ... VALUES (?,?,?,?,?,?)` statement).

**Q: What is persistence, and how did you prove it?**
Persistence means data written by one program run is still available the
next time the program starts, because it lives in a file on disk, not only
in memory. Proven by: (1) using a file-backed `jdbc:sqlite:database/...db`
connection string rather than `jdbc:sqlite::memory:`, and (2) running
`DatabaseLoader` twice — the second run reports "duplicate — skipped" for
every row instead of re-inserting, which is only possible if the first
run's data was actually still there when the second run queried it.

**Q: What is the purpose of a DAO (Data Access Object)?**
It's the single place that knows how to turn one table's rows into Java
objects and back. Every other class calls `locationDAO.findAll()` instead
of writing its own SQL — so if the schema changes, only the DAO needs to
change, not every module that uses locations.

**Q: Why are primary and foreign keys important here?**
The primary key (`locationId`, `requestId`, etc.) guarantees no two rows
claim the same identity, which is what lets `CsvImportExport` reliably
detect "this is a duplicate" instead of silently double-counting. Foreign
keys (e.g. `service_requests.source -> locations.locationId`) stop a
request from ever pointing at a location that doesn't exist — enforced by
SQLite itself via `PRAGMA foreign_keys = ON`, not just by application code.

**Q: How does CSV data enter the database?**
`CsvImportExport` reads each CSV with `BufferedReader`, splits rows on
commas, validates every required field (non-empty strings, numbers that
actually parse, urgency in range, foreign keys that exist), and only then
calls the matching DAO's `insert(...)`. Invalid rows are skipped and
reported — they do not stop the rest of the import.

**Q: How does database data enter your custom data structures?**
Through `DatabaseLoader`, e.g.
`loadLocationsIntoStructures(BTree<String,String> btree, HashTable<String,String> hashTable)`
reads every row via `LocationDAO.findAll()` and calls `btree.insert(...)`
/ `hashTable.put(...)` for each one. The data path is:
`database file -> DAO -> db.model.Location -> BTree/HashTable`.

**Q: How did you validate records?**
Two layers: SQLite enforces `NOT NULL`, `CHECK`, and foreign key
constraints at the database level (so even a bug in Java code can't insert
bad data), and `CsvImportExport` additionally validates *before* attempting
an insert, so it can report a specific, readable reason for each rejected
row instead of a raw SQL exception.

**Q: How did you test your database module?**
`db.DatabaseTest` (JUnit 4) covers connection, table creation, insert,
retrieve, update, delete, invalid input, invalid/missing CSV data,
duplicate records, and persistence across a new connection — ten tests
matching the brief's required evidence categories. `db.DatabaseSmokeTest`
is a JUnit-free version of the same checks for environments without the
JUnit jars set up yet.

**Q: How does your work integrate with the algorithms?**
Every algorithm module reads data through a DAO instead of hard-coding
values: the graph module gets edges from `RoadDAO`, the heap/priority
queue gets requests from `ServiceRequestDAO`, and every timed experiment
can log its result through `AlgorithmRunDAO.recordRun(...)` so the
`algorithm_runs` table becomes real evidence for the efficiency lab
(Section 9 of the brief) instead of only console output.

---

## 6. What was *not* specified by the brief (design choices)

Be ready to say plainly that these were team decisions, not requirements:

- SQLite over MySQL/PostgreSQL
- Exact CHECK constraint value lists
- `ON DELETE CASCADE` vs `RESTRICT` per table
- Splitting DAOs one-per-table rather than one large `DatabaseService` class
- Putting `DatabaseLoader` in the default package (this one *is* forced by
  a Java language rule, not a free choice — see Section 1)
