package it.trenical.trainmanager.repository.db;

import it.trenical.server.database.DatabaseManager;
import it.trenical.trainmanager.models.TrainType;
import it.trenical.trainmanager.repository.TrainTypeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;

public class TrainTypeJdbcRepository implements TrainTypeRepository {
    private final DatabaseManager db;

    public TrainTypeJdbcRepository(DatabaseManager db) {
        this.db = db;
        db.withConnection(this::createTable);
    }

    @Override
    public boolean save(TrainType trainType) {
        return db.withConnection(connection -> {
            String sql = "INSERT INTO TrainType (name, speed, costPerKm) VALUES (?,?,?)";
            try(PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setString(1, trainType.name());
                stmt.setDouble(2, trainType.speed());
                stmt.setDouble(3, trainType.costPerKm());
                return stmt.executeUpdate() > 0;
            }
        });
    }

    @Override
    public Optional<TrainType> findByName(String name) {
        return db.withConnection(connection -> {
            String sql = "SELECT * FROM TrainType WHERE name = ?";
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, name);
                try(ResultSet rs = stmt.executeQuery()) {
                    if (rs.next())
                        return Optional.of(mapToTrainType(rs));
                    return Optional.empty();
                }
            }
        });
    }

    @Override
    public boolean deleteByName(String name) {
        return db.withConnection(connection -> {
            String sql = "DELETE FROM TrainType WHERE name = ?";
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, name);
                int r = stmt.executeUpdate();
                return r > 0;
            }
        });
    }

    @Override
    public void findAll(Consumer<TrainType> consumer) {
        db.withConnection(connection -> {
            String sql = "SELECT * FROM TrainType";
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                try(ResultSet rs = stmt.executeQuery()) {
                    while (rs.next())
                        consumer.accept(mapToTrainType(rs));
                }
            }
        });
    }

    private TrainType mapToTrainType(ResultSet rs) throws SQLException {
        return new TrainType(
                rs.getString("name"),
                rs.getDouble("speed"),
                rs.getDouble("costPerKm")
        );
    }

    private void createTable(Connection conn) throws SQLException {
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
}
