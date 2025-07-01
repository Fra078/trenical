package it.trenical.trainmanager;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.trenical.trainmanager.service.TrainManagerService;

import java.io.IOException;

public class TrainManagerServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(5051)
                .addService(new TrainManagerService())
                .build()
                .start();
        server.awaitTermination();
    }

}
