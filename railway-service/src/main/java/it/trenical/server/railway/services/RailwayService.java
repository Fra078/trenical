package it.trenical.server.railway.services;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import it.trenical.proto.railway.*;
import it.trenical.server.railway.dao.StationDao;
import it.trenical.server.railway.mapper.RailwayMapper;
import it.trenical.server.railway.models.Station;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class RailwayService extends RailwayServiceGrpc.RailwayServiceImplBase {

    private final StationDao stationDao;

    public RailwayService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public void getAllStations(com.google.protobuf.Empty request, StreamObserver<StationList> responseObserver) {
        responseObserver.onNext(
                StationList.newBuilder()
                        .addAllStations(stationDao.getAll().stream().map(RailwayMapper::toDto).toList())
                        .build()
        );
        responseObserver.onCompleted();
    }

    @Override
    public void registerStation(RegisterStationRequest request, StreamObserver<Empty> responseObserver) {
        if (request.getName().isBlank() || request.getCity().isBlank() || request.getTrackCount() <= 0) {
            responseObserver.onError(Status.INVALID_ARGUMENT.asRuntimeException());
            return;
        }
        boolean done = stationDao.create(request.getName().trim(), request.getCity().trim(), request.getTrackCount());
        if (done) {
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.ALREADY_EXISTS.asRuntimeException());
        }
    }

    @Override
    public void getStation(GetStationRequest request, StreamObserver<StationResponse> responseObserver) {
        try {
            responseObserver.onNext(RailwayMapper.toDto(stationDao.get(request.getName())));
            responseObserver.onCompleted();
        } catch (NoSuchElementException exc){
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
        }
    }

    @Override
    public void deleteStation(DeleteStationRequest request, StreamObserver<Empty> responseObserver) {
        boolean done = stationDao.delete(request.getName());
        if (done) {
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } else {
            responseObserver.onError(Status.NOT_FOUND.asRuntimeException());
        }
    }

    @Override
    public void getNearStations(GetNearStationsRequest request, StreamObserver<StationList> responseObserver) {
        super.getNearStations(request, responseObserver);
    }
}
