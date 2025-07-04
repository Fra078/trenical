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
    private final ManagedChannel channel;
    public StationListCommand(ManagedChannel channel) {
        super("station list","Lista di tutte le stazioni esistenti");
        this.channel = channel;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        RailwayServiceGrpc.RailwayServiceBlockingStub stub = RailwayServiceGrpc.newBlockingStub(channel);
        Iterator<StationResponse> it = stub.getAllStations(Empty.getDefaultInstance());
        StationPrinter.printStations(it);
    }
}
