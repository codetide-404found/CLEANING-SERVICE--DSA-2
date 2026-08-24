package db;

import db.model.Location;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {

    public void insert(Location loc) throws SQLException {
        String sql = "INSERT INTO locations (locationId, name, area, type, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loc.locationId);
            ps.setString(2, loc.name);
            ps.setString(3, loc.area);
            ps.setString(4, loc.type);
            ps.setDouble(5, loc.latitude);
            ps.setDouble(6, loc.longitude);
            ps.executeUpdate();
        }
    }

    public Location findById(String locationId) throws SQLException {
        String sql = "SELECT * FROM locations WHERE locationId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, locationId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public List<Location> findAll() throws SQLException {
        List<Location> out = new ArrayList<>();
        String sql = "SELECT * FROM locations ORDER BY locationId";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(map(rs));
            }
        }
        return out;
    }

    public int update(Location loc) throws SQLException {
        String sql = "UPDATE locations SET name = ?, area = ?, type = ?, latitude = ?, longitude = ? WHERE locationId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loc.name);
            ps.setString(2, loc.area);
            ps.setString(3, loc.type);
            ps.setDouble(4, loc.latitude);
            ps.setDouble(5, loc.longitude);
            ps.setString(6, loc.locationId);
            return ps.executeUpdate();
        }
    }

    public int delete(String locationId) throws SQLException {
        String sql = "DELETE FROM locations WHERE locationId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, locationId);
            return ps.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM locations";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Location map(ResultSet rs) throws SQLException {
        return new Location(
                rs.getString("locationId"),
                rs.getString("name"),
                rs.getString("area"),
                rs.getString("type"),
                rs.getDouble("latitude"),
                rs.getDouble("longitude")
        );
    }
}
