package it.trenical.trainmanager.repository;

import it.trenical.trainmanager.models.TrainEntity;
import it.trenical.trainmanager.models.TrainQueryParams;

import java.util.Optional;
import java.util.function.Consumer;

public interface TrainRepository {
    TrainEntity save(TrainEntity train);
    Optional<TrainEntity> findById(int id);
    void findAll(TrainQueryParams params, Consumer<TrainEntity> train);

    void setTrainDelay(int trainId, int delay);
    void cancelTrain(int trainId);
    boolean updatePlatform(int trainId, String stationName, int platformNumber);
}

