package it.trenical.admin.train.commands.type;

import io.grpc.ManagedChannel;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.TrainManagerGrpc;
import it.trenical.proto.train.TrainTypeByNameRequest;

public class RemoveTrainTypeCommand extends Command {
    private final ManagedChannel channel;

    public RemoveTrainTypeCommand(ManagedChannel channel) {
        super("type rm", "Rimuove una tipologia di treno");
        this.channel = channel;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        String name = args[0];;

        TrainManagerGrpc.newBlockingStub(channel).removeTrainTypeByName(
                TrainTypeByNameRequest.newBuilder().setName(name).build()
        );
        System.out.println("Operazione completata!");
    }

    @Override
    public String getSyntax() {
        return getName() + " <name>";
    }
}
