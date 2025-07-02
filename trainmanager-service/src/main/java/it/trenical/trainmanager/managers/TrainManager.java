package it.trenical.trainmanager.managers;

import io.grpc.Status;
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
import it.trenical.trainmanager.utilities.Validator;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrainManager {

    private final TrainRepository trainRepository;
    private final TrainTypeRepository typeRepository;
    private final ServiceClassRepository classRepository;
    private final RailwayClient railClient;

    public TrainManager(TrainRepository trainRepository, TrainTypeRepository typeRepository, ServiceClassRepository classRepository, RailwayClient railClient) {
        this.trainRepository = trainRepository;
        this.typeRepository = typeRepository;
        this.classRepository = classRepository;
        this.railClient = railClient;
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


        Map<ServiceClassModel, Integer> seatsMap = request.getClassSeatsMap().entrySet().stream()
                .map(entry->{
                    ServiceClassModel sc = classRepository.getByName(entry.getKey());
                    if (sc == null)
                        throw new IllegalArgumentException("Class with name " + entry.getKey() + " not found");
                    if (entry.getValue() <= 0)
                        throw new IllegalArgumentException("Seats must be greater than 0");
                    return Map.entry(sc, entry.getValue());
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        builder.setClassSeats(request.getClassSeatsMap());

        PathResponse path = railClient.getPath(request.getPathId());
        if (path == null)
            throw new IllegalArgumentException("PathId not valid");
        builder.setId(path.getId());

        Map<String, Integer> maxPlatform = path.getLinksList().stream()
                .flatMap(link->Stream.of(link.getDeparture(), link.getArrival()))
                .distinct()
                .collect(Collectors.toMap(StationResponse::getName, StationResponse::getPlatformCount));
        Map<String, Integer> requestChoices = request.getPlatformChoicesMap();

        Map<String, Integer> finalChoices = new HashMap<>();
        for (Map.Entry<String, Integer> entry : maxPlatform.entrySet()) {
            Integer choice = requestChoices.get(entry.getKey());
            if (choice == null || choice <= 0 || choice > entry.getValue())
                finalChoices.put(entry.getKey(), new Random().nextInt(entry.getValue())+1);
            else
                finalChoices.put(entry.getKey(), choice);
        }

        builder.setPlatformChoice(finalChoices);

        TrainEntity train = trainRepository.create(builder.build());
        return TrainMapper.toDto(train, type, path, seatsMap);

    }

    public TrainResponse getTrainById(TrainId request) {
        TrainEntity entity = trainRepository.getTrainById(request.getId());
        if (entity == null)
            throw Status.NOT_FOUND.withDescription("Train with " + request.getId() + " non exists").asRuntimeException();
        TrainType type = typeRepository.findByName(entity.type()).orElseThrow();
        PathResponse path = railClient.getPath(entity.pathId());
        Map<ServiceClassModel, Integer> map = entity.classSeats().entrySet().stream()
                .map(entry->
                        Map.entry(classRepository.getByName(entry.getKey()), entry.getValue())
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return TrainMapper.toDto(entity, type, path, map);
    }

}
