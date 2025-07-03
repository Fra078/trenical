package it.trenical.admin.train;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.admin.train.commands.serviceClass.GetServiceClassCommand;
import it.trenical.admin.train.commands.serviceClass.ListServiceClassesCommand;
import it.trenical.admin.train.commands.serviceClass.RegisterServiceClassCommand;
import it.trenical.admin.train.commands.serviceClass.RemoveServiceClassCommand;
import it.trenical.admin.train.commands.train.CreateTrainCommand;
import it.trenical.admin.train.commands.train.GetTrainCommand;
import it.trenical.admin.train.commands.train.ListTrainCommand;
import it.trenical.admin.train.commands.type.GetTrainTypeCommand;
import it.trenical.admin.train.commands.type.ListTrainTypesCommand;
import it.trenical.admin.train.commands.type.RegisterTrainTypeCommand;
import it.trenical.admin.train.commands.type.RemoveTrainTypeCommand;
import it.trenical.frontend.cli.Cli;
import it.trenical.proto.train.TrainManagerGrpc;

public class TrainManagerMenu extends Cli {
    private final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 5051)
            .usePlaintext()
            .build();
    private final TrainManagerGrpc.TrainManagerBlockingStub stub =
            TrainManagerGrpc.newBlockingStub(channel);
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
                new RemoveServiceClassCommand(channel),
                new CreateTrainCommand(channel),
                new GetTrainCommand(stub),
                new ListTrainCommand(stub)
        );
    }

    public static void main(String[] args) {
        new TrainManagerMenu().start();
    }
}
