package it.trenical.server.railway.mapper;

import it.trenical.proto.railway.StationList;
import it.trenical.proto.railway.StationResponse;
import it.trenical.server.railway.models.Station;

public class RailwayMapper {
    private RailwayMapper() {}

    public static StationResponse toDto(Station station) {
        return StationResponse.newBuilder()
                .setCity(station.city())
                .setName(station.name())
                .setPlatformCount(station.trackCount())
                .build();
    }
}
