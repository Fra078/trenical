package it.trenical.admin.railway.commands;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import it.trenical.admin.railway.printer.StationPrinter;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.railway.GetStationRequest;
import it.trenical.proto.railway.RailwayServiceGrpc;
import it.trenical.proto.railway.StationList;
import it.trenical.proto.railway.StationResponse;

import java.util.List;

public class StationGetCommand extends Command {
    private final RailwayServiceGrpc.RailwayServiceBlockingStub stub;
    public StationGetCommand(RailwayServiceGrpc.RailwayServiceBlockingStub stub) {
        super("station","Ottiene le informazioni riguardo una stazione");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        String name = args[0];
        StationResponse station = stub.getStation(GetStationRequest.newBuilder().setName(name).build());
        StationPrinter.printStations(List.of(station));
    }

    @Override
    public String getSyntax() {
        return getName() + " <stationName>";
    }
}
