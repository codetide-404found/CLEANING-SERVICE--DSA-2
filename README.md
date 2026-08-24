## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## Database module (Member 2)

The `db` package (`src/db/`) implements the project's SQLite database layer:
schema, JDBC connection handling, DAOs for all six tables, CSV import/export,
and the bridge that loads database rows into the team's custom B-tree /
hash table (`src/DatabaseLoader.java`, default package, alongside `BTree`
and `HashTable`).

**Setup**

1. `lib/sqlite-jdbc-3.42.0.0.jar` is already included - add it to your
   classpath (VS Code's Java extension picks up anything in `lib/`
   automatically via `.vscode/settings.json`).
2. For the JUnit tests in `src/db/DatabaseTest.java`, add `junit-4.13.2.jar`
   and `hamcrest-core-1.3.jar` to `lib/` as well (most VS Code Java installs
   already bundle these via the Java Test Runner extension).

**Run the database loader / demo**

```
javac -cp "lib/*" -d bin $(find src -name "*.java")
java -cp "bin:lib/*" DatabaseLoader seeds
```

This creates `database/campus_operations.db`, creates the six tables from
`sql/schema.sql` if they do not exist, imports `seeds/*.csv` (skipping
already-populated tables so re-running is safe), and loads all locations
into a B-tree and hash table as a demonstration of the required
`database -> DAO -> model -> custom data structure` data path.

**Run the tests**

```
java -cp "bin:lib/*" org.junit.runner.JUnitCore db.DatabaseTest
```

A JUnit-free `db.DatabaseSmokeTest` (same checks, no external jars needed
beyond the SQLite driver) is included as a fallback:

```
java -cp "bin:lib/*" db.DatabaseSmokeTest
```

See `Member2_Database_Guide.md` for the schema design rationale, an
integration map for every other member, the evidence checklist, and oral
defense Q&A prep.
