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
    public Ticket save(Ticket ticket) {
        return db.withConnection(connection -> {
            String sql = "INSERT INTO Ticket (trainId, className, transactionId, promoId, ownerName, customerId, departure, arrival) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, ticket.getTrainId());
                ps.setString(2, ticket.getClassName());
                ps.setString(3, ticket.getTransactionId());
                ps.setString(4, ticket.getPromoId());
                ps.setString(5, ticket.getOwnerName());
                ps.setInt(6, ticket.getCustomerId());
                ps.setString(7, ticket.getDeparture());
                ps.setString(8, ticket.getArrival());
                ps.executeUpdate();
                ResultSet rs = ps.getGeneratedKeys();
                if (!rs.next()) {
                    throw new SQLException("Salvataggio del biglietto fallito, nessun ID ottenuto.");
                }
                int generatedId = rs.getInt(1);
                return Ticket.newBuilder()
                        .id(generatedId)
                        .trainId(ticket.getTrainId())
                        .className(ticket.getClassName())
                        .transactionId(ticket.getTransactionId())
                        .promoId(ticket.getPromoId())
                        .ownerName(ticket.getOwnerName())
                        .customerId(ticket.getCustomerId())
                        .departure(ticket.getDeparture())
                        .arrival(ticket.getArrival())
                        .build();
            }
        });
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

    private Ticket fromResultSet(ResultSet rs) throws SQLException {
        return Ticket.newBuilder()
                .id(rs.getInt("id"))
                .trainId(rs.getInt("trainId"))
                .className(rs.getString("className"))
                .transactionId(rs.getString("transactionId"))
                .promoId(rs.getString("promoId"))
                .ownerName(rs.getString("ownerName"))
                .customerId(rs.getInt("customerId"))
                .departure(rs.getString("departure"))
                .arrival(rs.getString("arrival"))
                .build();
    }

    private void createTable(Connection connection) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS Ticket (
                    id INT AUTO_INCREMENT,
                    trainId INT NOT NULL,
                    className VARCHAR(255) NOT NULL,
                    transactionId VARCHAR(255),
                    promoId VARCHAR(255),
                    ownerName VARCHAR(255) NOT NULL,
                    customerId INT NOT NULL,
                    departure VARCHAR(255) NOT NULL,
                    arrival VARCHAR(255) NOT NULL,
                    PRIMARY KEY (id, trainId)
                );
                """;
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
        }
    }
}