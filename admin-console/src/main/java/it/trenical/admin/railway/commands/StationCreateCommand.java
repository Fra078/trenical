package it.trenical.admin.railway.commands;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import it.trenical.admin.railway.printer.StationPrinter;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.railway.RailwayServiceGrpc;
import it.trenical.proto.railway.RegisterStationRequest;
import it.trenical.proto.railway.StationList;
import it.trenical.proto.railway.StationResponse;

import java.util.List;

public class StationCreateCommand extends Command {
    private final RailwayServiceGrpc.RailwayServiceBlockingStub stub;
    public StationCreateCommand(RailwayServiceGrpc.RailwayServiceBlockingStub stub) {
        super("station create","Registra una nuova stazione");
        this.stub = stub;
    }

    @Override
    public String getSyntax() {
        return getName() + " <name> <city> <trackCount>";
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 3) throw new BadCommandSyntaxException(getSyntax());
        String name = args[0];
        String city = args[1];
        int trackCount = Integer.parseInt(args[2]);
        stub.registerStation(RegisterStationRequest.newBuilder().setName(name).setCity(city).setTrackCount(trackCount).build());
        System.out.println("Operazione completata!");
    }
}
