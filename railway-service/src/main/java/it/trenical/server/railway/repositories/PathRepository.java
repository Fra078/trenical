package it.trenical.server.railway.repositories;

import it.trenical.server.railway.exceptions.LinkNotFoundException;
import it.trenical.server.railway.models.Path;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface PathRepository {

    void findAll(Consumer<Path> consumer);
    void findBySubpath(String stationA, String stationB, Consumer<Path> consumer);

    int registerPath(List<String> stations) throws LinkNotFoundException;

    Optional<Path> getPath(int pathId);
}
