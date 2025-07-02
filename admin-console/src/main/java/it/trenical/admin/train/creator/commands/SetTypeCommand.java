package it.trenical.admin.train.creator.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.RegisterTrainRequest;

public class SetTypeCommand extends Command {
    private final RegisterTrainRequest.Builder builder;

    public SetTypeCommand(RegisterTrainRequest.Builder builder) {
        super("type set", "Imposta il tipo del treno");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        String type = args[0];
        builder.setTypeName(type);
    }
}
