package it.trenical.server.railway.repositories;

import it.trenical.server.railway.exceptions.StationExistsException;
import it.trenical.server.railway.models.Station;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public interface StationRepository {
    void findAll(Consumer<Station> consumer);
    Optional<Station> get(String name);
    void save(Station station) throws StationExistsException;
    boolean delete(String name);

    Boolean insertLink(String stationA, String stationB, double distance);
    boolean removeLink(String stationA, String stationB);

    Map<String, Double> getNeighbours(String station);
}
