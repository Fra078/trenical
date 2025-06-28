package it.trenical.server.railway;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.trenical.server.railway.dao.StationDao;
import it.trenical.server.railway.repositories.StationRepository;
import it.trenical.server.railway.services.AdminRailwayService;
import it.trenical.server.railway.services.RailwayService;

import java.io.IOException;

public class RailwayServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        StationDao stationDao = new StationRepository();

        Server server = ServerBuilder.forPort(5050)
                .addService(new RailwayService(stationDao))
                .build()
                .start();

        Server adminServer = ServerBuilder.forPort(5051)
                .addService(new AdminRailwayService(stationDao))
                .build()
                .start();

        server.awaitTermination();
        adminServer.awaitTermination();
    }
}
