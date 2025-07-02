package it.trenical.admin.train.creator.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.RegisterTrainRequest;

public class SetPathCommand extends Command {
    private final RegisterTrainRequest.Builder builder;

    public SetPathCommand(RegisterTrainRequest.Builder builder) {
        super("path set", "Imposta il percorso del treno");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        int pathId = Integer.parseInt(args[0]);
        builder.setPathId(pathId);
    }

    @Override
    public String getSyntax() {
        return getName() + " <path>";
    }
}
