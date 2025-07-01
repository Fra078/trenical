package it.trenical.trainmanager.dao;

import it.trenical.trainmanager.models.ServiceClassModel;

import java.util.function.Consumer;

public interface ServiceClassDao {

    boolean register(ServiceClassModel trainType);

    ServiceClassModel getByName(String name);

    boolean removeByName(String name);

    void getAll(Consumer<ServiceClassModel> consumer);

}
