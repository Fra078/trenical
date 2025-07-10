package it.trenical.payment.repository.jdbc;

import it.trenical.payment.models.Transaction;
import it.trenical.payment.repository.TransactionRepository;
import it.trenical.server.database.DatabaseManager;

import java.sql.*;
import java.util.Optional;

public class TransactionJdbcRepository implements TransactionRepository {

    private final DatabaseManager db;

    public TransactionJdbcRepository(DatabaseManager db) {
        this.db = db;
        db.withConnection(this::createTable);
    }

    @Override
    public Transaction save(Transaction transaction) {
        String sql = """    
                        INSERT INTO TransactionEntity (userId, cc, amount, refund, date) VALUES (?, ?, ?, ?, ?)
                        """;
        return db.withConnection(connection -> {
            try(PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);) {
                stmt.setString(1, transaction.userId());
                stmt.setString(2, transaction.creditCard());
                stmt.setDouble(3, transaction.amount());
                stmt.setBoolean(4, transaction.refund());
                stmt.setDouble(5, transaction.date());
                stmt.executeUpdate();
                try(ResultSet rs = stmt.getGeneratedKeys()) {
                    rs.next();
                    return transaction.setId(rs.getInt(1));
                }
            }
        });
    }

    @Override
    public Optional<Transaction> findById(int id) {
        String sql = "SELECT * FROM TransactionEntity WHERE id = ?";
        return db.withConnection(connection -> {
            try(PreparedStatement stmt = connection.prepareStatement(sql);) {
                stmt.setInt(1, id);
                try(ResultSet rs = stmt.executeQuery()) {
                    if (rs.next())
                        return Optional.of(new Transaction(
                                id,
                                rs.getString("userId"),
                                rs.getString("cc"),
                                rs.getDouble("amount"),
                                rs.getBoolean("refund"),
                                rs.getLong("date")
                        ));
                    return Optional.empty();
                }
            }
        });
    }

    private void createTable(Connection connection) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS TransactionEntity (
                    id INT AUTO_INCREMENT,
                    userId VARCHAR(255) NOT NULL,
                    cc VARCHAR(16) NOT NULL,
                    amount DECIMAL(6,3) NOT NULL,
                    refund BOOLEAN NOT NULL,
                    date BIGINT NOT NULL
                );
                """;
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
        }
    }
}
