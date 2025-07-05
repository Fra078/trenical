package it.trenical.admin.railway.commands.path;

import com.google.protobuf.Empty;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.railway.PathResponse;
import it.trenical.proto.railway.RailwayServiceGrpc;
import it.trenical.proto.railway.StopResponse;

import java.util.Iterator;
import java.util.List;

public class PathListCommand extends Command {

    private final RailwayServiceGrpc.RailwayServiceBlockingStub stub;

    public PathListCommand(RailwayServiceGrpc.RailwayServiceBlockingStub stub) {
        super("path list", "Ottiene la lista di tutte le tratte registrate");
        this.stub = stub;
    }

    @Override
    public String getSyntax() {
        return super.getSyntax();
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        Iterator<PathResponse> it = stub.getAllPaths(Empty.getDefaultInstance());
        while (it.hasNext()) {
            PathResponse response = it.next();
            System.out.print("ID: " + response.getId()+" [");
            List<StopResponse> stops = response.getStopsList();
            for (StopResponse stop : stops) {
                if (stop.getDistance() != 0)
                    System.out.printf(" - %.2f - ", stop.getDistance());
                System.out.print(stop.getStation().getName());
            }
            System.out.println("]");
        }
    }
}
