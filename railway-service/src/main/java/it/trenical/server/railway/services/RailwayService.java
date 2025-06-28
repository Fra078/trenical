package it.trenical.server.railway.services;

import io.grpc.stub.StreamObserver;
import it.trenical.proto.railway.RailwayServiceGrpc;
import it.trenical.proto.railway.StationList;
import it.trenical.server.railway.dao.StationDao;

public class RailwayService extends RailwayServiceGrpc.RailwayServiceImplBase {

    private StationDao stationDao;

    public RailwayService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Override
    public void getAllStations(com.google.protobuf.Empty request, StreamObserver<StationList> responseObserver) {
        super.getAllStations(request, responseObserver);
    }
}
