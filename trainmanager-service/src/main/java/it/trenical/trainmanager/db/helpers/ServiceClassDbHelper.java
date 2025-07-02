package it.trenical.trainmanager.db.helpers;

import it.trenical.trainmanager.models.ServiceClassModel;
import it.trenical.trainmanager.models.TrainType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;

public class ServiceClassDbHelper {
    private ServiceClassDbHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static void createTable(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS ServiceClass (
                name VARCHAR(255) PRIMARY KEY,
                incrementFactor DECIMAL(6,3) NOT NULL)
               """;
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        }
    }

    public static boolean register(Connection connection, ServiceClassModel type) throws SQLException {
        String sql = "INSERT INTO ServiceClass (name, incrementFactor) VALUES (?,?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1,type.name());
            stmt.setDouble(2, type.incrementFactor());
            int r = stmt.executeUpdate();
            return r > 0;
        }
    }

    public static ServiceClassModel get(Connection connection, String name) throws SQLException {
        String sql = "SELECT * FROM ServiceClass WHERE name = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            try(ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return map(rs);
                return null;
            }
        }
    }

    public static void getAll(Connection connection, Consumer<ServiceClassModel> consumer) throws SQLException {
        String sql = "SELECT * FROM ServiceClass";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            try(ResultSet rs = stmt.executeQuery()) {
                while (rs.next())
                    consumer.accept(map(rs));
            }
        }
    }

    public static boolean delete(Connection connection, String name) throws SQLException {
        String sql = "DELETE FROM ServiceClass WHERE name = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            int r = stmt.executeUpdate();
            return r > 0;
        }
    }

    public static ServiceClassModel map(ResultSet rs) throws SQLException {
        return new ServiceClassModel(
                rs.getString("name"),
                rs.getDouble("incrementFactor")
        );
    }

}
