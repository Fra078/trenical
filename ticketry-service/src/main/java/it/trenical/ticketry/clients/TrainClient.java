package it.trenical.ticketry.clients;

import io.grpc.stub.StreamObserver;
import it.trenical.proto.train.TrainQueryParameters;
import it.trenical.proto.train.TrainResponse;

public interface TrainClient {
    void getTrainsForPath(
            TrainQueryParameters request, StreamObserver<TrainResponse> observer
    );

    void getTrain(int trainId, StreamObserver<TrainResponse> observer);
}
