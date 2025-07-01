package it.trenical.admin.train.commands.serviceClass;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.proto.train.ServiceClass;
import it.trenical.proto.train.TrainManagerGrpc;
import it.trenical.proto.train.TrainTypeResponse;

import java.util.Iterator;

public class ListServiceClassesCommand extends Command {
    private final ManagedChannel channel;

    public ListServiceClassesCommand(ManagedChannel channel) {
        super("class list", "Lista tutte le classi di servizio");
        this.channel = channel;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        Iterator<ServiceClass> it =
                TrainManagerGrpc.newBlockingStub(channel).getAllServiceClasses(Empty.getDefaultInstance());
        System.out.printf("%-18s %-15s%n", "name", "incrementFactor");
        it.forEachRemaining(resp-> System.out.printf("%-18s %-15.2f%n", resp.getName(), resp.getIncrementFactor()));

    }
}
