package it.trenical.server.railway.db.helpers;

import it.trenical.server.railway.models.Station;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinkDatabaseHelper {
    private LinkDatabaseHelper(){
        throw new UnsupportedOperationException("Utility class");
    }

    public static void createTable(Connection connection) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS Link (
                    station1 VARCHAR(255) NOT NULL,
                    station2 VARCHAR(255) NOT NULL,
                    distance DECIMAL(6,2) NOT NULL,
                    PRIMARY KEY (station1, station2),
                    FOREIGN KEY (station1) REFERENCES Station(name),
                    FOREIGN KEY (station2) REFERENCES Station(name),
                    CHECK (station1 < station2)
                );
               """;
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.execute();
        }
    }

    public static boolean insertLink(Connection conn, String stationA, String stationB, double distance) throws SQLException {

        String station1Name = min(stationA, stationB);
        String station2Name = max(stationA, stationB);

        String sql = "INSERT INTO Link (station1, station2, distance) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, station1Name);
            stmt.setString(2, station2Name);
            stmt.setDouble(3, distance);
            return stmt.executeUpdate() > 0;

        }
    }

    public static Double getDistance(Connection connection, String stationA, String stationB) throws SQLException {
        String station1 = min(stationA, stationB);
        String station2 = max(stationA, stationB);

        String sql = "SELECT distance FROM Link WHERE station1 = ? AND station2 = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, station1);
            stmt.setString(2, station2);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getDouble("distance");
                else
                    return null;
            }
        }
    }

    public static boolean removeLink(Connection connection, String stationA, String stationB) throws SQLException {
        String station1 = min(stationA, stationB);
        String station2 = max(stationA, stationB);

        String sql = "DELETE FROM Link WHERE station1 = ? AND station2 = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, station1);
            stmt.setString(2, station2);
            return stmt.executeUpdate() > 0;
        }
    }

    public static Map<String, Double> getNeighbours(Connection connection, String station) throws SQLException {
        String sql = """
                    SELECT s.name as station, l.distance as distance
                    FROM Station s, Link l
                    WHERE (l.station1 = ? AND l.station2 = s.name)
                          OR (l.station2 = ? AND l.station1 = s.name)
                    """;
        Map<String, Double> result = new HashMap<>();
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, station);
            stmt.setString(2, station);
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()){
                    String stationName = rs.getString("station");
                    double distance = rs.getDouble("distance");
                    result.put(stationName, distance);
                }
            }
        }
        return result;
    }

    private static String max(String s1, String s2){
        if (s1.compareToIgnoreCase(s2) > 0)
            return s1;
        return s2;
    }

    private static String min(String s1, String s2){
        if (s1.compareToIgnoreCase(s2) > 0)
            return s2;
        return s1;
    }
}
