package it.trenical.admin.train.commands.serviceClass;

import io.grpc.ManagedChannel;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.ServiceClassByNameRequest;
import it.trenical.proto.train.TrainManagerGrpc;
import it.trenical.proto.train.TrainTypeByNameRequest;

public class GetServiceClassCommand extends Command {
    private final ManagedChannel channel;

    public GetServiceClassCommand(ManagedChannel channel) {
        super("class", "Ottieni le informazioni su una classe di servizio");
        this.channel = channel;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        String name = args[0];

        var result = TrainManagerGrpc.newBlockingStub(channel).getServiceClassByName(
                ServiceClassByNameRequest.newBuilder().setName(name).build()
        );
        System.out.printf("%s CostFactor: %.2f km/h%n", name, result.getIncrementFactor());
    }

    @Override
    public String getSyntax() {
        return getName() + " <name>";
    }
}
