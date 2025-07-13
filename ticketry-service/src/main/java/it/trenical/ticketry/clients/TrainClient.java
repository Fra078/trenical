package it.trenical.ticketry.clients;

import io.grpc.stub.StreamObserver;
import it.trenical.proto.train.TrainQueryParameters;
import it.trenical.proto.train.TrainResponse;
import it.trenical.train.proto.TrainUpdate;

public interface TrainClient {
    void getTrainsForPath(
            TrainQueryParameters request, StreamObserver<TrainResponse> observer
    );

    void getTrain(int trainId, StreamObserver<TrainResponse> observer);

    void listenForUpdates(StreamObserver<TrainUpdate> observer);
}
