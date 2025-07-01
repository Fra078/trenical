package it.trenical.trainmanager.repository;

import it.trenical.trainmanager.dao.TrainTypeDao;
import it.trenical.trainmanager.db.TrainDb;
import it.trenical.trainmanager.db.helpers.TrainTypeDbHelper;
import it.trenical.trainmanager.models.TrainType;

import java.util.function.Consumer;

public class TrainTypeRepository implements TrainTypeDao {
    private final TrainDb db = new TrainDb();

    public TrainTypeRepository() {
        db.withConnection(TrainTypeDbHelper::createTable);
    }

    @Override
    public boolean register(TrainType trainType) {
        return db.withConnection(connection -> {
            return TrainTypeDbHelper.register(connection, trainType);
        });
    }

    @Override
    public TrainType getByName(String name) {
        return db.withConnection(connection -> {
            return TrainTypeDbHelper.get(connection, name).orElse(null);
        });
    }

    @Override
    public boolean removeByName(String name) {
        return db.withConnection(connection -> {
            return TrainTypeDbHelper.delete(connection, name);
        });
    }

    @Override
    public void getAll(Consumer<TrainType> consumer) {
        db.withConnection(connection -> {
           TrainTypeDbHelper.getAll(connection, consumer);
        });
    }
}
