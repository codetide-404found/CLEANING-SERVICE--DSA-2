package db;

import db.model.Resource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ResourceDAO {

    public void insert(Resource r) throws SQLException {
        String sql = "INSERT INTO resources (resourceId, type, homeLocation, capacity, availabilityStatus) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.resourceId);
            ps.setString(2, r.type);
            ps.setString(3, r.homeLocation);
            ps.setInt(4, r.capacity);
            ps.setString(5, r.availabilityStatus);
            ps.executeUpdate();
        }
    }

    public Resource findById(String resourceId) throws SQLException {
        String sql = "SELECT * FROM resources WHERE resourceId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, resourceId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Resource> findAll() throws SQLException {
        List<Resource> out = new ArrayList<>();
        String sql = "SELECT * FROM resources ORDER BY resourceId";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(map(rs));
            }
        }
        return out;
    }

    public List<Resource> findAvailable() throws SQLException {
        List<Resource> out = new ArrayList<>();
        String sql = "SELECT * FROM resources WHERE availabilityStatus = 'available'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(map(rs));
            }
        }
        return out;
    }

    public int update(Resource r) throws SQLException {
        String sql = "UPDATE resources SET type = ?, homeLocation = ?, capacity = ?, availabilityStatus = ? WHERE resourceId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, r.type);
            ps.setString(2, r.homeLocation);
            ps.setInt(3, r.capacity);
            ps.setString(4, r.availabilityStatus);
            ps.setString(5, r.resourceId);
            return ps.executeUpdate();
        }
    }

    public int delete(String resourceId) throws SQLException {
        String sql = "DELETE FROM resources WHERE resourceId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, resourceId);
            return ps.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM resources";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Resource map(ResultSet rs) throws SQLException {
        return new Resource(
                rs.getString("resourceId"),
                rs.getString("type"),
                rs.getString("homeLocation"),
                rs.getInt("capacity"),
                rs.getString("availabilityStatus")
        );
    }
}
