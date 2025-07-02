package it.trenical.trainmanager.repository;

import it.trenical.trainmanager.models.ServiceClassModel;

import java.util.Optional;
import java.util.function.Consumer;

public interface ServiceClassRepository {

    boolean save(ServiceClassModel serviceClass);

    Optional<ServiceClassModel> findByName(String name);

    boolean deleteByName(String name);

    void findAll(Consumer<ServiceClassModel> consumer);

}
