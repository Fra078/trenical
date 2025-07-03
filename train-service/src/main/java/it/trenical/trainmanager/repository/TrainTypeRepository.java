package it.trenical.trainmanager.repository;

import it.trenical.trainmanager.models.TrainType;

import java.util.Optional;
import java.util.function.Consumer;

public interface TrainTypeRepository {
    boolean save(TrainType trainType);
    Optional<TrainType> findByName(String name);
    boolean deleteByName(String name);
    void findAll(Consumer<TrainType> consumer);
}
