package it.trenical.admin.railway.commands.path;

import io.grpc.ManagedChannel;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.railway.GetPathRequest;
import it.trenical.proto.railway.PathResponse;
import it.trenical.proto.railway.RailwayServiceGrpc;
import it.trenical.proto.railway.StopResponse;

public class GetPathCommand extends Command {
    private final RailwayServiceGrpc.RailwayServiceBlockingStub stub;
    public GetPathCommand(RailwayServiceGrpc.RailwayServiceBlockingStub stub) {
        super("path", "Stampa una tratta ferroviaria");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        int id = Integer.parseInt(args[0]);
        PathResponse resp = stub.getPath(
                GetPathRequest.newBuilder().setId(id).build()
        );
        for (StopResponse stop : resp.getStopsList()) {
            System.out.printf("%.2f KM -> %s%n",stop.getDistance(), stop.getStation().getName());
        }
    }

    @Override
    public String getSyntax() {
        return getName() + " <pathId>";
    }
}
