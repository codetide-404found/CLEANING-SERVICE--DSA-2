package db;

import db.model.AuditEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AuditEventDAO {

    public int insert(AuditEvent event) throws SQLException {
        String sql = "INSERT INTO audit_events (eventType, description, relatedEntityId, performedBy, eventTime) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, event.eventType);
            ps.setString(2, event.description);
            ps.setString(3, event.relatedEntityId);
            ps.setString(4, event.performedBy);
            ps.setString(5, event.eventTime);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public int logEvent(String eventType, String description, String relatedEntityId, String performedBy) throws SQLException {
        String now = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return insert(new AuditEvent(eventType, description, relatedEntityId, performedBy, now));
    }

    public List<AuditEvent> findAll() throws SQLException {
        List<AuditEvent> out = new ArrayList<>();
        String sql = "SELECT * FROM audit_events ORDER BY eventId";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(map(rs));
            }
        }
        return out;
    }

    public AuditEvent findMostRecent() throws SQLException {
        String sql = "SELECT * FROM audit_events ORDER BY eventId DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? map(rs) : null;
        }
    }

    public int delete(int eventId) throws SQLException {
        String sql = "DELETE FROM audit_events WHERE eventId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            return ps.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM audit_events";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private AuditEvent map(ResultSet rs) throws SQLException {
        AuditEvent e = new AuditEvent(
                rs.getString("eventType"),
                rs.getString("description"),
                rs.getString("relatedEntityId"),
                rs.getString("performedBy"),
                rs.getString("eventTime")
        );
        e.eventId = rs.getInt("eventId");
        return e;
    }
}
