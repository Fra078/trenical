package it.trenical.server.railway.repositories;

import it.trenical.server.railway.dao.StationDao;
import it.trenical.server.railway.db.StationDb;
import it.trenical.server.railway.db.helpers.LinkDatabaseHelper;
import it.trenical.server.railway.db.helpers.StationDatabaseHelper;
import it.trenical.server.railway.models.Station;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public class StationRepository implements StationDao {

    private final StationDb db = new StationDb();

    public StationRepository() {
        db.withConnection(connection->{
            StationDatabaseHelper.createTable(connection);
            LinkDatabaseHelper.createTable(connection);
        });
    }

    @Override
    public Collection<Station> getAll() {
        return db.withConnection(StationDatabaseHelper::getAll);
    }

    @Override
    public Station get(String name) {
        return db.withConnection(connection->{
            return StationDatabaseHelper.get(connection, name).orElseThrow();
        });
    }

    @Override
    public boolean create(String name, String city, int trackCount) {
        return db.withConnection(connection->{
            return StationDatabaseHelper.create(connection, name, city, trackCount);
        });
    }

    @Override
    public boolean delete(String name) {
        return db.withConnection(connection->{
            return StationDatabaseHelper.delete(connection, name);
        });
    }

    @Override
    public boolean insertLink(String stationA, String stationB, double distance) {
        return db.withConnection(connection->{
            if (LinkDatabaseHelper.getDistance(connection, stationA, stationB) != null)
                return false;
            LinkDatabaseHelper.insertLink(connection, stationA, stationB, distance);
            return true;
        });
    }

    @Override
    public boolean removeLink(String stationA, String stationB) {
        return db.withConnection(connection -> {
            if (LinkDatabaseHelper.getDistance(connection, stationA, stationB) == null)
                return false;
            return LinkDatabaseHelper.removeLink(connection, stationA, stationB);
        });
    }

    @Override
    public Map<String, Double> getNeighbours(String station) {
        return db.withConnection(connection->{
            return LinkDatabaseHelper.getNeighbours(connection, station);
        });
    }
}
