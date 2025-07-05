package it.trenical.ticketry;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.trenical.ticketry.clients.TrainClient;
import it.trenical.ticketry.clients.grpc.TrainGrpcClient;
import it.trenical.ticketry.managers.TripManager;
import it.trenical.ticketry.services.TicketService;

import java.io.IOException;

public class TicketryServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        TrainClient trainClient = new TrainGrpcClient();
        TripManager tripManager = new TripManager(trainClient);
        Server server = ServerBuilder.forPort(8778)
                .addService(new TicketService(tripManager))
                .build().start();
        System.out.println("Server started on port " + server.getPort());
        server.awaitTermination();
    }
}
