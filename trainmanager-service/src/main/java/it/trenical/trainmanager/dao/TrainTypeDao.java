package it.trenical.trainmanager.dao;

import it.trenical.trainmanager.models.TrainType;

import java.util.List;
import java.util.function.Consumer;

public interface TrainTypeDao {

    boolean register(TrainType trainType);

    TrainType getByName(String name);

    boolean removeByName(String name);

    void getAll(Consumer<TrainType> consumer);

}
