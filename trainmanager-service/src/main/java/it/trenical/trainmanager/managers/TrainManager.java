package it.trenical.trainmanager.managers;

import it.trenical.proto.railway.PathResponse;
import it.trenical.proto.railway.StationResponse;
import it.trenical.proto.train.RegisterTrainRequest;
import it.trenical.proto.train.TrainId;
import it.trenical.proto.train.TrainResponse;
import it.trenical.trainmanager.client.RailwayClient;
import it.trenical.trainmanager.mapper.TrainMapper;
import it.trenical.trainmanager.models.ServiceClassModel;
import it.trenical.trainmanager.models.TrainEntity;
import it.trenical.trainmanager.models.TrainType;
import it.trenical.trainmanager.repository.ServiceClassRepository;
import it.trenical.trainmanager.repository.TrainRepository;
import it.trenical.trainmanager.repository.TrainTypeRepository;
import it.trenical.trainmanager.strategy.PlatformAssignmentStrategy;
import it.trenical.trainmanager.utilities.Validator;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrainManager {

    private final TrainRepository trainRepository;
    private final TrainTypeRepository typeRepository;
    private final ServiceClassRepository classRepository;
    private final RailwayClient railClient;
    private final PlatformAssignmentStrategy paStrategy;

    public TrainManager(TrainRepository trainRepository, TrainTypeRepository typeRepository, ServiceClassRepository classRepository, RailwayClient railClient, PlatformAssignmentStrategy paStrategy) {
        this.trainRepository = trainRepository;
        this.typeRepository = typeRepository;
        this.classRepository = classRepository;
        this.railClient = railClient;
        this.paStrategy = paStrategy;
    }

    public TrainResponse register(RegisterTrainRequest request) {
        if (Validator.isBlank(request.getName()))
            throw new IllegalArgumentException("Name cannot be empty");
        if (!Validator.isFuture(request.getDepartureTime()))
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

        Map<String, Integer> stationPlatformLimits = path.getLinksList().stream()
                .flatMap(link->Stream.of(link.getDeparture(), link.getArrival()))
                .distinct()
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

    private Map<ServiceClassModel, Integer> getAndValidateClassSeats(Map<String, Integer> seats) {
        return seats.entrySet().stream().map(entry -> {
            ServiceClassModel sc = classRepository.findByName(entry.getKey()).orElseThrow(
                    () -> new IllegalArgumentException("Class with name " + entry.getKey() + " not found"));
            if (entry.getValue() <= 0)
                throw new IllegalArgumentException("Seats must be greater than 0");
            return Map.entry(sc, entry.getValue());
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

}
