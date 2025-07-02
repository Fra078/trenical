package it.trenical.admin.train.creator.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.RegisterTrainRequest;

public class AddPlatformPreferenceCommand extends Command {
    private final RegisterTrainRequest.Builder builder;

    public AddPlatformPreferenceCommand(RegisterTrainRequest.Builder builder) {
        super("platform set", "Imposta il binario di preferenza per una stazione");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 2)
            throw new BadCommandSyntaxException(getSyntax());
        String station  = args[0];
        int value = Integer.parseInt(args[1]);
        builder.putPlatformChoices(station, value);
    }
}
