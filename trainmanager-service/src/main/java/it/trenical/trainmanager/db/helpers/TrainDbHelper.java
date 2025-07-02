package it.trenical.trainmanager.db.helpers;

import it.trenical.trainmanager.models.TrainEntity;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class TrainDbHelper {
    private TrainDbHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static void createTable(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS Train (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255) NOT NULL,
                    typeName VARCHAR(255) NOT NULL,
                    departureTime VARCHAR(255) NOT NULL,
                    pathId INT NOT NULL,
                    FOREIGN KEY (typeName) REFERENCES TrainType(name)
                    );
               CREATE TABLE IF NOT EXISTS TrainSeat (
                    train_id INT NOT NULL,
                    className VARCHAR(255) NOT NULL,
                    count INT NOT NULL,
                    PRIMARY KEY (train_id, className),
                    FOREIGN KEY (train_id) REFERENCES Train(id),
                    FOREIGN KEY (className) REFERENCES ServiceClass(name)
               );
               CREATE TABLE IF NOT EXISTS TrainStationPlatform (
                    train_id INT NOT NULL,
                    stationName VARCHAR(255) NOT NULL,
                    number INT NOT NULL,
                    PRIMARY KEY (train_id, stationName),
                    FOREIGN KEY (train_id) REFERENCES Train(id)
               );
               """;
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        }
    }

    public static int insertTrain(
            Connection connection,
            String name,
            String typeName,
            long departureTime,
            int pathId) throws SQLException {
        String sql = """
                INSERT INTO Train (name, typeName, departureTime, pathId) VALUES (?, ?, ?, ?)
                """;
        try(PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, name);
            ps.setString(2, typeName);
            ps.setLong(3, departureTime);
            ps.setInt(4, pathId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(!rs.next())
                throw new SQLException("Failed to insert new train");
            return rs.getInt(1);
        }
    }

    public static void insertSeats(Connection connection, int trainId, Map<String, Integer> seats) throws SQLException {
        String sql = "INSERT INTO TrainSeat (train_id, className, count) VALUES (?, ?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, trainId);
            for (Map.Entry<String, Integer> entry : seats.entrySet()) {
                stmt.setString(2, entry.getKey());
                stmt.setInt(3, entry.getValue());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public static void insertPlatformChoices(Connection connection, int trainId, Map<String, Integer> stPlatform) throws SQLException {
        String sql = "INSERT INTO TrainStationPlatform (train_id, stationName, number) VALUES (?, ?, ?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, trainId);
            for (Map.Entry<String, Integer> entry : stPlatform.entrySet()) {
                stmt.setString(2, entry.getKey());
                stmt.setInt(3, entry.getValue());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    public static TrainEntity getTrain(Connection connection, int id) throws SQLException {
        TrainEntity.Builder builder = TrainEntity.builder().setId(id);
        String sql = "SELECT * FROM Train WHERE id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                if (!rs.next())
                    return null;
                builder.setName(rs.getString("name"));
                builder.setType(rs.getString("typeName"));
                builder.setDepartureTime(rs.getLong("departureTime"));
                builder.setPathId(rs.getInt("pathId"));
            }
        }
        builder.setClassSeats(getSeats(connection, id));
        builder.setPlatformChoice(getChosenPlatform(connection, id));
        return builder.build();
    }

    public static Map<String, Integer> getSeats(Connection connection, int trainId) throws SQLException {
        String sql = "SELECT * FROM TrainSeat WHERE train_id = ?";
        Map<String, Integer> seats = new HashMap<>();
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, trainId);
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String serviceClass = rs.getString("className");
                    int count = rs.getInt("count");
                    seats.put(serviceClass, count);
                }
            }
        }
        return seats;
    }

    public static Map<String, Integer> getChosenPlatform(Connection connection, int trainId) throws SQLException {
        String sql = "SELECT * FROM TrainStationPlatform WHERE train_id = ?";
        Map<String, Integer> ret = new HashMap<>();
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, trainId);
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String serviceClass = rs.getString("stationName");
                    int count = rs.getInt("number");
                    ret.put(serviceClass, count);
                }
            }
        }
        return ret;
    }

}
