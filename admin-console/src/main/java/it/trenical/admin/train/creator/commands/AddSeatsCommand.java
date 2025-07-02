package it.trenical.admin.train.creator.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.RegisterTrainRequest;

public class AddSeatsCommand extends Command {
    private final RegisterTrainRequest.Builder builder;

    public AddSeatsCommand(RegisterTrainRequest.Builder builder) {
        super("seats add", "Aggiunge o rimuove posti a sedere");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 2)
            throw new BadCommandSyntaxException(getSyntax());
        String serviceClass = args[0];
        int seats = Integer.parseInt(args[1]);
        Integer current = builder.getClassSeatsMap().get(serviceClass);
        if (current == null)
            current = 0;
        current+=seats;
        if (current <= 0)
            builder.removeClassSeats(serviceClass);
        else
            builder.putClassSeats(serviceClass, current);
    }

    @Override
    public String getSyntax() {
        return getName() + " <serviceClass> <count>";
    }
}
