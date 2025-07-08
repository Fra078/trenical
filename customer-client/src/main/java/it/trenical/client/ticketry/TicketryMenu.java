package it.trenical.client.ticketry;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.client.ticketry.commands.QueryTripCommand;
import it.trenical.frontend.authentication.BearerToken;
import it.trenical.frontend.cli.Cli;
import it.trenical.ticketry.proto.TicketryServiceGrpc;

public class TicketryMenu extends Cli {

    private BearerToken token;
    private final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 8778).usePlaintext().build();
    private final TicketryServiceGrpc.TicketryServiceBlockingStub stub;

    public TicketryMenu(String jwt) {
        super("Ticketry");
        token = new BearerToken(jwt);
        stub = TicketryServiceGrpc.newBlockingStub(channel).withCallCredentials(token);
        registerCommands(new QueryTripCommand(stub));
    }

}
