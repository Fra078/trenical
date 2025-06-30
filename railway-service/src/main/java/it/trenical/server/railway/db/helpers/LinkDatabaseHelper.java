package it.trenical.server.railway.db.helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
                    FOREIGN KEY (station2) REFERENCES Station(name)
                );
               """;
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.execute();
        }
    }

    public static boolean insertLink(Connection conn, String stationA, String stationB, double distance) throws SQLException {
        String sql = "INSERT INTO Link (station1, station2, distance) VALUES (?, ?, ?)";
        conn.setAutoCommit(false);
        int[] result = new int[1];
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, stationA);
            stmt.setString(2, stationB);
            stmt.setDouble(3, distance);
            stmt.addBatch();
            stmt.setString(1, stationB);
            stmt.setString(2, stationA);
            stmt.setDouble(3, distance);
            stmt.addBatch();

            result = stmt.executeBatch();
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
        } finally {
            conn.setAutoCommit(true);
        }
        for (int j : result) {
            if (j == 0) return false;
        }
        return true;
    }

    public static Double getDistance(Connection connection, String stationA, String stationB) throws SQLException {
        String sql = "SELECT distance FROM Link WHERE station1 = ? AND station2 = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, stationA);
            stmt.setString(2, stationB);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getDouble("distance");
                else
                    return null;
            }
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public static boolean removeLink(Connection connection, String stationA, String stationB) throws SQLException {
        String sql = "DELETE FROM Link WHERE station1 = ? AND station2 = ?";
        int[] result = new int[1];
        connection.setAutoCommit(false);
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, stationA);
            stmt.setString(2, stationB);
            stmt.addBatch();
            stmt.setString(1, stationB);
            stmt.setString(2, stationA);
            stmt.addBatch();
            stmt.executeBatch();
            result = stmt.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
        for (int j : result)
            if (j == 0) return false;
        return true;
    }

    public static Map<String, Double> getNeighbours(Connection connection, String station) throws SQLException {
        String sql = """
                    SELECT s.name as station, l.distance as distance
                    FROM Station s, Link l
                    WHERE (l.station1 = ? AND l.station2 = s.name)
                    """;
        Map<String, Double> result = new HashMap<>();
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, station);
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
}
