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


    protected void doWithStatement(String sqlCommand, SqlAction<PreparedStatement> function) {
        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(sqlCommand)
        ){
            function.execute(statement);
        } catch (SQLException e) {
            System.err.println("Errore durante l'interazione con il database:");
        }
    }

    protected void doWithStatement(SqlAction<Statement> function) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()
        ){
            function.execute(statement);
        } catch (SQLException e) {
            System.err.println("Errore durante l'interazione con il database:");
        }
    }

    protected <T> T executeQuery(
            String sqlCommand,
            SqlAction<PreparedStatement> setParams,
            SqlQueryAction<ResultSet, T> action
    ) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlCommand)
        ){
            setParams.execute(statement);
            ResultSet resultSet = statement.executeQuery();
            return action.execute(resultSet);
        } catch (SQLException e) {
            System.err.println("Errore durante l'interazione con il database:");
        }
        return null;
    }

    protected <T> T executeQuery(
            String sqlCommand,
            SqlQueryAction<ResultSet, T> action
    ) {
        return executeQuery(sqlCommand, st->{}, action);
    }

    public interface SqlAction<T> {
        void execute(T statement) throws SQLException;
    }

    public interface ComplSqlAction<I,O> {
        O execute(I statement) throws SQLException;
    }

    protected interface SqlQueryAction<T, R> {
        R execute(T statement) throws SQLException;
    }
}
