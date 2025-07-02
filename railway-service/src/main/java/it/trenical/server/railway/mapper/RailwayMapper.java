package it.trenical.server.railway.mapper;

import it.trenical.proto.railway.LinkResponse;
import it.trenical.proto.railway.PathResponse;
import it.trenical.proto.railway.StationList;
import it.trenical.proto.railway.StationResponse;
import it.trenical.server.railway.models.Link;
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
                .setId(path.id())
                .addAllLinks(path.links().stream().map(RailwayMapper::toDto).toList())
                .build();
    }

    public static LinkResponse toDto(Link link) {
        return LinkResponse.newBuilder()
                .setArrival(toDto(link.arrival()))
                .setDeparture(toDto(link.departure()))
                .setDistance(link.distance())
                .build();
    }
}
