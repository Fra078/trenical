package it.trenical.server.railway.services;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import it.trenical.proto.railway.*;
import it.trenical.server.railway.managers.StationManager;
import it.trenical.server.railway.repositories.StationRepository;
import it.trenical.server.railway.managers.PathManager;
import it.trenical.server.railway.mapper.RailwayMapper;
import it.trenical.server.railway.models.Path;

import java.util.Map;
import java.util.NoSuchElementException;

public class RailwayService extends RailwayServiceGrpc.RailwayServiceImplBase {

    private final StationManager stationManager;
    private final PathManager pathManager;

    public RailwayService(StationManager stationManager, PathManager pathManager) {
        this.stationManager = stationManager;
        this.pathManager = pathManager;
    }

    @Override
    public void getAllStations(Empty request, StreamObserver<StationResponse> responseObserver) {
        stationManager.getAll(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public void registerStation(RegisterStationRequest request, StreamObserver<Empty> responseObserver) {
        try {
            stationManager.registerStation(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (StatusRuntimeException ex){
            responseObserver.onError(ex);
        }
    }

    @Override
    public void getStation(GetStationRequest request, StreamObserver<StationResponse> responseObserver) {
        try {
            responseObserver.onNext(stationManager.getByName(request));
            responseObserver.onCompleted();
        } catch (StatusRuntimeException exc){
            responseObserver.onError(exc);
        }
    }

    @Override
    public void deleteStation(DeleteStationRequest request, StreamObserver<Empty> responseObserver) {
        try {
            stationManager.deleteStation(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (StatusRuntimeException exc){
            responseObserver.onError(exc);
        }
    }

    @Override
    public void linkStations(LinkStationsRequest request, StreamObserver<Empty> responseObserver) {
        try {
            stationManager.linkStations(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e){
            responseObserver.onError(e);
        }
    }

    @Override
    public void unLinkStations(UnlinkStationsRequest request, StreamObserver<Empty> responseObserver) {
        try {
            stationManager.unlinkStations(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e){
            responseObserver.onError(e);
        }
    }

    @Override
    public void getNearStations(GetNearStationsRequest request, StreamObserver<GetNearStationsResponse> responseObserver) {
        try {
            responseObserver.onNext(stationManager.getNearStations(request));
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e){
            responseObserver.onError(e);
        }
    }

    @Override
    public void registerPath(RegisterPathRequest request, StreamObserver<RegisterPathResponse> responseObserver) {
        try {
            responseObserver.onNext(pathManager.registerPath(request));
            responseObserver.onCompleted();
        } catch (StatusRuntimeException exc){
            responseObserver.onError(exc);
        }
    }

    @Override
    public void getPath(GetPathRequest request, StreamObserver<PathResponse> responseObserver) {
        try {
            responseObserver.onNext(pathManager.getPath(request));
            responseObserver.onCompleted();
        } catch (StatusRuntimeException exc){
            responseObserver.onError(exc);
        }
    }
}
