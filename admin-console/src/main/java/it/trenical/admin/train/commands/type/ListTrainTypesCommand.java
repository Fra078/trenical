package it.trenical.admin.train.commands.type;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.RegisterTrainTypeRequest;
import it.trenical.proto.train.TrainManagerGrpc;
import it.trenical.proto.train.TrainTypeResponse;

import java.util.Iterator;

public class ListTrainTypesCommand extends Command {
    private final ManagedChannel channel;

    public ListTrainTypesCommand(ManagedChannel channel) {
        super("type list", "Lista tutte le tipologie di treno");
        this.channel = channel;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        Iterator<TrainTypeResponse> it =
                TrainManagerGrpc.newBlockingStub(channel).getAllTrainTypes(Empty.getDefaultInstance());
        System.out.printf("%-18s %-10s %-10s%n", "name", "km/h", "$/km");
        it.forEachRemaining(resp-> System.out.printf("%-18s %-10.2f %-10.2f%n", resp.getName(), resp.getSpeed(), resp.getCostPerKm()));

    }
}
