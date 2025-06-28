package it.trenical.server.railway.services;

import it.trenical.proto.railway.RailwayServiceAdminGrpc;
import it.trenical.server.railway.dao.StationDao;

public class AdminRailwayService extends RailwayServiceAdminGrpc.RailwayServiceAdminImplBase {
    private StationDao stationDao;

    public AdminRailwayService(StationDao stationDao) {
        this.stationDao = stationDao;
    }
}
