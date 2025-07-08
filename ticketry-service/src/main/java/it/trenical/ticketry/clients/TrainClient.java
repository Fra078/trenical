package it.trenical.ticketry.clients;

import io.grpc.stub.StreamObserver;
import it.trenical.proto.train.TrainQueryParameters;
import it.trenical.proto.train.TrainResponse;
import it.trenical.server.stream.DefaultStreamObserver;

public interface TrainClient {
    void getTrainForPath(
            TrainQueryParameters request, StreamObserver<TrainResponse> observer
    );
}
