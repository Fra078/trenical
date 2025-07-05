package it.trenical.trainmanager.clients;

import it.trenical.proto.railway.PathResponse;

import java.util.function.Consumer;

public interface RailwayClient {
    void findPaths(String departure, String arrival, Consumer<PathResponse> consumer);
    PathResponse getPath(int pathId);
}
