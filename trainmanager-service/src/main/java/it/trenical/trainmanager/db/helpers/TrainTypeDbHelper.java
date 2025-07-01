package it.trenical.trainmanager.db.helpers;

import it.trenical.trainmanager.models.TrainType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class TrainTypeDbHelper {
    private TrainTypeDbHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static void createTable(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS TrainType (
                name VARCHAR(255) PRIMARY KEY,
                speed DECIMAL(6,3) NOT NULL,
                costPerKm DECIMAL(6,3) NOT NULL)
               """;
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        }
    }

    public static boolean register(Connection connection, TrainType type) throws SQLException {
        String sql = "INSERT INTO TrainType (name, speed, costPerKm) VALUES (?,?,?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1,type.name());
            stmt.setDouble(2, type.speed());
            stmt.setDouble(3, type.costPerKm());
            int r = stmt.executeUpdate();
            return r > 0;
        }
    }

    public static Optional<TrainType> get(Connection connection, String name) throws SQLException {
        String sql = "SELECT * FROM TrainType WHERE name = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return Optional.of(map(rs));
                return Optional.empty();
            }
        }
    }

    public static void getAll(Connection connection, Consumer<TrainType> consumer) throws SQLException {
        String sql = "SELECT * FROM TrainType";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next())
                    consumer.accept(map(rs));
            }
        }
    }

    public static boolean delete(Connection connection, String name) throws SQLException {
        String sql = "DELETE FROM TrainType WHERE name = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            int r = stmt.executeUpdate();
            return r > 0;
        }
    }

    public static TrainType map(ResultSet rs) throws SQLException {
        return new TrainType(
                rs.getString("name"),
                rs.getDouble("speed"),
                rs.getDouble("costPerKm")
        );
    }

}
