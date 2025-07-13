package it.trenical.trainmanager.managers;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import it.trenical.proto.railway.PathResponse;
import it.trenical.proto.railway.StationResponse;
import it.trenical.proto.railway.StopResponse;
import it.trenical.proto.train.*;
import it.trenical.trainmanager.clients.RailwayClient;
import it.trenical.trainmanager.clients.grpc.RailwayGrpcClient;
import it.trenical.trainmanager.mapper.TrainMapper;
import it.trenical.trainmanager.models.ServiceClassModel;
import it.trenical.trainmanager.models.TrainEntity;
import it.trenical.trainmanager.models.TrainQueryParams;
import it.trenical.trainmanager.models.TrainType;
import it.trenical.trainmanager.repository.ServiceClassRepository;
import it.trenical.trainmanager.repository.TrainRepository;
import it.trenical.trainmanager.repository.TrainTypeRepository;
import it.trenical.trainmanager.strategy.PlatformAssignmentStrategy;

import java.time.Instant;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class TrainManager {

    private final TrainRepository trainRepository;
    private final TrainTypeRepository typeRepository;
    private final ServiceClassRepository classRepository;
    private final RailwayClient railClient;
    private final PlatformAssignmentStrategy paStrategy;

    public TrainManager(TrainRepository trainRepository, TrainTypeRepository typeRepository, ServiceClassRepository classRepository, RailwayGrpcClient railClient, PlatformAssignmentStrategy paStrategy) {
        this.trainRepository = trainRepository;
        this.typeRepository = typeRepository;
        this.classRepository = classRepository;
        this.railClient = railClient;
        this.paStrategy = paStrategy;
    }

    public TrainResponse register(RegisterTrainRequest request) {
        if (request.getName().isBlank())
            throw new IllegalArgumentException("Name cannot be empty");
        if (request.getDepartureTime() <= Instant.now().getEpochSecond())
            throw new IllegalArgumentException("Departure time must be in the future");

        TrainEntity.Builder builder = TrainEntity.builder()
                .setName(request.getName())
                .setDepartureTime(request.getDepartureTime());

        TrainType type = typeRepository.findByName(request.getTypeName()).orElseThrow(
                () -> new IllegalArgumentException("TrainType with name " + request.getTypeName() + " not found"));
        builder.setType(type.name());

        builder.setClassSeats(getAndValidateClassSeats(request.getClassSeatsMap()));

        PathResponse path = railClient.getPath(request.getPathId());
        if (path == null)
            throw new IllegalArgumentException("PathId not valid");
        builder.setPathId(path.getId());

        Map<String, Integer> stationPlatformLimits = path.getStopsList().stream()
                .map(StopResponse::getStation)
                .collect(Collectors.toMap(StationResponse::getName, StationResponse::getPlatformCount));

        Map<String, Integer> finalPlatformChoices =
                paStrategy.assignPlatforms(stationPlatformLimits, request.getPlatformChoicesMap());

        builder.setPlatformChoice(finalPlatformChoices);

        TrainEntity train = trainRepository.save(builder.build());
        return TrainMapper.toDto(train, type, path);
    }

    public TrainResponse getTrainById(TrainId request) {
        TrainEntity entity = trainRepository.findById(request.getId()).orElseThrow(
                () -> new NoSuchElementException("Train not found")
        );
        TrainType type = typeRepository.findByName(entity.type()).orElseThrow();
        PathResponse path = railClient.getPath(entity.pathId());

        return TrainMapper.toDto(entity, type, path);
    }

    public void getAll(TrainQueryParameters params, Consumer<TrainResponse> consumer){
        if (params.hasStations()){
            StationPair pair = params.getStations();
            railClient.findPaths(pair.getDeparture(), pair.getArrival(), (path) -> {
                TrainQueryParameters newParams = TrainQueryParameters.newBuilder(params)
                        .setPathId(path.getId())
                        .build();

                trainRepository.findAll(TrainMapper.fromDto(newParams), entity->{
                    TrainType type = typeRepository.findByName(entity.type()).orElseThrow();
                    consumer.accept(TrainMapper.toDto(entity, type, path));
                });
            });
        } else {
            trainRepository.findAll(TrainMapper.fromDto(params), entity->{
                TrainType type = typeRepository.findByName(entity.type()).orElseThrow();
                PathResponse path = railClient.getPath(entity.pathId());
                consumer.accept(TrainMapper.toDto(entity, type, path));
            });

        }
    }

    private Map<ServiceClassModel, Integer> getAndValidateClassSeats(Map<String, Integer> seats) {
        return seats.entrySet().stream().map(entry -> {
            ServiceClassModel sc = classRepository.findByName(entry.getKey()).orElseThrow(
                    () -> new IllegalArgumentException("Class with name " + entry.getKey() + " not found"));
            if (entry.getValue() <= 0)
                throw new IllegalArgumentException("Seats must be greater than 0");
            return Map.entry(sc, entry.getValue());
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    public void setTrainDelay(int trainId, int minutes) {
        trainRepository.setTrainDelay(trainId, minutes);
    }

    public void cancelTrain(int trainId) {
        try {
            trainRepository.cancelTrain(trainId);
        } catch (RuntimeException e) {
            throw Status.INVALID_ARGUMENT.asRuntimeException();
        }
    }

    public void setTrainStationPlatform(int trainId, String station, int platformId) {
        boolean done = trainRepository.updatePlatform(trainId, station, platformId);
        if (!done)
            throw new IllegalArgumentException("Unable to update platform stop!");
    }

}
