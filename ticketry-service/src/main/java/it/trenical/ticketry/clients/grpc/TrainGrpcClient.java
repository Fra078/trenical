package it.trenical.ticketry.clients.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.proto.train.TrainManagerGrpc;
import it.trenical.proto.train.TrainQueryParameters;
import it.trenical.proto.train.TrainResponse;
import it.trenical.ticketry.clients.TrainClient;
import it.trenical.ticketry.proto.TripQueryParams;

import java.util.Iterator;
import java.util.function.Consumer;

public class TrainGrpcClient implements TrainClient {

    private final ManagedChannel channel = ManagedChannelBuilder.
            forAddress("localhost", 5051)
            .usePlaintext().build();
    private final TrainManagerGrpc.TrainManagerBlockingStub stub =
            TrainManagerGrpc.newBlockingStub(channel);

    @Override
    public void getTrainForPath(TrainQueryParameters request, Consumer<TrainResponse> consumer) {
        Iterator<TrainResponse> it = stub.getAllTrains(request);
        while (it.hasNext()) {
            TrainResponse r = it.next();
            consumer.accept(r);
        }
    }
}
