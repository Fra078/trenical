package it.trenical.ticketry.repositories.jdbc;

import it.trenical.server.database.DatabaseManager;
import it.trenical.ticketry.models.Ticket;
import it.trenical.ticketry.repositories.TicketRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TicketJdbcRepository implements TicketRepository {

    private final DatabaseManager db;

    public TicketJdbcRepository(DatabaseManager db) {
        this.db = db;
        db.withConnection(this::createTable);
    }

    @Override
    public Optional<Ticket> findById(int trainId, int id) {
        return db.withConnection(connection -> {
            String sql = "SELECT * FROM Ticket WHERE trainId = ? AND id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, trainId);
                stmt.setInt(2, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(fromResultSet(rs));
                    }
                    return Optional.empty();
                }
            }
        });
    }

    @Override
    public List<Ticket> findByCustomerId(int customerId) {
        return db.withConnection(connection -> {
            List<Ticket> tickets = new ArrayList<>();
            String sql = "SELECT * FROM Ticket WHERE customerId = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, customerId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        tickets.add(fromResultSet(rs));
                    }
                }
            }
            return tickets;
        });
    }

    @Override
    public List<Ticket> findByTrainId(int trainId) {
        return db.withConnection(connection -> {
            List<Ticket> tickets = new ArrayList<>();
            String sql = "SELECT * FROM Ticket WHERE trainId = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, trainId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        tickets.add(fromResultSet(rs));
                    }
                }
            }
            return tickets;
        });
    }

    @Override
    public Map<String, Integer> countSeatsForTrain(int trainId) {
        return db.withConnection(connection -> {
            Map<String, Integer> counts = new HashMap<>();
            String sql = "SELECT className, COUNT(*) as count FROM Ticket WHERE trainId = ? GROUP BY className";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, trainId);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        counts.put(rs.getString("className"), rs.getInt("count"));
                    }
                }
            }
            return counts;
        });
    }

    @Override
    public List<Ticket> addTicketIfPossible(Ticket ticket, int count, int maxClassCount) {
        String countSql = """
                SELECT COUNT(*)
                FROM Ticket
                WHERE train_id = ? AND className = ?
                """;

        String insertSql = """
                INSERT INTO Ticket (trainId, className, promoId, customerId, departure, arrival, status) 
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        return db.withConnection(false, connection -> {
            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            try(PreparedStatement stmt = connection.prepareStatement(countSql)){
                stmt.setInt(1, ticket.getTrainId());
                stmt.setString(2, ticket.getClassName());
                try(ResultSet rs = stmt.executeQuery()){
                    rs.next();
                    int currentCount = rs.getInt(1);
                    if(count > maxClassCount - currentCount) {
                        return List.of();
                    }
                }
            }
            try(PreparedStatement stmt = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)){
                stmt.setInt(1, ticket.getTrainId());
                stmt.setString(2, ticket.getClassName());
                stmt.setString(3, ticket.getPromoId());
                stmt.setString(4, ticket.getCustomerId());
                stmt.setString(5, ticket.getDeparture());
                stmt.setString(6, ticket.getArrival());
                stmt.setString(7, ticket.getStatus().toString());
                int r = stmt.executeUpdate();
                if (r != count) {
                    connection.rollback();
                    throw new SQLException("Insertion failed");
                }
                ResultSet rs = stmt.getGeneratedKeys();
                List<Ticket> tickets = new ArrayList<>();
                while (rs.next()) {
                    Ticket t = Ticket.newBuilder(ticket).id(rs.getInt(1)).build();
                    tickets.add(t);
                }
                return tickets;
            }
        });
    }

    private Ticket fromResultSet(ResultSet rs) throws SQLException {
        return Ticket.newBuilder()
                .id(rs.getInt("id"))
                .trainId(rs.getInt("trainId"))
                .className(rs.getString("className"))
                .transactionId(rs.getString("transactionId"))
                .promoId(rs.getString("promoId"))
                .customerId(rs.getString("customerId"))
                .departure(rs.getString("departure"))
                .arrival(rs.getString("arrival"))
                .status(Ticket.Status.valueOf(rs.getString("status")))
                .build();
    }

    private void createTable(Connection connection) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS Ticket (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    trainId INT NOT NULL,
                    className VARCHAR(255) NOT NULL,
                    transactionId INT,
                    promoId VARCHAR(255),
                    customerId VARCHAR(255) NOT NULL,
                    departure VARCHAR(255) NOT NULL,
                    arrival VARCHAR(255) NOT NULL,
                    status VARCHAR(255) NOT NULL,
                    PRIMARY KEY (id, trainId)
                );
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
        }
    }
}