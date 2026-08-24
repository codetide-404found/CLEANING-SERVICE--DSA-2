package db;

import db.model.AlgorithmRun;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AlgorithmRunDAO {

    public int insert(AlgorithmRun run) throws SQLException {
        String sql = "INSERT INTO algorithm_runs (algorithmName, inputSize, timeNs, memoryKb, dateRun) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, run.algorithmName);
            ps.setInt(2, run.inputSize);
            ps.setLong(3, run.timeNs);
            ps.setLong(4, run.memoryKb);
            ps.setString(5, run.dateRun);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public int recordRun(String algorithmName, int inputSize, long timeNs, long memoryKb) throws SQLException {
        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return insert(new AlgorithmRun(algorithmName, inputSize, timeNs, memoryKb, now));
    }

    public List<AlgorithmRun> findAll() throws SQLException {
        List<AlgorithmRun> out = new ArrayList<>();
        String sql = "SELECT * FROM algorithm_runs ORDER BY runId";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(map(rs));
            }
        }
        return out;
    }

    public List<AlgorithmRun> findByAlgorithm(String algorithmName) throws SQLException {
        List<AlgorithmRun> out = new ArrayList<>();
        String sql = "SELECT * FROM algorithm_runs WHERE algorithmName = ? ORDER BY inputSize";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, algorithmName);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(map(rs));
                }
            }
        }
        return out;
    }

    public int delete(int runId) throws SQLException {
        String sql = "DELETE FROM algorithm_runs WHERE runId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, runId);
            return ps.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM algorithm_runs";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private AlgorithmRun map(ResultSet rs) throws SQLException {
        AlgorithmRun run = new AlgorithmRun(
                rs.getString("algorithmName"),
                rs.getInt("inputSize"),
                rs.getLong("timeNs"),
                rs.getLong("memoryKb"),
                rs.getString("dateRun")
        );
        run.runId = rs.getInt("runId");
        return run;
    }
}
