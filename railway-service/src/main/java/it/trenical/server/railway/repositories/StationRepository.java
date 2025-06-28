package it.trenical.server.railway.repositories;

import it.trenical.server.railway.dao.StationDao;
import it.trenical.server.railway.db.StationDb;
import it.trenical.server.railway.models.Station;

import java.util.Collection;
import java.util.List;

public class StationRepository implements StationDao {

    private final StationDb db = new StationDb();

    @Override
    public Collection<Station> getAll() {
        return db.getAll();
    }

    @Override
    public Station get(int id) {
        return db.get(id);
    }

    @Override
    public Station create(String name, String city, int trackCount) {
        return db.add(name, city, trackCount);
    }

    @Override
    public Station delete(int id) {
        return db.delete(id);
    }
}
