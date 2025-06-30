package it.trenical.server.railway.db.helpers;

import it.trenical.server.railway.models.Station;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StationDatabaseHelper {
    private StationDatabaseHelper(){
        throw new UnsupportedOperationException("Utility class");
    }

    public static void createTable(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS Station (
                name VARCHAR(255) PRIMARY KEY,
                city VARCHAR(255) NOT NULL ,
                trackCount INT NOT NULL)
               """;
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        }
    }

    public static boolean create(Connection connection, String name, String city, int trackCount) throws SQLException {
        String sql = "INSERT INTO Station (name, city, trackCount) VALUES (?,?,?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1,name);
            stmt.setString(2,city);
            stmt.setInt(3,trackCount);
            int r = stmt.executeUpdate();
            return r > 0;
        }
    }

    public static Optional<Station> get(Connection connection, String name) throws SQLException {
        String sql = "SELECT * FROM Station WHERE name = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return Optional.of(map(rs));
                return Optional.empty();
            }
        }
    }

    public static List<Station> getAll(Connection connection) throws SQLException {
        String sql = "SELECT * FROM Station";
        List<Station> result = new ArrayList<>();
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next())
                    result.add(map(rs));
            }
        }
        return result;
    }

    public static boolean delete(Connection connection, String name) throws SQLException {
        String sql = "DELETE FROM Station WHERE name = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            int r = stmt.executeUpdate();
            return r > 0;
        }
    }

    public static Station map(ResultSet rs) throws SQLException {
        return new Station(
                rs.getString("name"),
                rs.getString("city"),
                rs.getInt("trackCount")
        );
    }

}
