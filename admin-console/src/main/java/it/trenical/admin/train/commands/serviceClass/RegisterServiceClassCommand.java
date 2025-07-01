package it.trenical.admin.train.commands.serviceClass;

import io.grpc.ManagedChannel;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.RegisterTrainTypeRequest;
import it.trenical.proto.train.ServiceClass;
import it.trenical.proto.train.TrainManagerGrpc;

public class RegisterServiceClassCommand extends Command {
    private final ManagedChannel channel;

    public RegisterServiceClassCommand(ManagedChannel channel) {
        super("class create", "Genera una nuova classe di servizio");
        this.channel = channel;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 2)
            throw new BadCommandSyntaxException(getSyntax());
        String name = args[0];
        double incrementFactor = Double.parseDouble(args[1]);

        TrainManagerGrpc.newBlockingStub(channel).registerServiceClass(
                ServiceClass.newBuilder()
                        .setName(name)
                        .setIncrementFactor(incrementFactor)
                        .build()
        );
        System.out.println("Operazione completata!");
    }

    @Override
    public String getSyntax() {
        return getName() + " <name> <incrementFactor>";
    }
}
