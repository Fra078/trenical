package it.trenical.trainmanager.observer;

import it.trenical.train.proto.TrainUpdate;

import java.util.function.Consumer;

public class SingleTrainObserver implements TrainUpdateObserver {

    private final Consumer<TrainUpdate> consumer;
    private final Integer trainId;

    public SingleTrainObserver(Integer trainId, Consumer<TrainUpdate> consumer) {
        this.trainId = trainId;
        this.consumer = consumer;
    }

    @Override
    public boolean canHandle(int updateId) {
        return trainId == null || trainId.equals(updateId);
    }

    @Override
    public void onNext(TrainUpdate update) {
        consumer.accept(update);
    }
}
