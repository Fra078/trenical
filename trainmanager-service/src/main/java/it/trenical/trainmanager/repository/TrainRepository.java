package it.trenical.trainmanager.repository;

import it.trenical.trainmanager.models.TrainEntity;

import java.util.Optional;

public interface TrainRepository {
    TrainEntity save(TrainEntity train);
    Optional<TrainEntity> findById(int id);
}

