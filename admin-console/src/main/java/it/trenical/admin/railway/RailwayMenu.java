package it.trenical.admin.railway;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.admin.railway.commands.*;
import it.trenical.frontend.cli.Cli;

public class RailwayMenu extends Cli {
    private final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 5050)
            .usePlaintext()
            .build();
    public RailwayMenu() {
        super("Railway Manager");
        registerCommands(
                new StationListCommand(channel),
                new StationCreateCommand(channel),
                new StationGetCommand(channel),
                new StationRemoveCommand(channel),
                new StationLinkCommand(channel),
                new StationUnlinkCommand(channel),
                new NearStationsCommand(channel)
        );
    }

    public static void main(String[] args) {
        new RailwayMenu().start();
    }
}
