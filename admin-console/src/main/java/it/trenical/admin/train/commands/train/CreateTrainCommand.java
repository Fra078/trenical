package it.trenical.admin.train.commands.train;

import io.grpc.ManagedChannel;
import it.trenical.admin.train.creator.TrainCreatorMenu;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;

public class CreateTrainCommand extends Command {

    private final ManagedChannel channel;

    public CreateTrainCommand(ManagedChannel channel) {
        super("train create", "Crea un nuovo treno");
        this.channel = channel;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        new TrainCreatorMenu(channel).start();
    }
}
