package it.trenical.ticketry.clients;

import it.trenical.proto.train.TrainQueryParameters;
import it.trenical.proto.train.TrainResponse;

import java.util.function.Consumer;

public interface TrainClient {
    void getTrainForPath(TrainQueryParameters request, Consumer<TrainResponse> consumer);
}
