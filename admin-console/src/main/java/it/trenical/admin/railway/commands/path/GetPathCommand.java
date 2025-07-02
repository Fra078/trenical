package it.trenical.admin.railway.commands.path;

import io.grpc.ManagedChannel;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.railway.GetPathRequest;
import it.trenical.proto.railway.LinkResponse;
import it.trenical.proto.railway.PathResponse;
import it.trenical.proto.railway.RailwayServiceGrpc;

public class GetPathCommand extends Command {
    private final ManagedChannel channel;
    public GetPathCommand(ManagedChannel channel) {
        super("path", "Stampa una tratta ferroviaria");
        this.channel = channel;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        int id = Integer.parseInt(args[0]);
        PathResponse resp = RailwayServiceGrpc.newBlockingStub(channel).getPath(
                GetPathRequest.newBuilder().setId(id).build()
        );
        System.out.println(resp.getId());
        for (LinkResponse link : resp.getLinksList()) {
            System.out.printf("%s -> %s : %.2f%n", link.getDeparture().getName(), link.getArrival().getName(), link.getDistance());
        }
    }

    @Override
    public String getSyntax() {
        return getName() + " <pathId>";
    }
}
