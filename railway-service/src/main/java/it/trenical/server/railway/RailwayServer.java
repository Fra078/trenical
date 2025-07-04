package it.trenical.server.railway;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.trenical.server.database.DatabaseManager;
import it.trenical.server.railway.managers.PathManager;
import it.trenical.server.railway.managers.StationManager;
import it.trenical.server.railway.repositories.PathRepository;
import it.trenical.server.railway.repositories.StationRepository;
import it.trenical.server.railway.repositories.jdbc.PathJdbcRepository;
import it.trenical.server.railway.repositories.jdbc.StationJdbcRepository;
import it.trenical.server.railway.services.RailwayService;

import java.io.IOException;

public class RailwayServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        DatabaseManager db = new DatabaseManager("./db/railway-db");
        StationRepository stationRepository = new StationJdbcRepository(db);
        PathRepository pathRepository = new PathJdbcRepository(db);
        StationManager stationManager = new StationManager(stationRepository);
        PathManager pathManager = new PathManager(pathRepository);

        Server server = ServerBuilder.forPort(5050)
                .addService(new RailwayService(stationManager, pathManager))
                .build()
                .start();
        server.awaitTermination();
    }
}
