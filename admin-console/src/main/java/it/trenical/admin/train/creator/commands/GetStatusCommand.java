package it.trenical.admin.train.creator.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.RegisterTrainRequest;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class GetStatusCommand extends Command {
    private final RegisterTrainRequest.Builder builder;
    public GetStatusCommand(RegisterTrainRequest.Builder builder) {
        super("status", "Mostra lo stato attuale del treno");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        RegisterTrainRequest request = builder.build();
        System.out.println("name: " + request.getName());
        System.out.println("type: " + request.getTypeName());
        ZonedDateTime dt = Instant
                .ofEpochSecond(builder.getDepartureTime())
                .atZone(ZoneId.systemDefault());
        System.out.println("departure time: " + dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        System.out.println("pathId: " + request.getPathId());
        System.out.println("Seats: {");
        builder.getClassSeatsMap().forEach((sc, count)->{
            System.out.println("\t" + sc + ": " + count);
        });
        System.out.println("}");
        System.out.println("PlatformPrefs: {");
        builder.getPlatformChoicesMap().forEach((station, sel)->{
            System.out.println("\t" + station + ": " + sel);
        });
        System.out.println("}");
    }
}
