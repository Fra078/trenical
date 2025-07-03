package it.trenical.trainmanager.mapper;

import it.trenical.proto.railway.PathResponse;
import it.trenical.proto.train.*;
import it.trenical.trainmanager.models.ServiceClassModel;
import it.trenical.trainmanager.models.TrainEntity;
import it.trenical.trainmanager.models.TrainQueryParams;
import it.trenical.trainmanager.models.TrainType;

public class TrainMapper {

    public static TrainTypeResponse toDto(TrainType trainType) {
        return TrainTypeResponse.newBuilder()
                .setName(trainType.name())
                .setSpeed(trainType.speed())
                .setCostPerKm(trainType.costPerKm())
                .build();
    }

    public static TrainType fromRequest(RegisterTrainTypeRequest request) {
        return new TrainType(
                request.getName(),
                request.getSpeed(),
                request.getCostPerKm()
        );
    }

    public static ServiceClass toDto(ServiceClassModel trainType) {
        return ServiceClass.newBuilder()
                .setName(trainType.name())
                .setIncrementFactor(trainType.incrementFactor())
                .build();
    }

    public static ServiceClassModel fromDto(ServiceClass request) {
        return new ServiceClassModel(
                request.getName(),
                request.getIncrementFactor()
        );
    }

    public static TrainResponse toDto(
            TrainEntity train, TrainType type, PathResponse path
    ){
        return TrainResponse.newBuilder()
                .setId(train.id())
                .setName(train.name())
                .setType(toDto(type))
                .setDepartureTime(train.departureTime())
                .setPath(path)
                .addAllSeats(train.classSeats().entrySet().stream()
                        .map(entry-> ClassSeats.newBuilder()
                                        .setServiceClass(toDto(entry.getKey()))
                                        .setCount(entry.getValue()).build())
                        .toList())
                .putAllPlatformChoices(train.platformChoice())
                .build();
    }

    public static TrainQueryParams fromDto(TrainQueryParameters request) {
        TrainQueryParams.Builder builder = TrainQueryParams.builder();
        if (request.hasType())
            builder.setType(request.getType());
        if (request.hasServiceClass())
            builder.setServiceClass(request.getServiceClass());
        if (request.hasDateRange())
            builder.setDateRange(request.getDateRange().getFrom(), request.getDateRange().getTo());
        if (request.hasPathId())
            builder.setPathId(request.getPathId());
        return builder.build();
    }

}
