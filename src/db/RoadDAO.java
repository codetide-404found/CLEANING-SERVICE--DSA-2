package db;

import db.model.Road;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoadDAO {

    public int insert(Road road) throws SQLException {
        String sql = "INSERT INTO roads (fromLocationId, toLocationId, distance, travelTime, roadConditionWeight) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, road.fromLocationId);
            ps.setString(2, road.toLocationId);
            ps.setDouble(3, road.distance);
            ps.setDouble(4, road.travelTime);
            ps.setDouble(5, road.roadConditionWeight);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public List<Road> findAll() throws SQLException {
        List<Road> out = new ArrayList<>();
        String sql = "SELECT * FROM roads ORDER BY roadId";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                out.add(map(rs));
            }
        }
        return out;
    }

    public List<Road> findByFromLocation(String locationId) throws SQLException {
        List<Road> out = new ArrayList<>();
        String sql = "SELECT * FROM roads WHERE fromLocationId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, locationId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    out.add(map(rs));
                }
            }
        }
        return out;
    }

    public int update(Road road) throws SQLException {
        String sql = "UPDATE roads SET distance = ?, travelTime = ?, roadConditionWeight = ? WHERE roadId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, road.distance);
            ps.setDouble(2, road.travelTime);
            ps.setDouble(3, road.roadConditionWeight);
            ps.setInt(4, road.roadId);
            return ps.executeUpdate();
        }
    }

    public int delete(int roadId) throws SQLException {
        String sql = "DELETE FROM roads WHERE roadId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roadId);
            return ps.executeUpdate();
        }
    }

    public int count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM roads";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private Road map(ResultSet rs) throws SQLException {
        Road r = new Road(
                rs.getString("fromLocationId"),
                rs.getString("toLocationId"),
                rs.getDouble("distance"),
                rs.getDouble("travelTime"),
                rs.getDouble("roadConditionWeight")
        );
        r.roadId = rs.getInt("roadId");
        return r;
    }
}
