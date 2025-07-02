package it.trenical.trainmanager.repository.db;

import it.trenical.trainmanager.db.TrainDb;
import it.trenical.trainmanager.models.ServiceClassModel;
import it.trenical.trainmanager.repository.ServiceClassRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Consumer;

public class ServiceClassJdbcRepository implements ServiceClassRepository {
    private final TrainDb db;

    public ServiceClassJdbcRepository(TrainDb db) {
        this.db = db;
        db.withConnection(this::createTable);
    }

    @Override
    public boolean save(ServiceClassModel serviceClass) {
        return db.withConnection(connection -> {
            String sql = "INSERT INTO ServiceClass (name, incrementFactor) VALUES (?,?)";
            try(PreparedStatement stmt = connection.prepareStatement(sql)){
                stmt.setString(1,serviceClass.name());
                stmt.setDouble(2, serviceClass.incrementFactor());
                int r = stmt.executeUpdate();
                return r > 0;
            }
        });
    }

    @Override
    public Optional<ServiceClassModel> findByName(String name) {
        return db.withConnection(connection -> {
            String sql = "SELECT * FROM ServiceClass WHERE name = ?";
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, name);
                try(ResultSet rs = stmt.executeQuery()) {
                    if (rs.next())
                        return Optional.of(mapToModel(rs));
                    return Optional.empty();
                }
            }
        });
    }

    @Override
    public boolean deleteByName(String name) {
        return db.withConnection(connection -> {
            String sql = "DELETE FROM ServiceClass WHERE name = ?";
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, name);
                int r = stmt.executeUpdate();
                return r > 0;
            }
        });
    }

    @Override
    public void findAll(Consumer<ServiceClassModel> consumer) {
        db.withConnection(connection -> {
            String sql = "SELECT * FROM ServiceClass";
            try(PreparedStatement stmt = connection.prepareStatement(sql)) {
                try(ResultSet rs = stmt.executeQuery()) {
                    while (rs.next())
                        consumer.accept(mapToModel(rs));
                }
            }
        });
    }

    private void createTable(Connection conn) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS ServiceClass (
                name VARCHAR(255) PRIMARY KEY,
                incrementFactor DECIMAL(6,3) NOT NULL)
               """;
        try(PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.execute();
        }
    }

    private ServiceClassModel mapToModel(ResultSet rs) throws SQLException {
        return new ServiceClassModel(
                rs.getString("name"),
                rs.getDouble("incrementFactor")
        );
    }
}
