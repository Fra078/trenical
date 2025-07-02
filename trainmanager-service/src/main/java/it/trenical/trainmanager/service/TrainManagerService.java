package it.trenical.trainmanager.service;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import it.trenical.proto.train.*;
import it.trenical.server.database.DatabaseManager;
import it.trenical.trainmanager.client.RailwayClient;
import it.trenical.trainmanager.db.TrainDb;
import it.trenical.trainmanager.managers.TrainManager;
import it.trenical.trainmanager.mapper.TrainMapper;
import it.trenical.trainmanager.models.ServiceClassModel;
import it.trenical.trainmanager.models.TrainType;
import it.trenical.trainmanager.repository.ServiceClassRepository;
import it.trenical.trainmanager.repository.TrainRepository;
import it.trenical.trainmanager.repository.db.ServiceClassJdbcRepository;
import it.trenical.trainmanager.repository.db.TrainJdbcRepository;
import it.trenical.trainmanager.repository.TrainTypeRepository;
import it.trenical.trainmanager.repository.db.TrainTypeJdbcRepository;
import it.trenical.trainmanager.strategy.impl.DefaultPlatformAssignmentStrategy;

import java.util.Optional;

public class TrainManagerService extends TrainManagerGrpc.TrainManagerImplBase {

    private final TrainManager trainManager;
    private final ServiceClassRepository classRepository;
    private final TrainTypeRepository typeRepository;

    public TrainManagerService(TrainManager trainManager, ServiceClassRepository classRepository, TrainTypeRepository typeRepository) {
        this.trainManager = trainManager;
        this.classRepository = classRepository;
        this.typeRepository = typeRepository;
    }

    @Override
    public void getAllTrainTypes(Empty request, StreamObserver<TrainTypeResponse> responseObserver) {
        typeRepository.findAll(type-> responseObserver.onNext(TrainMapper.toDto(type)));
        responseObserver.onCompleted();
    }

    @Override
    public void getTrainTypeByName(TrainTypeByNameRequest request, StreamObserver<TrainTypeResponse> responseObserver) {
        Optional<TrainType> result = typeRepository.findByName(request.getName());
        if (result.isEmpty())
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
        else {
            responseObserver.onNext(TrainMapper.toDto(result.get()));
            responseObserver.onCompleted();
        }
    }

    @Override
    public void registerTrainType(RegisterTrainTypeRequest request, StreamObserver<Empty> responseObserver) {
        boolean done = typeRepository.save(TrainMapper.fromRequest(request));
        if (done){
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.ALREADY_EXISTS.asRuntimeException());
        }
    }

    @Override
    public void removeTrainTypeByName(TrainTypeByNameRequest request, StreamObserver<Empty> responseObserver) {
        boolean done = typeRepository.deleteByName(request.getName());
        if (done){
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
        }
    }

    @Override
    public void getAllServiceClasses(Empty request, StreamObserver<ServiceClass> responseObserver) {
        classRepository.findAll(type-> responseObserver.onNext(TrainMapper.toDto(type)));
        responseObserver.onCompleted();
    }

    @Override
    public void getServiceClassByName(ServiceClassByNameRequest request, StreamObserver<ServiceClass> responseObserver) {
        Optional<ServiceClassModel> result = classRepository.findByName(request.getName());
        if (result.isEmpty())
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
        else {
            responseObserver.onNext(TrainMapper.toDto(result.get()));
            responseObserver.onCompleted();
        }
    }

    @Override
    public void registerServiceClass(ServiceClass request, StreamObserver<Empty> responseObserver) {
        boolean done = classRepository.save(TrainMapper.fromDto(request));
        if (done){
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.ALREADY_EXISTS.asRuntimeException());
        }
    }

    @Override
    public void removeServiceClassByName(ServiceClassByNameRequest request, StreamObserver<Empty> responseObserver) {
        boolean done = classRepository.deleteByName(request.getName());
        if (done){
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
        }
    }

    @Override
    public void registerTrain(RegisterTrainRequest request, StreamObserver<TrainResponse> responseObserver) {
        try {
            responseObserver.onNext(trainManager.register(request));
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e) {
            e.printStackTrace();
            responseObserver.onError(e);
        } catch (RuntimeException e) {
            e.printStackTrace();
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getTrainById(TrainId request, StreamObserver<TrainResponse> responseObserver) {
        try {
            responseObserver.onNext(trainManager.getTrainById(request));
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e) {
            e.printStackTrace();
            responseObserver.onError(e);
        } catch (RuntimeException e) {
            e.printStackTrace();
            responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}
