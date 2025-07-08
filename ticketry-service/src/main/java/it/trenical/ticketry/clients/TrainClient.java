package it.trenical.ticketry.clients;

import io.grpc.stub.StreamObserver;
import it.trenical.proto.train.TrainQueryParameters;
import it.trenical.proto.train.TrainResponse;

public interface TrainClient {
    void getTrainForPath(
            TrainQueryParameters request, StreamObserver<TrainResponse> observer
    );
}
