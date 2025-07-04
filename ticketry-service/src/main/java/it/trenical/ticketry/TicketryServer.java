package it.trenical.ticketry;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class TicketryServer {

    public static void main(String[] args) {
        Server server = ServerBuilder.forPort(8778)
                //.addService()
                .build();
    }
}
