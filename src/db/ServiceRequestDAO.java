package db;

import db.model.ServiceRequestRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceRequestDAO {

    public void insert(ServiceRequestRecord r) throws SQLException {
        String sql = "INSERT INTO service_requests (requestId, source, destination, category, urgency, timeSubmitted, deadline, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            bind(ps, r);
            ps.executeUpdate();
        }
    }

    public ServiceRequestRecord findById(String requestId) throws SQLException {
        String sql = "SELECT * FROM service_requests WHERE requestId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<ServiceRequestRecord> findAll() throws SQLException {
        List<ServiceRequestRecord> out = new ArrayList<>();
        String sql = "SELECT * FROM service_requests ORDER BY timeSubmitted";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(map(rs));
            }
        }
        return out;
    }

    public List<ServiceRequestRecord> findByStatus(String status) throws SQLException {
        List<ServiceRequestRecord> out = new ArrayList<>();
        String sql = "SELECT * FROM service_requests WHERE status = ? ORDER BY urgency DESC, timeSubmitted";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(map(rs));
                }
            }
        }
        return out;
    }

    public int update(ServiceRequestRecord r) throws SQLException {
        String sql = "UPDATE service_requests SET source = ?, destination = ?, category = ?, urgency = ?, timeSubmitted = ?, deadline = ?, status = ? WHERE requestId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.source);
            ps.setString(2, r.destination);
            ps.setString(3, r.category);
            ps.setInt(4, r.urgency);
            ps.setString(5, r.timeSubmitted);
            ps.setString(6, r.deadline);
            ps.setString(7, r.status);
            ps.setString(8, r.requestId);
            return ps.executeUpdate();
        }
    }

    public int updateStatus(String requestId, String newStatus) throws SQLException {
        String sql = "UPDATE service_requests SET status = ? WHERE requestId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setString(2, requestId);
            return ps.executeUpdate();
        }
    }

    public int delete(String requestId) throws SQLException {
        String sql = "DELETE FROM service_requests WHERE requestId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, requestId);
            return ps.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM service_requests";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private void bind(PreparedStatement ps, ServiceRequestRecord r) throws SQLException {
        ps.setString(1, r.requestId);
        ps.setString(2, r.source);
        ps.setString(3, r.destination);
        ps.setString(4, r.category);
        ps.setInt(5, r.urgency);
        ps.setString(6, r.timeSubmitted);
        ps.setString(7, r.deadline);
        ps.setString(8, r.status);
    }

    private ServiceRequestRecord map(ResultSet rs) throws SQLException {
        return new ServiceRequestRecord(
                rs.getString("requestId"),
                rs.getString("source"),
                rs.getString("destination"),
                rs.getString("category"),
                rs.getInt("urgency"),
                rs.getString("timeSubmitted"),
                rs.getString("deadline"),
                rs.getString("status")
        );
    }
}
