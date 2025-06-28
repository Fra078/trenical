package it.trenical.server.railway.db;

import it.trenical.server.railway.models.Station;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class StationDb {

    private static final String JDBC_URL = "jdbc:h2:./railway-db";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    public StationDb() {
        createTable();
    }

    private void createTable() {
        doWithStatement(statement -> {
            String tbc =
                    """
                    CREATE TABLE IF NOT EXISTS Stations (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(255),
                    city VARCHAR(255),
                    trackCount INT)
                   """;
            statement.execute(tbc);
        });
    }

    public List<Station> getAll(){
        List<Station> stations = new ArrayList<>();
        doWithStatement(statement -> {
            try(ResultSet rs = statement.executeQuery("SELECT * FROM Stations")){
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String city = rs.getString("city");
                    int trackCount = rs.getInt("trackCount");
                    stations.add(new Station(id, name, city, trackCount));
                }
            }
        });
        return stations;
    }

    public Station get(int id) {
        AtomicReference<Station> result = new AtomicReference<>();
        doWithStatement(statement -> {
            try(ResultSet rs = statement.executeQuery("SELECT * FROM Stations WHERE id = " + id)){
                if (rs.next()) {
                    String name = rs.getString("name");
                    String city = rs.getString("city");
                    int trackCount = rs.getInt("trackCount");
                    result.set(new Station(id, name, city, trackCount));
                }
            }
        });
        return result.get();
    }

    public Station add(String name, String city, int trackCount) {
        doWithStatement(statement -> {
            statement.executeUpdate(
                "INSERT INTO Stations (name, city, trackCount) VALUES ("+
                            "'"+name+"',"+
                            "'"+city+"',"+
                            trackCount+")");
        });
        return null;
    }

    public Station remove(int id) {
        throw new UnsupportedOperationException("Non implementata");
    }

    private void doWithConnection(Function<Connection, Boolean> function) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)){
            function.apply(connection);
        } catch (SQLException e) {
            System.err.println("Errore durante l'interazione con il database:");
        }
    }

    private void doWithStatement(SqlFun function) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)){
            Statement statement = connection.createStatement();
            function.statementAction(statement);
        } catch (SQLException e) {
            System.err.println("Errore durante l'interazione con il database:");
        }
    }

    public interface SqlFun {
        void statementAction(Statement statement) throws SQLException;
    }

}
