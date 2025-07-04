package it.trenical.server.railway.repositories.jdbc;

import it.trenical.server.database.DatabaseManager;
import it.trenical.server.railway.repositories.StationRepository;
import it.trenical.server.railway.models.Station;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;

public class StationJdbcRepository implements StationRepository {

    private final DatabaseManager db;

    public StationJdbcRepository(DatabaseManager db) {
        this.db = db;
        db.withConnection(StationJdbcRepository::createTable);
    }

    @Override
    public void findAll(Consumer<Station> consumer) {
        String sql = "SELECT * FROM Station";
        db.withConnection(connection->{
            try(PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
                    while (rs.next())
                        consumer.accept(map(rs));
            }
        });
    }

    @Override
    public Optional<Station> get(String name) {
        String sql = "SELECT * FROM Station WHERE name = ?";
        return db.withConnection(connection->{
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, name);
                try(ResultSet rs = stmt.executeQuery()) {
                    if (rs.next())
                        return Optional.of(map(rs));
                    return Optional.empty();
                }
            }
        });
    }

    @Override
    public void save(Station station) {
        db.withConnection(connection->{
            String sql = "INSERT INTO Station (name, city, trackCount) VALUES (?,?,?)";
            try(PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setString(1,station.name());
                stmt.setString(2,station.city());
                stmt.setInt(3,station.trackCount());
                stmt.executeUpdate();
            }
        });
    }

    @Override
    public boolean delete(String name) {
        String sql = "DELETE FROM Station WHERE name = ?";
        return db.withConnection(connection->{
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, name);
                return stmt.executeUpdate() > 0;
            }
        });
    }

    @Override
    public Boolean insertLink(String stationA, String stationB, double distance) {
        String sql = "INSERT INTO Link (station1, station2, distance) VALUES (?, ?, ?)";
        return db.withConnection(false, connection->{
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, stationA);
                stmt.setString(2, stationB);
                stmt.setDouble(3, distance);
                stmt.addBatch();
                stmt.setString(1, stationB);
                stmt.setString(2, stationA);
                stmt.setDouble(3, distance);
                stmt.addBatch();
                int[] result = stmt.executeBatch();
                connection.commit();
                for (int n : result)
                    if (n<=0)
                        return false;
                return true;
            }
        });
    }


    @Override
    public boolean removeLink(String stationA, String stationB) {
        String sql = "DELETE FROM Link WHERE station1 = ? AND station2 = ?";
        return db.withConnection(false, connection -> {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, stationA);
                stmt.setString(2, stationB);
                stmt.addBatch();
                stmt.setString(1, stationB);
                stmt.setString(2, stationA);
                stmt.addBatch();
                stmt.executeBatch();
                int[] result = stmt.executeBatch();
                for(int n: result)
                    if (n<=0) return false;
                return true;
            }
        });
    }

    @Override
    public Map<String, Double> getNeighbours(String station) {
        String sql = """
                    SELECT s.name as station, l.distance as distance
                    FROM Station s, Link l
                    WHERE (l.station1 = ? AND l.station2 = s.name)
                    """;
        return db.withConnection(connection->{
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
        });
    }

    private Station map(ResultSet rs) throws SQLException {
        return new Station(
                rs.getString("name"),
                rs.getString("city"),
                rs.getInt("trackCount")
        );
    }

    public static void createTable(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS Station (
                name VARCHAR(255) PRIMARY KEY,
                city VARCHAR(255) NOT NULL ,
                trackCount INT NOT NULL);
               CREATE TABLE IF NOT EXISTS Link (
                    station1 VARCHAR(255) NOT NULL,
                    station2 VARCHAR(255) NOT NULL,
                    distance DECIMAL(6,2) NOT NULL,
                    PRIMARY KEY (station1, station2),
                    FOREIGN KEY (station1) REFERENCES Station(name),
                    FOREIGN KEY (station2) REFERENCES Station(name)
                );
               """;
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        }
    }
}
