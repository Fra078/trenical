package it.trenical.ticketry.mappers;

import it.trenical.promotion.proto.TravelContextMessage;
import it.trenical.proto.railway.PathResponse;
import it.trenical.proto.railway.StopResponse;
import it.trenical.proto.train.ServiceClass;
import it.trenical.proto.train.TrainResponse;
import it.trenical.ticketry.proto.TripQueryParams;

import java.util.Collection;
import java.util.List;

public class TravelContextBuilder {

    public TravelContextMessage build(
            TrainResponse train,
            TripQueryParams queryParams,
            Collection<ServiceClass> classes
    ) {
        return TravelContextMessage.newBuilder()
                .setTrainId(train.getId())
                .setTrainName(train.getName())
                .setTrainType(train.getType())
                .setTicketCount(queryParams.getTicketCount())
                .setRouteInfo(calculateRouteInfo(train, queryParams.getDeparture(), queryParams.getArrival()))
                .build();
    }

    private TravelContextMessage.RouteInfo calculateRouteInfo(
            TrainResponse train,
            String departure,
            String arrival
    ) {
        double departureDistance = 0, arrivalDistance = 0;
        PathResponse path = train.getPath();
        List<StopResponse> stops = path.getStopsList();
        for (StopResponse stop : stops) {
            arrivalDistance += stop.getDistance();
            if (stop.getStation().getName().equals(departure))
                departureDistance = arrivalDistance;
            else if (stop.getStation().getName().equals(arrival))
                break;
        }

        double speed = train.getType().getSpeed();

        long departureTime = train.getDepartureTime() + Math.round(departureDistance * 3600 / speed);
        long arrivalTime = train.getDepartureTime() + Math.round(arrivalDistance * 3600 / speed);
        return TravelContextMessage.RouteInfo.newBuilder()
                .setPathId(path.getId())
                .setDepartureStation(departure)
                .setDepartureTime(departureTime)
                .setArrivalStation(arrival)
                .setArrivalTime(arrivalTime)
                .setDistance(departureDistance-arrivalDistance)
                .build();
    }

}
