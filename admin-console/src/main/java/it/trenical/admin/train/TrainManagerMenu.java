package it.trenical.admin.train;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.admin.train.commands.serviceClass.GetServiceClassCommand;
import it.trenical.admin.train.commands.serviceClass.ListServiceClassesCommand;
import it.trenical.admin.train.commands.serviceClass.RegisterServiceClassCommand;
import it.trenical.admin.train.commands.serviceClass.RemoveServiceClassCommand;
import it.trenical.admin.train.commands.type.GetTrainTypeCommand;
import it.trenical.admin.train.commands.type.ListTrainTypesCommand;
import it.trenical.admin.train.commands.type.RegisterTrainTypeCommand;
import it.trenical.admin.train.commands.type.RemoveTrainTypeCommand;
import it.trenical.frontend.cli.Cli;

public class TrainManagerMenu extends Cli {
    private final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 5051)
            .usePlaintext()
            .build();
    public TrainManagerMenu() {
        super("TrainManager");
        registerCommands(
                new ListTrainTypesCommand(channel),
                new RegisterTrainTypeCommand(channel),
                new GetTrainTypeCommand(channel),
                new RemoveTrainTypeCommand(channel),
                new GetServiceClassCommand(channel),
                new ListServiceClassesCommand(channel),
                new RegisterServiceClassCommand(channel),
                new RemoveServiceClassCommand(channel)
        );
    }

    public static void main(String[] args) {
        new TrainManagerMenu().start();
    }
}
