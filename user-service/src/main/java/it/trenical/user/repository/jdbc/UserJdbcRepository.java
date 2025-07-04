package it.trenical.user.repository.jdbc;

import it.trenical.server.database.DatabaseManager;
import it.trenical.user.models.User;
import it.trenical.user.repository.UserRepository;

import java.sql.*;
import java.util.Optional;

public class UserJdbcRepository implements UserRepository {

    private final DatabaseManager db;

    public UserJdbcRepository(DatabaseManager db) {
        this.db = db;
        db.withConnection(this::createTable);
    }

    @Override
    public void save(User user) {
        db.withConnection(connection -> {
            insertUser(connection, user);
        });
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return db.withConnection(connection->{
            return Optional.ofNullable(getByUsername(connection, username));
        });
    }

    private void insertUser(Connection connection, User user) throws SQLException {
        String sql = "INSERT INTO UserEntity (username,passwordHash,firstName,lastName,type) VALUES (?, ?, ?, ?,?)";
        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.username());
            ps.setString(2, user.passwordHash());
            ps.setString(3, user.firstName());
            ps.setString(4, user.lastName());
            ps.setString(5, user.type().name());
            if (ps.executeUpdate() <= 0)
                throw new SQLException("Unable to insert user");
        }
    }

    private User getByUsername(Connection connection, String username) throws SQLException {
        String sql = "SELECT * FROM UserEntity WHERE username = ?";
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try(ResultSet rs = stmt.executeQuery()){
                if (!rs.next())
                    return null;
                return fromResultSet(rs);
            }
        }
    }

    private User fromResultSet(ResultSet rs) throws SQLException {
        return User.builder()
                .setUsername(rs.getString("username"))
                .setPasswordHash(rs.getString("passwordHash"))
                .setFirstName(rs.getString("firstName"))
                .setLastName(rs.getString("lastName"))
                .setType(User.Type.valueOf(rs.getString("type")))
                .build();
    }

    private void createTable(Connection connection) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS UserEntity (
                    username VARCHAR(255) PRIMARY KEY NOT NULL,
                    passwordHash VARCHAR(255) NOT NULL,
                    firstName VARCHAR(255) NOT NULL,
                    lastName VARCHAR(255) NOT NULL,
                    type VARCHAR(255) NOT NULL
                );
                """;
        try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.execute();
        }
    }

}
