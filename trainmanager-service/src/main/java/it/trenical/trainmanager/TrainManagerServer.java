package it.trenical.trainmanager;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.trenical.server.database.DatabaseManager;
import it.trenical.trainmanager.client.RailwayClient;
import it.trenical.trainmanager.managers.TrainManager;
import it.trenical.trainmanager.repository.ServiceClassRepository;
import it.trenical.trainmanager.repository.TrainRepository;
import it.trenical.trainmanager.repository.TrainTypeRepository;
import it.trenical.trainmanager.repository.db.ServiceClassJdbcRepository;
import it.trenical.trainmanager.repository.db.TrainJdbcRepository;
import it.trenical.trainmanager.repository.db.TrainTypeJdbcRepository;
import it.trenical.trainmanager.service.TrainManagerService;
import it.trenical.trainmanager.strategy.impl.DefaultPlatformAssignmentStrategy;

import java.io.IOException;

public class TrainManagerServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        DatabaseManager db = new DatabaseManager("./train-db");
        TrainRepository trainRepository = new TrainJdbcRepository(db);
        TrainTypeRepository typeRepository = new TrainTypeJdbcRepository(db);
        ServiceClassRepository serviceClassRepository = new ServiceClassJdbcRepository(db);

        RailwayClient railwayClient = RailwayClient.getInstance();
        TrainManager trainManager = new TrainManager(
                trainRepository,
                typeRepository,
                serviceClassRepository,
                railwayClient,
                new DefaultPlatformAssignmentStrategy()
        );

        Server server = ServerBuilder.forPort(5051)
                .addService(new TrainManagerService(trainManager, serviceClassRepository, typeRepository))
                .build()
                .start();
        server.awaitTermination();
    }

}
