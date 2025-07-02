package it.trenical.trainmanager.service;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import it.trenical.proto.train.*;
import it.trenical.trainmanager.dao.ServiceClassDao;
import it.trenical.trainmanager.dao.TrainTypeDao;
import it.trenical.trainmanager.managers.TrainManager;
import it.trenical.trainmanager.mapper.TrainMapper;
import it.trenical.trainmanager.models.ServiceClassModel;
import it.trenical.trainmanager.models.TrainType;
import it.trenical.trainmanager.repository.ServiceClassRepository;
import it.trenical.trainmanager.repository.TrainTypeRepository;

public class TrainManagerService extends TrainManagerGrpc.TrainManagerImplBase {

    private final TrainTypeDao trainTypeDao = new TrainTypeRepository();
    private final ServiceClassDao serviceClassDao = new ServiceClassRepository();
    private final TrainManager trainManager = new TrainManager();


    @Override
    public void getAllTrainTypes(Empty request, StreamObserver<TrainTypeResponse> responseObserver) {
        trainTypeDao.getAll(type-> responseObserver.onNext(TrainMapper.toDto(type)));
        responseObserver.onCompleted();
    }

    @Override
    public void getTrainTypeByName(TrainTypeByNameRequest request, StreamObserver<TrainTypeResponse> responseObserver) {
        TrainType result = trainTypeDao.getByName(request.getName());
        if (result == null)
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
        else {
            responseObserver.onNext(TrainMapper.toDto(result));
            responseObserver.onCompleted();
        }
    }

    @Override
    public void registerTrainType(RegisterTrainTypeRequest request, StreamObserver<Empty> responseObserver) {
        boolean done = trainTypeDao.register(TrainMapper.fromRequest(request));
        if (done){
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.ALREADY_EXISTS.asRuntimeException());
        }
    }

    @Override
    public void removeTrainTypeByName(TrainTypeByNameRequest request, StreamObserver<Empty> responseObserver) {
        boolean done = trainTypeDao.removeByName(request.getName());
        if (done){
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
        }
    }

    @Override
    public void getAllServiceClasses(Empty request, StreamObserver<ServiceClass> responseObserver) {
        serviceClassDao.getAll(type-> responseObserver.onNext(TrainMapper.toDto(type)));
        responseObserver.onCompleted();
    }

    @Override
    public void getServiceClassByName(ServiceClassByNameRequest request, StreamObserver<ServiceClass> responseObserver) {
        ServiceClassModel result = serviceClassDao.getByName(request.getName());
        if (result == null)
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
        else {
            responseObserver.onNext(TrainMapper.toDto(result));
            responseObserver.onCompleted();
        }
    }

    @Override
    public void registerServiceClass(ServiceClass request, StreamObserver<Empty> responseObserver) {
        boolean done = serviceClassDao.register(TrainMapper.fromDto(request));
        if (done){
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.ALREADY_EXISTS.asRuntimeException());
        }
    }

    @Override
    public void removeServiceClassByName(ServiceClassByNameRequest request, StreamObserver<Empty> responseObserver) {
        boolean done = serviceClassDao.removeByName(request.getName());
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
