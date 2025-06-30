package it.trenical.admin.railway.commands;

import io.grpc.ManagedChannel;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.railway.GetNearStationsRequest;
import it.trenical.proto.railway.RailwayServiceGrpc;
import it.trenical.proto.railway.UnlinkStationsRequest;

public class NearStationsCommand extends Command {
    private final ManagedChannel channel;
    public NearStationsCommand(ManagedChannel channel) {
        super("station list neighbours", "Mostra le distanze con le stazioni collegate");
        this.channel = channel;
    }

    @Override
    public String getSyntax() {
        return getName() + " <station1>";
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        String station = args[0];
        RailwayServiceGrpc.newBlockingStub(channel).getNearStations(
                GetNearStationsRequest.newBuilder().setName(station).build()
        ).getValueMap().forEach((stationName, distance)->{
            System.out.println(stationName+" - "+ distance);
        });

    }
}
