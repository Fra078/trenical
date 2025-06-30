package it.trenical.server.railway.dao;

import it.trenical.server.railway.models.Path;
import it.trenical.server.railway.models.Station;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface StationDao {
    Collection<Station> getAll();
    Station get(String name);
    boolean create(String name, String city, int trackCount);
    boolean delete(String name);

    boolean insertLink(String stationA, String stationB, double distance);

    boolean removeLink(String stationA, String stationB);

    Map<String, Double> getNeighbours(String station);

    Integer registerPath(List<String> stations);
    Path getPath(int id);
}
