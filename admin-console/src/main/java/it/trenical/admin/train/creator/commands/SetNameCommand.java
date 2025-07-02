package it.trenical.admin.train.creator.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.RegisterTrainRequest;

public class SetNameCommand extends Command {
    private final RegisterTrainRequest.Builder builder;

    public SetNameCommand(RegisterTrainRequest.Builder builder) {
        super("name set", "Imposta il nome del treno");
        this.builder = builder;
    }

    @Override
    public String getSyntax() {
        return getName() + " <name>";
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        String name = args[0];
        builder.setName(name);
    }
}
