package it.trenical.admin.railway.commands;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import it.trenical.admin.railway.printer.StationPrinter;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.railway.RailwayServiceGrpc;
import it.trenical.proto.railway.StationList;
import it.trenical.proto.railway.StationResponse;

import java.util.Iterator;

public class StationListCommand extends Command {
    private final RailwayServiceGrpc.RailwayServiceBlockingStub stub;
    public StationListCommand(RailwayServiceGrpc.RailwayServiceBlockingStub stub) {
        super("station list","Lista di tutte le stazioni esistenti");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        Iterator<StationResponse> it = stub.getAllStations(Empty.getDefaultInstance());
        StationPrinter.printStations(it);
    }
}
