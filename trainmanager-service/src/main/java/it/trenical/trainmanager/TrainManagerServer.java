package it.trenical.trainmanager;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class TrainManagerServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(5051)
                .build()
                .start();
        server.awaitTermination();
    }

}
