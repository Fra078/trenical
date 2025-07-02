package it.trenical.server.database;

import java.sql.*;
import java.util.function.Function;

public class DatabaseManager {
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";
    private static final String JDBC_BASEURL = "jdbc:h2:";

    private final String fileName;

    protected DatabaseManager(String fileName) {
        this.fileName = fileName;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_BASEURL+fileName, DB_USER, DB_PASSWORD);
    }

    public <O> O withConnection(ComplSqlAction<Connection, O> action) {
        try(Connection connection = getConnection()) {
            return action.execute(connection);
        } catch (SQLException exc){
            throw new RuntimeException(exc);
        }
    }

    public void withConnection(SqlAction<Connection> action) {
        try(Connection connection = getConnection()) {
            action.execute(connection);
        } catch (SQLException exc){
            throw new RuntimeException(exc);
        }
    }

    public <O> O withConnection(boolean autoCommit, ComplSqlAction<Connection, O> action) {
        return withConnection(connection -> {
            connection.setAutoCommit(autoCommit);
            return action.execute(connection);
        });
    }

    public void withConnection(boolean autoCommit, SqlAction<Connection> action) {
        withConnection(connection -> {
            connection.setAutoCommit(autoCommit);
            action.execute(connection);
        });
    }

    public interface SqlAction<T> {
        void execute(T statement) throws SQLException;
    }

    public interface ComplSqlAction<I,O> {
        O execute(I statement) throws SQLException;
    }
}
