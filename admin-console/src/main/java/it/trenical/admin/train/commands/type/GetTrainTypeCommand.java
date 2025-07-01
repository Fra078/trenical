package it.trenical.admin.train.commands.type;

import io.grpc.ManagedChannel;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.RegisterTrainTypeRequest;
import it.trenical.proto.train.TrainManagerGrpc;
import it.trenical.proto.train.TrainTypeByNameRequest;

public class GetTrainTypeCommand extends Command {
    private final ManagedChannel channel;

    public GetTrainTypeCommand(ManagedChannel channel) {
        super("type", "Ottieni le informazioni su una tipologia di treno");
        this.channel = channel;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        String name = args[0];;


        var result = TrainManagerGrpc.newBlockingStub(channel).getTrainTypeByName(
                TrainTypeByNameRequest.newBuilder().setName(name).build()
        );
        System.out.printf("%s Speed: %.2f km/h Cost: %.2f $/km%n", name, result.getSpeed(), result.getCostPerKm());
    }

    @Override
    public String getSyntax() {
        return getName() + " <name>";
    }
}
