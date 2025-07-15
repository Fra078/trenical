package it.trenical.admin.railway.commands.path;

import io.grpc.ManagedChannel;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.railway.RailwayServiceGrpc;
import it.trenical.proto.railway.RegisterPathRequest;

import java.util.Arrays;

public class RegisterPathCommand extends Command {

    private final RailwayServiceGrpc.RailwayServiceBlockingStub stub;
    public RegisterPathCommand(RailwayServiceGrpc.RailwayServiceBlockingStub stub) {
        super("path create", "Registra una tratta ferroviaria");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length < 2)
            throw new BadCommandSyntaxException(getSyntax());
        int id = stub.registerPath(
                RegisterPathRequest.newBuilder()
                        .addAllStations(Arrays.asList(args))
                        .build()
        ).getId();
        System.out.println("Registrato con id: " + id);
    }

    @Override
    public String getSyntax() {
        return getName() + " <departure> ... <stationN> ... <arrival>";
    }
}
