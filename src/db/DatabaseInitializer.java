package db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static final String DEFAULT_SCHEMA_PATH = "sql/schema.sql";

    public static void initialize() throws SQLException, IOException {
        initialize(DEFAULT_SCHEMA_PATH);
    }

    public static void initialize(String schemaPath) throws SQLException, IOException {

        Path dbFile = Paths.get(DatabaseConnection.getDbPath());
        if (dbFile.getParent() != null) {
            Files.createDirectories(dbFile.getParent());
        }

        String schemaSql = new String(Files.readAllBytes(Paths.get(schemaPath)));

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (Statement st = conn.createStatement()) {
                for (String statement : splitStatements(schemaSql)) {
                    if (!statement.isBlank()) {
                        st.executeUpdate(statement);
                    }
                }
                conn.commit();
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    private static String[] splitStatements(String sql) {
        StringBuilder cleaned = new StringBuilder();
        for (String line : sql.split("\n")) {
            String trimmed = line.trim();
            if (trimmed.startsWith("--") || trimmed.isEmpty()) {
                continue;
            }
            cleaned.append(line).append("\n");
        }
        return cleaned.toString().split(";");
    }

    public static void dropAllTables() throws SQLException {
        String[] tables = {"audit_events", "algorithm_runs", "resources", "service_requests", "roads", "locations"};
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement()) {
            for (String t : tables) {
                st.executeUpdate("DROP TABLE IF EXISTS " + t);
            }
        }
    }
}
