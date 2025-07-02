package it.trenical.trainmanager.repository;

import io.grpc.stub.StreamObserver;
import it.trenical.proto.train.TrainId;
import it.trenical.trainmanager.db.TrainDb;
import it.trenical.trainmanager.db.helpers.TrainDbHelper;
import it.trenical.trainmanager.models.TrainEntity;

import java.util.Map;

public class TrainRepository {

    private final TrainDb trainDb = new TrainDb();

    public TrainRepository() {
        trainDb.withConnection(TrainDbHelper::createTable);
    }

    public TrainEntity create(
            TrainEntity train
    ){
        int id = trainDb.withConnection(false, connection -> {
            int trainId = TrainDbHelper.insertTrain(connection, train.name(), train.type(), train.departureTime(), train.pathId());
            TrainDbHelper.insertSeats(connection, trainId, train.classSeats());
            TrainDbHelper.insertPlatformChoices(connection, trainId, train.platformChoice());
            connection.commit();
            return trainId;
        });
        return TrainEntity.builder(train).setId(id).build();
    }

    public TrainEntity getTrainById(int id) {
        return trainDb.withConnection(connection -> {
            return TrainDbHelper.getTrain(connection, id);
        });
    }

}
