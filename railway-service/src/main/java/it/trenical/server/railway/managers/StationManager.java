package it.trenical.server.railway.managers;

import io.grpc.Status;
import it.trenical.proto.railway.*;
import it.trenical.server.railway.exceptions.StationExistsException;
import it.trenical.server.railway.mapper.RailwayMapper;
import it.trenical.server.railway.models.Station;
import it.trenical.server.railway.repositories.StationRepository;

import java.util.Map;
import java.util.function.Consumer;

public class StationManager {

    private final StationRepository stationRepository;

    public StationManager(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public void getAll(Consumer<StationResponse> consumer){
        stationRepository.findAll(station->
                consumer.accept(RailwayMapper.toDto(station))
        );
    }

    public void registerStation(RegisterStationRequest request){
        if (request.getName().isBlank() || request.getCity().isBlank())
            throw Status.INVALID_ARGUMENT.withDescription("Arguments must be non empty").asRuntimeException();
        if (request.getTrackCount() <= 0)
            throw Status.INVALID_ARGUMENT.withDescription("Tracks must be greater than 0").asRuntimeException();
        try {
            stationRepository.save(new Station(request.getName(), request.getCity(), request.getTrackCount()));
        } catch (StationExistsException exc){
            throw Status.ALREADY_EXISTS.withDescription(exc.getMessage()).asRuntimeException();
        }
    }

    public StationResponse getByName(GetStationRequest req){
        Station station = stationRepository.get(req.getName())
                .orElseThrow(()->Status.NOT_FOUND.withDescription("Station not found").asRuntimeException());
        return RailwayMapper.toDto(station);
    }

    public void deleteStation(DeleteStationRequest request) {
        boolean done = stationRepository.delete(request.getName());
        if (!done)
            throw Status.NOT_FOUND.withDescription("Station not found").asRuntimeException();
    }

    public void linkStations(LinkStationsRequest request) {
        boolean done = stationRepository.insertLink(request.getStation1(), request.getStation2(), request.getDistance());
        if (!done)
            throw Status.INVALID_ARGUMENT.withDescription("Link not allowed").asRuntimeException();

    }

    public void unlinkStations(UnlinkStationsRequest request) {
        boolean done = stationRepository.removeLink(request.getStation1(), request.getStation2());
        if (!done)
            throw Status.NOT_FOUND.withDescription("Link not found").asRuntimeException();
    }

    public GetNearStationsResponse getNearStations(GetNearStationsRequest request) {
        Map<String, Double> neighbours = stationRepository.getNeighbours(request.getName());
        return GetNearStationsResponse.newBuilder().putAllValue(neighbours).build();
    }

}
