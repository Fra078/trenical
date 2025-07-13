package it.trenical.trainmanager.observer;

import it.trenical.train.proto.TrainUpdate;

public interface TrainUpdateObserver {
    boolean canHandle(int trainId);
    void onNext(TrainUpdate update);
}
