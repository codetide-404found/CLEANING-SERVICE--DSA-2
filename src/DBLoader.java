import java.io.*;
import java.nio.file.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DBLoader {
    private final String dbUrl;

    public DBLoader(String dbFilePath) {
        this.dbUrl = "jdbc:sqlite:" + dbFilePath;
    }

    public void executeSqlFile(String sqlFilePath) throws Exception {
        String sql = new String(Files.readAllBytes(Paths.get(sqlFilePath)));
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(false);
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("PRAGMA foreign_keys = ON;");
                for (String stmt : sql.split(";")) {
                    String t = stmt.trim();
                    if (!t.isEmpty()) {
                        st.executeUpdate(t);
                    }
                }
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    public void importCsv(String tableName, String csvPath, String[] columns) throws Exception {
        Path csvFile = Paths.get(csvPath);
        if (!Files.exists(csvFile)) {
            return;
        }
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            conn.setAutoCommit(false);
            String qMarks = String.join(",", Collections.nCopies(columns.length, "?"));
            String sql = String.format("INSERT OR REPLACE INTO %s (%s) VALUES (%s)",
                    tableName, String.join(",", columns), qMarks);
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 BufferedReader br = Files.newBufferedReader(csvFile)) {
                br.readLine();
                String line;
                int batch = 0;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",", -1);
                    for (int i = 0; i < columns.length; i++) {
                        String v = i < parts.length ? parts[i].trim() : null;
                        ps.setString(i + 1, v);
                    }
                    ps.addBatch();
                    if (++batch % 500 == 0) {
                        ps.executeBatch();
                    }
                }
                ps.executeBatch();
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    public void loadIntoStructures(String dbFilePath, BTree<String,String> btree, HashTable<String,String> htable) throws Exception {
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT locationId, name FROM locations")) {
            while (rs.next()) {
                String id = rs.getString("locationId");
                String name = rs.getString("name");
                btree.insert(id, name);
                htable.put(id, name);
            }
        }
    }

    public void recordRun(String algorithmName, int inputSize, long timeNs, long memoryKb) throws Exception {
        String dateRun = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        try (Connection conn = DriverManager.getConnection(dbUrl);
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO algorithm_runs (algorithmName,inputSize,timeNs,memoryKb,dateRun) VALUES (?,?,?,?,?)")) {
            ps.setString(1, algorithmName);
            ps.setInt(2, inputSize);
            ps.setLong(3, timeNs);
            ps.setLong(4, memoryKb);
            ps.setString(5, dateRun);
            ps.executeUpdate();
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Usage: java DBLoader <dbFile> <schema.sql> <seedsFolder>");
            System.out.println("Example: java DBLoader ug.db schema.sql seeds/");
            return;
        }
        String dbFile = args[0];
        String schemaFile = args[1];
        String seedsFolder = args[2];

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ignored) {
            System.out.println("SQLite JDBC driver not on classpath; continuing without database execution.");
            return;
        }

        DBLoader loader = new DBLoader(dbFile);
        loader.executeSqlFile(schemaFile);
        System.out.println("Schema created.");

        Map<String,String[]> tableToCols = new LinkedHashMap<>();
        tableToCols.put("locations", new String[]{"locationId","name","area","type","latitude","longitude"});
        tableToCols.put("roads", new String[]{"fromLocationId","toLocationId","distance","travelTime","roadConditionWeight"});
        tableToCols.put("service_requests", new String[]{"requestId","source","destination","category","urgency","timeSubmitted","deadline","status"});
        tableToCols.put("resources", new String[]{"resourceId","type","homeLocation","capacity","availabilityStatus"});

        for (Map.Entry<String,String[]> e : tableToCols.entrySet()) {
            String csv = Paths.get(seedsFolder, e.getKey() + ".csv").toString();
            if (Files.exists(Paths.get(csv))) {
                System.out.println("Importing " + csv + " -> " + e.getKey());
                loader.importCsv(e.getKey(), csv, e.getValue());
            } else {
                System.out.println("Seed CSV not found, skipping: " + csv);
            }
        }

        BTree<String,String> btree = new BTree<>(3);
        HashTable<String,String> htable = new HashTable<>(101, HashTable.Mode.CHAINING);
        loader.loadIntoStructures(dbFile, btree, htable);
        System.out.println("Loaded locations into B-tree and HashTable. Root keys: " + btree.dumpRootKeys());

        long start = System.nanoTime();

        long end = System.nanoTime();
        long timeNs = end - start;
        long memoryKb = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024;
        loader.recordRun("BTree_insert_demo", 10, timeNs, memoryKb);
        System.out.println("Recorded demo algorithm run.");
    }
}
