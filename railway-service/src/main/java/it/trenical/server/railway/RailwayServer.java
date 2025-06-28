package it.trenical.server.railway;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.trenical.server.railway.services.AdminRailwayService;
import it.trenical.server.railway.services.RailwayService;

import java.io.IOException;

public class RailwayServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = ServerBuilder.forPort(5050)
                .addService(new RailwayService())
                .build()
                .start();

        Server adminServer = ServerBuilder.forPort(5051)
                .addService(new AdminRailwayService())
                .build()
                .start();

        server.awaitTermination();
        adminServer.awaitTermination();
    }
}
