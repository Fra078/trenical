package it.trenical.admin.railway;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.admin.railway.commands.*;
import it.trenical.admin.railway.commands.path.GetPathCommand;
import it.trenical.admin.railway.commands.path.PathListCommand;
import it.trenical.admin.railway.commands.path.RegisterPathCommand;
import it.trenical.frontend.cli.Cli;
import it.trenical.proto.railway.RailwayServiceGrpc;

public class RailwayMenu extends Cli {
    private final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 5050)
            .usePlaintext()
            .build();
    private final RailwayServiceGrpc.RailwayServiceBlockingStub railwayStub
            = RailwayServiceGrpc.newBlockingStub(channel);

    public RailwayMenu() {
        super("Railway Manager");
        registerCommands(
                new StationListCommand(railwayStub),
                new StationCreateCommand(railwayStub),
                new StationGetCommand(railwayStub),
                new StationRemoveCommand(railwayStub),
                new StationLinkCommand(railwayStub),
                new StationUnlinkCommand(railwayStub),
                new NearStationsCommand(railwayStub),
                new StationLinkCommand(railwayStub),
                new StationUnlinkCommand(railwayStub),
                new RegisterPathCommand(railwayStub),
                new GetPathCommand(railwayStub),
                new PathListCommand(railwayStub)
        );
    }

    public static void main(String[] args) {
        new RailwayMenu().start();
    }
}
