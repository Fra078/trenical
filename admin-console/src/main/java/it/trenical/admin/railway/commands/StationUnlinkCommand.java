package it.trenical.admin.railway.commands;

import io.grpc.ManagedChannel;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.railway.RailwayServiceGrpc;
import it.trenical.proto.railway.UnlinkStationsRequest;

public class StationUnlinkCommand extends Command {
    private final ManagedChannel channel;
    public StationUnlinkCommand(ManagedChannel channel) {
        super("station unlink", "Rimuove un collegamento tra stazioni");
        this.channel = channel;
    }

    @Override
    public String getSyntax() {
        return getName() + " <station1> <station2>";
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 2)
            throw new BadCommandSyntaxException(getSyntax());
        String station1 = args[0];
        String station2 = args[1];
        RailwayServiceGrpc.newBlockingStub(channel).unLinkStations(
                UnlinkStationsRequest.newBuilder()
                        .setStation1(station1)
                        .setStation2(station2)
                        .build()
        );
        System.out.println("Operazione completata");
    }
}
