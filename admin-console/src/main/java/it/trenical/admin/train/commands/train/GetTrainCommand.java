package it.trenical.admin.train.commands.train;

import it.trenical.admin.train.creator.printers.TrainPrinter;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.TrainId;
import it.trenical.proto.train.TrainManagerGrpc;
import it.trenical.proto.train.TrainResponse;

public class GetTrainCommand extends Command {
    private TrainManagerGrpc.TrainManagerBlockingStub stub;

    public GetTrainCommand(TrainManagerGrpc.TrainManagerBlockingStub stub) {
        super("train", "Ottieni un treno dall'id");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        int id = Integer.parseInt(args[0]);
        TrainResponse ts = stub.getTrainById(TrainId.newBuilder().setId(id).build());
        var j = TrainPrinter.printTrain(TrainResponse.newBuilder().setName("CIAO").build());
        System.out.println(TrainPrinter.printTrain(ts));
    }

    @Override
    public String getSyntax() {
        return getName() + " <id>";
    }
}
