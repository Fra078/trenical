package it.trenical.ticketry.test.purchase;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import it.trenical.proto.train.TrainQueryParameters;
import it.trenical.proto.train.TrainResponse;
import it.trenical.ticketry.clients.TrainClient;

public class MockTrainClient implements TrainClient {
    private TrainResponse trainResponse;
    public void setTrainToReturn(TrainResponse train) { this.trainResponse = train; }
    @Override
    public void getTrain(int trainId, StreamObserver<TrainResponse> observer) {
        if (trainResponse != null) {
            observer.onNext(trainResponse);
            observer.onCompleted();
        } else {
            observer.onError(Status.NOT_FOUND.asRuntimeException());
        }
    }
    @Override
    public void getTrainsForPath(TrainQueryParameters request, StreamObserver<TrainResponse> observer) {}
}