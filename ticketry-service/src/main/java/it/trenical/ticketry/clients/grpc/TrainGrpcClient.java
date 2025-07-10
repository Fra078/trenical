package it.trenical.ticketry.clients.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import it.trenical.proto.train.TrainId;
import it.trenical.proto.train.TrainManagerGrpc;
import it.trenical.proto.train.TrainQueryParameters;
import it.trenical.proto.train.TrainResponse;
import it.trenical.ticketry.clients.TrainClient;

public class TrainGrpcClient implements TrainClient {

    private final ManagedChannel channel = ManagedChannelBuilder.
            forAddress("localhost", 5051)
            .usePlaintext().build();
    private final TrainManagerGrpc.TrainManagerStub stub =
            TrainManagerGrpc.newStub(channel);

    @Override
    public void getTrainsForPath(TrainQueryParameters request, StreamObserver<TrainResponse> observer) {
        stub.getAllTrains(request, observer);
    }

    @Override
    public void getTrain(int trainId, StreamObserver<TrainResponse> observer) {
        stub.getTrainById(TrainId.newBuilder().setId(trainId).build(), observer);
    }
}
