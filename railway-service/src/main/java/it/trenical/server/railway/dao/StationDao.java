package it.trenical.server.railway.dao;

import it.trenical.server.railway.models.Station;

import java.util.Collection;
import java.util.List;

public interface StationDao {
    Collection<Station> getAll();
    Station get(int id);
    Station create(String name, String city, int trackCount);
    Station delete(int id);
}
