package it.trenical.admin.railway.commands;

import io.grpc.ManagedChannel;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.railway.LinkStationsRequest;
import it.trenical.proto.railway.RailwayServiceGrpc;

public class StationLinkCommand extends Command {
    private final ManagedChannel channel;
    public StationLinkCommand(ManagedChannel channel) {
        super("station link", "Collega due stazioni");
        this.channel = channel;
    }

    @Override
    public String getSyntax() {
        return getName() + " <station1> <station2> <distance>";
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 3)
            throw new BadCommandSyntaxException(getSyntax());
        String station1 = args[0];
        String station2 = args[1];
        double distance = Double.parseDouble(args[2]);
        RailwayServiceGrpc.newBlockingStub(channel).linkStations(
                LinkStationsRequest.newBuilder()
                    .setStation1(station1)
                    .setStation2(station2)
                    .setDistance(distance)
                    .build()
        );
        System.out.println("Operazione completata");
    }
}
