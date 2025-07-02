package it.trenical.admin.train.creator.commands;

import it.trenical.admin.train.creator.printers.TrainPrinter;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.RegisterTrainRequest;
import it.trenical.proto.train.TrainManagerGrpc;
import it.trenical.proto.train.TrainResponse;

public class ConfirmCommand extends Command {

    private final RegisterTrainRequest.Builder builder;
    private final TrainManagerGrpc.TrainManagerBlockingStub stub;
    public ConfirmCommand(RegisterTrainRequest.Builder builder, TrainManagerGrpc.TrainManagerBlockingStub stub) {
        super("confirm", "Conferma le informazioni inserite e registra il treno");
        this.builder = builder;
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        TrainResponse ts = stub.registerTrain(builder.build());
        TrainPrinter.printTrain(ts);
    }
}
