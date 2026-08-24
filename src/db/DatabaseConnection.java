package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public static final String DEFAULT_DB_PATH = "database/campus_operations.db";

    private static String dbPath = DEFAULT_DB_PATH;

    private DatabaseConnection() {

    }

    public static void setDbPath(String path) {
        dbPath = path;
    }

    public static String getDbPath() {
        return dbPath;
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found on classpath. " +
                    "Add sqlite-jdbc-<version>.jar to the lib/ folder.", e);
        }
        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

        conn.createStatement().execute("PRAGMA foreign_keys = ON;");
        return conn;
    }
}
