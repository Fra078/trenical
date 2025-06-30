package it.trenical.server.railway.db;

import it.trenical.server.database.DatabaseManager;

public class StationDb extends DatabaseManager {
    public StationDb() {
        super("./railway-db");
    }
}
