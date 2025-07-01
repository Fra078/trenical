package it.trenical.admin.train.commands.type;

import io.grpc.ManagedChannel;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.RegisterTrainTypeRequest;
import it.trenical.proto.train.TrainManagerGrpc;

public class RegisterTrainTypeCommand extends Command {
    private final ManagedChannel channel;

    public RegisterTrainTypeCommand(ManagedChannel channel) {
        super("type create", "Genera un nuovo tipo di treno");
        this.channel = channel;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 3)
            throw new BadCommandSyntaxException(getSyntax());
        String name = args[0];
        double speed = Double.parseDouble(args[1]);
        double costPerKm = Double.parseDouble(args[2]);

        TrainManagerGrpc.newBlockingStub(channel).registerTrainType(
                RegisterTrainTypeRequest.newBuilder()
                        .setName(name)
                        .setSpeed(speed)
                        .setCostPerKm(costPerKm)
                        .build()
        );
        System.out.println("Operazione completata!");
    }

    @Override
    public String getSyntax() {
        return getName() + " <name> <speed> <costPerKm>";
    }
}
