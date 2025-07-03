package it.trenical.trainmanager.repository.db;

import it.trenical.server.database.DatabaseManager;
import it.trenical.trainmanager.models.ServiceClassModel;
import it.trenical.trainmanager.models.TrainEntity;
import it.trenical.trainmanager.models.TrainQueryParams;
import it.trenical.trainmanager.repository.TrainRepository;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class TrainJdbcRepository implements TrainRepository {

    private final DatabaseManager db;

    public TrainJdbcRepository(DatabaseManager db) {
        this.db = db;
        db.withConnection(this::createTables);
    }

    @Override
    public TrainEntity save(TrainEntity train){
        int id = db.withConnection(false, connection -> {
            int trainId = insertTrain(connection, train);
            insertSeats(connection, trainId, train.classSeats());
            insertPlatformChoices(connection, trainId, train.platformChoice());

            connection.commit();
            return trainId;
        });
        return TrainEntity.builder(train).setId(id).build();
    }

    @Override
    public Optional<TrainEntity> findById(int id) {
        return db.withConnection(connection -> {
                return getTrain(connection, id);
        });
    }

    @Override
    public void findAll(TrainQueryParams params, Consumer<TrainEntity> train) {
        db.withConnection(connection -> {
            getAll(connection, train, params);
        });
    }

    private int insertTrain(Connection connection, TrainEntity train) throws SQLException {
        String sql = "INSERT INTO Train (name, typeName, departureTime, pathId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, train.name());
            ps.setString(2, train.type());
            ps.setLong(3, train.departureTime());
            ps.setInt(4, train.pathId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next())
                throw new SQLException("Fallimento nell'inserimento del nuovo treno");
            return rs.getInt(1);
        }
    }

    private void insertSeats(Connection connection, int trainId, Map<ServiceClassModel, Integer> seats) throws SQLException {
        String sql = "INSERT INTO TrainSeat (train_id, className, count) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, trainId);
            for (Map.Entry<ServiceClassModel, Integer> entry : seats.entrySet()) {
                stmt.setString(2, entry.getKey().name());
                stmt.setInt(3, entry.getValue());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void insertPlatformChoices(Connection connection, int trainId, Map<String, Integer> stPlatform) throws SQLException {
        String sql = "INSERT INTO TrainStationPlatform (train_id, stationName, number) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, trainId);
            for (Map.Entry<String, Integer> entry : stPlatform.entrySet()) {
                stmt.setString(2, entry.getKey());
                stmt.setInt(3, entry.getValue());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    private void getAll(
            Connection connection,
            Consumer<TrainEntity> consumer,
            TrainQueryParams params
    ) throws SQLException {
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM Train where 1=1");

        if (params.type() != null)
            sqlBuilder.append(" and typeName = ?");
        if (params.dateFrom() != null && params.dateTo() != null)
            sqlBuilder.append(" and departureTime >= ? and departureTime <= ?");
        if (params.pathId() != null)
            sqlBuilder.append(" and pathId = ?");
        if (params.serviceClass() != null){
            sqlBuilder.append(" and id IN ( SELECT train_id FROM TrainSeat WHERE className = ? )");
        }

        try (PreparedStatement ps = connection.prepareStatement(sqlBuilder.toString())) {
            int i = 1;
            if (params.type() != null)
                ps.setString(i++, params.type());
            if (params.dateFrom() != null && params.dateTo() != null) {
                ps.setLong(i++, params.dateFrom());
                ps.setLong(i++, params.dateTo());
            }
            if (params.pathId() != null)
                ps.setInt(i++, params.pathId());
            if (params.serviceClass() != null)
                ps.setString(i++, params.serviceClass());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TrainEntity.Builder builder = TrainEntity.builder()
                            .setId(rs.getInt("id"))
                            .setName(rs.getString("name"))
                            .setType(rs.getString("typeName"))
                            .setDepartureTime(rs.getLong("departureTime"))
                            .setPathId(rs.getInt("pathId"));
                    builder.setClassSeats(getSeats(connection, builder.getId()));
                    builder.setPlatformChoice(getChosenPlatform(connection, builder.getId()));
                    consumer.accept(builder.build());
                }
            }
        }
    }

    private Optional<TrainEntity> getTrain(Connection connection, int id) throws SQLException {
        TrainEntity.Builder builder = TrainEntity.builder().setId(id);
        String sql = "SELECT * FROM Train WHERE id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try(ResultSet rs = stmt.executeQuery()) {
                if (!rs.next())
                    return Optional.empty();
                builder.setName(rs.getString("name"));
                builder.setType(rs.getString("typeName"));
                builder.setDepartureTime(rs.getLong("departureTime"));
                builder.setPathId(rs.getInt("pathId"));
            }
        }
        builder.setClassSeats(getSeats(connection, id));
        builder.setPlatformChoice(getChosenPlatform(connection, id));
        return Optional.of(builder.build());
    }

    private Map<ServiceClassModel, Integer> getSeats(Connection connection, int trainId) throws SQLException {
        String sql = """
                 SELECT sc.name AS class_name,
                        sc.incrementFactor AS increment_factor,
                        ts.count AS seat_count
                 FROM TrainSeat ts
                 JOIN ServiceClass sc ON sc.name = ts.className
                 WHERE ts.train_id = ?
                 """;

        Map<ServiceClassModel, Integer> seats = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, trainId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String className = rs.getString("class_name");
                    double incrementFactor = rs.getDouble("increment_factor");
                    int count = rs.getInt("seat_count");

                    ServiceClassModel model = new ServiceClassModel(className, incrementFactor);
                    seats.put(model, count);
                }
            }
        }

        return seats;
    }

    private Map<String, Integer> getChosenPlatform(Connection connection, int trainId) throws SQLException {
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

    private void createTables(Connection conn) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS Train (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                typeName VARCHAR(255) NOT NULL,
                departureTime BIGINT NOT NULL,
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
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

}
