package it.trenical.client.ticketry.commands;

import it.trenical.common.proto.Common;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.TrainQueryParameters;
import it.trenical.proto.train.TrainResponse;
import it.trenical.ticketry.proto.TicketryServiceGrpc;
import it.trenical.ticketry.proto.TripMode;
import it.trenical.ticketry.proto.TripQueryParams;
import it.trenical.ticketry.proto.TripSolution;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class QueryTripCommand extends Command {
    private TicketryServiceGrpc.TicketryServiceBlockingStub stub;

    public QueryTripCommand(TicketryServiceGrpc.TicketryServiceBlockingStub stub) {
        super("trip query", "Ricerca treni disponibili");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        TripQueryParams.Builder builder = TripQueryParams.newBuilder();
        if (args.length < 3 || (args.length -3) % 2 != 0)
            throw new BadCommandSyntaxException(getSyntax());
        decorateWithDate(builder, args[0]);
        String departure = args[1];
        String arrival = args[2];
        builder.setDeparture(departure);
        builder.setArrival(arrival);

        for (int i = 3; i < args.length; i += 2) {
            String param = args[i];
            String value = args[i + 1];
            switch (param){
                case "-type" -> builder.setTrainType(value);
                case "-class" -> builder.setServiceClass(value);
                default -> throw new BadCommandSyntaxException(getSyntax());
            }
        }

        Iterator<TripSolution> it = stub.getTripSolutions(builder.build());
        while (it.hasNext()) {
            TripSolution tripSolution = it.next();
            System.out.printf("ID:%d %s %s%n", tripSolution.getTrainId(), tripSolution.getType().getName(), tripSolution.getTrainName());
            System.out.printf("%s (%s) --- %s (%s)%n",
                    tripSolution.getDepartureStation(),
                    formatTime(tripSolution.getDepartureTime()),
                    tripSolution.getArrivalStation(),
                    formatTime(tripSolution.getArrivalTime()));
            for (TripMode tm : tripSolution.getModesList())
                System.out.printf("%s €%.2f%n", tm.getServiceClass().getName(), tm.getTotalPrice());
            System.out.println();
        }
    }

    @Override
    public String getSyntax() {
        return getName() + " <gg/mm/aaaa> <departureStation> <arrivalStation> [-type <typeName>] [-class <serviceClass>]";
    }

    private void decorateWithDate(TripQueryParams.Builder builder, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        ZonedDateTime startOfDay = date.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
        builder.setDate(Common.DateRange.newBuilder()
                .setFrom(startOfDay.toEpochSecond())
                .setTo(endOfDay.toEpochSecond())
                .build());
    }

    private String formatTime(long time) {
        Instant instant = Instant.ofEpochSecond(time);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
