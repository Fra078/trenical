package it.trenical.client.ticketry;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.client.ticketry.commands.QueryTripCommand;
import it.trenical.frontend.cli.Cli;
import it.trenical.ticketry.proto.TicketryServiceGrpc;

public class TicketryMenu extends Cli {

    private final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 8778).usePlaintext().build();
    private final TicketryServiceGrpc.TicketryServiceBlockingStub stub =
            TicketryServiceGrpc.newBlockingStub(channel);

    public TicketryMenu(String jwt) {
        super("Ticketry");
        registerCommands(new QueryTripCommand(stub));
    }

}
