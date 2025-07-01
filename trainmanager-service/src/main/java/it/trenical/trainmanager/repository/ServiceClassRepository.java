package it.trenical.trainmanager.repository;

import it.trenical.trainmanager.dao.ServiceClassDao;
import it.trenical.trainmanager.db.TrainDb;
import it.trenical.trainmanager.db.helpers.ServiceClassDbHelper;
import it.trenical.trainmanager.models.ServiceClassModel;

import java.util.function.Consumer;

public class ServiceClassRepository implements ServiceClassDao {
    private final TrainDb db = new TrainDb();

    public ServiceClassRepository() {
        db.withConnection(ServiceClassDbHelper::createTable);
    }

    @Override
    public boolean register(ServiceClassModel trainType) {
        return db.withConnection(connection -> {
            return ServiceClassDbHelper.register(connection, trainType);
        });
    }

    @Override
    public ServiceClassModel getByName(String name) {
        return db.withConnection(connection -> {
            return ServiceClassDbHelper.get(connection, name);
        });
    }

    @Override
    public boolean removeByName(String name) {
        return db.withConnection(connection -> {
            return ServiceClassDbHelper.delete(connection, name);
        });
    }

    @Override
    public void getAll(Consumer<ServiceClassModel> consumer) {
        db.withConnection(connection -> {
            ServiceClassDbHelper.getAll(connection, consumer);
        });
    }
}
