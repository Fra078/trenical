package it.trenical.trainmanager.mapper;

import it.trenical.proto.train.RegisterTrainTypeRequest;
import it.trenical.proto.train.ServiceClass;
import it.trenical.proto.train.TrainTypeResponse;
import it.trenical.trainmanager.models.ServiceClassModel;
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

}
