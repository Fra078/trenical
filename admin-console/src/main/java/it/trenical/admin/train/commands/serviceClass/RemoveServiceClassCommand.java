package it.trenical.admin.train.commands.serviceClass;

import io.grpc.ManagedChannel;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.ServiceClassByNameRequest;
import it.trenical.proto.train.TrainManagerGrpc;
import it.trenical.proto.train.TrainTypeByNameRequest;

public class RemoveServiceClassCommand extends Command {
    private final ManagedChannel channel;

    public RemoveServiceClassCommand(ManagedChannel channel) {
        super("class rm", "Rimuove una classe di servizio");
        this.channel = channel;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        String name = args[0];

        TrainManagerGrpc.newBlockingStub(channel).removeServiceClassByName(
                ServiceClassByNameRequest.newBuilder().setName(name).build()
        );
        System.out.println("Operazione completata!");
    }

    @Override
    public String getSyntax() {
        return getName() + " <name>";
    }
}
