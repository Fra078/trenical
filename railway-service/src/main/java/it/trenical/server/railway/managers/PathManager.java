package it.trenical.server.railway.managers;

import io.grpc.Status;
import it.trenical.proto.railway.GetPathRequest;
import it.trenical.proto.railway.PathResponse;
import it.trenical.proto.railway.RegisterPathRequest;
import it.trenical.proto.railway.RegisterPathResponse;
import it.trenical.server.railway.exceptions.LinkNotFoundException;
import it.trenical.server.railway.mapper.RailwayMapper;
import it.trenical.server.railway.models.Path;
import it.trenical.server.railway.repositories.PathRepository;
import it.trenical.server.railway.repositories.StationRepository;

import java.util.List;

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

}
