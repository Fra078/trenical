package it.trenical.trainmanager.db;

import it.trenical.server.database.DatabaseManager;

public class TrainDb extends DatabaseManager {
    public TrainDb() {
        super("./train-db");
    }
}
