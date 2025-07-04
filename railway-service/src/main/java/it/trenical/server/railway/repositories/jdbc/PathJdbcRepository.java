package it.trenical.server.railway.repositories.jdbc;

import it.trenical.server.database.DatabaseManager;
import it.trenical.server.railway.exceptions.LinkNotFoundException;
import it.trenical.server.railway.models.Path;
import it.trenical.server.railway.models.Station;
import it.trenical.server.railway.repositories.PathRepository;
import it.trenical.server.railway.repositories.StationRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PathJdbcRepository implements PathRepository {

    private final DatabaseManager db;

    public PathJdbcRepository(DatabaseManager db) {
        this.db = db;
        db.withConnection(this::createTables);
    }


    @Override
    public int registerPath(List<String> stations) throws LinkNotFoundException {
        return db.withConnection(false, connection -> {
            checkValidPath(connection, stations);
            int id = insertPath(connection);
            insertStops(connection, id, stations);
            connection.commit();
            return id;
        });
    }

    @Override
    public Optional<Path> getPath(int pathId) {
        return db.withConnection(connection -> {
            return getPath(connection, pathId);
        });
    }

    private Optional<Path> getPath(Connection connection, int pathId) throws SQLException {
        String sql = """
                        SELECT station.NAME, station.CITY, station.TRACKCOUNT
                        FROM Stop s, Station station
                        WHERE s.path_id = ? AND s.station = station.name
                        ORDER BY s.stop_index
                      """;
        List<Station> stations = new ArrayList<>();
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pathId);
            try(ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                do {
                    String name = rs.getString(1);
                    String city = rs.getString(2);
                    int trackCount = rs.getInt(3);
                    stations.add(new Station(name, city, trackCount));
                } while (rs.next());
            }
        }
        Path.Builder builder = Path.builder();
        builder.setId(pathId);
        builder.addLast(stations.getFirst(), 0);
        for (int i = 1; i < stations.size(); i++) {
            Optional<Double> dist = getDistance(connection, stations.get(i-1).name(),stations.get(i).name());
            if(dist.isEmpty()) return Optional.empty();
            builder.addLast(stations.get(i), dist.get());
        }
        return Optional.of(builder.build());
    }

    private int insertPath(Connection connection) throws SQLException {
        String insertPath = "INSERT INTO Path VALUES ()";
        try(PreparedStatement stmt = connection.prepareStatement(insertPath, Statement.RETURN_GENERATED_KEYS)) {
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Insert path failed");
        }
    }

    private void insertStops(Connection connection, int pathId, List<String> station) throws SQLException {
        String insertStop = "INSERT INTO Stop (path_id, stop_index,station) VALUES (?,?,?)";
        try(PreparedStatement stmt = connection.prepareStatement(insertStop)) {
            stmt.setInt(1, pathId);
            for (int i = 0; i <station.size(); i++) {
                stmt.setInt(2, i);
                stmt.setString(3, station.get(i));
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void checkValidPath(Connection connection, List<String> stations) throws SQLException {
        for (int i = 0; i < stations.size() - 1; i++) {
            String station1 = stations.get(i);
            String station2 = stations.get(i + 1);
            if (getDistance(connection, station1, station2).isEmpty())
                throw new LinkNotFoundException(station1, station2);
        }
    }

    private Optional<Double> getDistance(Connection connection, String station1, String station2) throws SQLException {
        String sql ="SELECT distance FROM Link WHERE station1=? AND station2=?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, station1);
            stmt.setString(2, station2);
            try(ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return Optional.empty();
                return Optional.of(rs.getDouble(1));
            }
        }
    }


    private void createTables(Connection connection) throws SQLException {
        String sql = """
                        CREATE TABLE IF NOT EXISTS Path (
                            id INT AUTO_INCREMENT PRIMARY KEY
                        );
                        CREATE TABLE IF NOT EXISTS Stop (
                                path_id INT,
                                stop_index INT,
                                station VARCHAR(255),
                                PRIMARY KEY (path_id, stop_index),
                                FOREIGN KEY (path_id) REFERENCES Path(id),
                                FOREIGN KEY (station) REFERENCES Station(NAME)
                            )
                       """;
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
        }
    }

}
