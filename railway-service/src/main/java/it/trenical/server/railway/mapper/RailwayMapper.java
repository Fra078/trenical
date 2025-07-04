package it.trenical.server.railway.mapper;

import it.trenical.proto.railway.PathResponse;
import it.trenical.proto.railway.StationResponse;
import it.trenical.proto.railway.StopResponse;
import it.trenical.server.railway.models.Path;
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

    public static PathResponse toDto(Path path) {
        return PathResponse.newBuilder()
                .setId(path.getId())
                .addAllStops(
                        path.getStops().stream().map(item ->
                                StopResponse.newBuilder()
                                        .setStation(toDto(item.getStation()))
                                        .setDistance(item.getDistance())
                                        .build()
                        ).toList())
                .build();
    }
}
