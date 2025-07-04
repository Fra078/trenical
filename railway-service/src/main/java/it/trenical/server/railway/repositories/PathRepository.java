package it.trenical.server.railway.repositories;

import it.trenical.server.railway.exceptions.LinkNotFoundException;
import it.trenical.server.railway.models.Path;

import java.util.List;
import java.util.Optional;

public interface PathRepository {

    int registerPath(List<String> stations) throws LinkNotFoundException;

    Optional<Path> getPath(int pathId);
}
