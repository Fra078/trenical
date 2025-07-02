package it.trenical.admin.train.creator.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.RegisterTrainRequest;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class SetDepartureTime extends Command {
    private final RegisterTrainRequest.Builder builder;

    public SetDepartureTime(RegisterTrainRequest.Builder builder) {
        super("departure set", "Imposta l'orario di partenza");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 2)
            throw new BadCommandSyntaxException(getSyntax());
        String dateStr = args[0];
        String timeStr = args[1];

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime localDateTime = LocalDateTime.parse(dateStr + " " + timeStr, dateFormatter);

        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        long epochSeconds = zonedDateTime.toEpochSecond();
        builder.setDepartureTime(epochSeconds);
    }

    @Override
    public String getSyntax() {
        return getName() + " <gg/mm/yyyy> <hh:mm>";
    }
}
