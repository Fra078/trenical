package it.trenical.admin.train.commands.train;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import it.trenical.admin.train.creator.printers.TrainPrinter;
import it.trenical.common.proto.Common;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class ListTrainCommand extends Command {
    private TrainManagerGrpc.TrainManagerBlockingStub stub;

    public ListTrainCommand(TrainManagerGrpc.TrainManagerBlockingStub stub) {
        super("train list", "Lista di tutti i treni");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        TrainQueryParameters.Builder builder = TrainQueryParameters.newBuilder();
        if (args.length % 2 != 0)
            throw new BadCommandSyntaxException(getSyntax());
        for (int i = 0; i < args.length; i += 2) {
            String param = args[i];
            String value = args[i + 1];
            switch (param){
                case "-type" -> builder.setType(value);
                case "-path" -> builder.setPathId(Integer.parseInt(value));
                case "-date" -> {
                    LocalDate date = LocalDate.parse(value, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                    ZonedDateTime startOfDay = date.atStartOfDay(ZoneId.systemDefault());
                    ZonedDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);
                    builder.setDateRange(Common.DateRange.newBuilder()
                            .setFrom(startOfDay.toEpochSecond())
                            .setTo(endOfDay.toEpochSecond())
                            .build());
                }
                case "-class" -> {
                    builder.setServiceClass(value);
                }
                default -> throw new BadCommandSyntaxException(getSyntax());
            }
        }

        Iterator<TrainResponse> it = stub.getAllTrains(builder.build());
        while (it.hasNext()) {
            System.out.println(TrainPrinter.printTrain(it.next()));
        }
    }

    @Override
    public String getSyntax() {
        return getName() + " [-type <typeName>] [-path <pathId>] [-date <gg/mm/aaaa>] [-class <serviceClass>]";
    }
}
