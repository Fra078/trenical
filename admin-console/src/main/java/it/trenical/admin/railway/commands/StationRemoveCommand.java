package it.trenical.admin.railway.commands;

import io.grpc.ManagedChannel;
import it.trenical.admin.railway.printer.StationPrinter;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.railway.DeleteStationRequest;
import it.trenical.proto.railway.GetStationRequest;
import it.trenical.proto.railway.RailwayServiceGrpc;
import it.trenical.proto.railway.StationResponse;

import java.util.List;

public class StationRemoveCommand extends Command {
    private final RailwayServiceGrpc.RailwayServiceBlockingStub stub;
    public StationRemoveCommand(RailwayServiceGrpc.RailwayServiceBlockingStub stub) {
        super("station rm","Rimuove una stazione");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        String name = args[0];
        stub.deleteStation(DeleteStationRequest.newBuilder().setName(name).build());
        System.out.println("Stazione " + name + " rimossa correttamente!");
    }

    @Override
    public String getSyntax() {
        return getName() + " <stationId>";
    }
}
