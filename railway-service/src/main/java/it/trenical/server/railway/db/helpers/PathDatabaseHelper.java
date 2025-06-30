package it.trenical.server.railway.db.helpers;

import it.trenical.server.railway.models.Link;
import it.trenical.server.railway.models.Path;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public final class PathDatabaseHelper {

    private PathDatabaseHelper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void createTables(Connection connection) throws SQLException {
        String pathsSql = """
                        CREATE TABLE IF NOT EXISTS Path (
                            id INT AUTO_INCREMENT PRIMARY KEY
                        )
                       """;
        String stopsSql = """
                            CREATE TABLE IF NOT EXISTS PathItem (
                                path_id INT,
                                stop_index INT,
                                departure VARCHAR(255),
                                arrival VARCHAR(255),
                                PRIMARY KEY (path_id, stop_index),
                                FOREIGN KEY (path_id) REFERENCES Path(id),
                                FOREIGN KEY (departure,arrival) REFERENCES LINK(station1, station2)
                            )""";
        try(PreparedStatement stmtPaths = connection.prepareStatement(pathsSql);
            PreparedStatement stmtStops = connection.prepareStatement(stopsSql)){
            stmtPaths.execute();
            stmtStops.execute();
        }
    }

    public static Integer insertPath(Connection connection, List<String> stations) throws SQLException {
        if (stations.size() < 2)
            return null;
        String insertPath = "INSERT INTO Path DEFAULT VALUES";
        try(PreparedStatement pathStmt = connection.prepareStatement(insertPath, Statement.RETURN_GENERATED_KEYS)){
            pathStmt.executeUpdate();
            ResultSet keys = pathStmt.getGeneratedKeys();
            if (!keys.next())
                throw new SQLException("Insert path failed");
            int pathId = keys.getInt(1);


            String insertStop = "INSERT INTO PathItem (path_id, stop_index, departure, arrival) VALUES (?, ?, ?,?)";
            try(PreparedStatement stmtStops = connection.prepareStatement(insertStop)) {
                stmtStops.setInt(1, pathId);
                for (int i = 0; i < stations.size()-1; i++) {
                    stmtStops.setInt(2, i);
                    stmtStops.setString(3, stations.get(i));
                    stmtStops.setString(4, stations.get(i+1));
                    stmtStops.addBatch();
                }
                stmtStops.executeBatch();
            }
            return pathId;
        }
    }

    public static Path getPath(Connection connection, int pathId) throws SQLException {
        String sql = """
                        SELECT pi.departure, pi.arrival, l.distance
                        FROM PathItem pi, Link l
                        WHERE pi.path_id = ? AND pi.departure = l.station1 AND pi.arrival = l.station2
                        ORDER BY pi.stop_index
                      """;
        List<Link> links = new ArrayList<>();
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, pathId);
            try(ResultSet rs = stmt.executeQuery()){
                while (rs.next()) {
                    String departure = rs.getString("departure");
                    String arrival = rs.getString("arrival");
                    double distance = rs.getDouble("distance");
                    links.add(new Link(pathId, departure, arrival, distance));
                }
            }
        }
        return new Path(pathId, links);
    }

}
