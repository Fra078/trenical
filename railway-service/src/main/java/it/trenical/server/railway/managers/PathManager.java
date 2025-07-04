package it.trenical.server.railway.managers;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import it.trenical.proto.railway.*;
import it.trenical.server.railway.exceptions.LinkNotFoundException;
import it.trenical.server.railway.mapper.RailwayMapper;
import it.trenical.server.railway.models.Path;
import it.trenical.server.railway.repositories.PathRepository;
import it.trenical.server.railway.repositories.StationRepository;

import java.util.List;
import java.util.function.Consumer;

public class PathManager {

    private final PathRepository pathRepository;

    public PathManager(PathRepository pathRepository) {
        this.pathRepository = pathRepository;
    }

    public RegisterPathResponse registerPath(RegisterPathRequest request) {
        List<String> stations = request.getStationsList();
        if (stations.size() < 2)
            throw Status.INVALID_ARGUMENT.withDescription("A path must have a departure and an arrival!").asRuntimeException();
        try {
            int pathId = pathRepository.registerPath(stations);
            return RegisterPathResponse.newBuilder().setId(pathId).build();
        } catch (LinkNotFoundException exc){
            throw Status.INVALID_ARGUMENT.withDescription(exc.getMessage()).asRuntimeException();
        }
    }

    public PathResponse getPath(GetPathRequest request) {
        Path path = pathRepository.getPath(request.getId())
                .orElseThrow(() -> Status.NOT_FOUND.withDescription("Path not found!").asRuntimeException());
        return RailwayMapper.toDto(path);
    }

    public void findAll(Consumer<PathResponse> consumer) {
        pathRepository.findAll((path)->consumer.accept(RailwayMapper.toDto(path)));
    }

    public void findBySubpath(PathsQueryParams request, Consumer<PathResponse> consumer) {
        pathRepository.findBySubpath(
                request.getDeparture(),
                request.getArrival(),
                path -> consumer.accept(RailwayMapper.toDto(path))
        );
    }

}
