package it.trenical.ticketry.managers;

import it.trenical.proto.railway.PathResponse;
import it.trenical.proto.railway.StopResponse;
import it.trenical.proto.train.ServiceClass;
import it.trenical.proto.train.TrainResponse;
import it.trenical.ticketry.proto.TripQueryParams;
import it.trenical.ticketry.strategy.PriceCalculationStrategy;
import it.trenical.travel.proto.TravelSolution;

import java.util.Collection;
import java.util.List;

public class TravelSolutionFactory {

    private final PriceCalculationStrategy priceCalculationStrategy;

    public TravelSolutionFactory(PriceCalculationStrategy priceCalculationStrategy) {
        this.priceCalculationStrategy = priceCalculationStrategy;
    }

    public TravelSolution buildFrom(
            TrainResponse train,
            TripQueryParams queryParams,
            Collection<ServiceClass> serviceClasses
    ) {
        return buildFrom(train, queryParams.getDeparture(), queryParams.getArrival(), queryParams.getUsername(), queryParams.getTicketCount(), serviceClasses);
    }

    public TravelSolution buildFrom(
            TrainResponse train,
            String departure,
            String arrival,
            String username,
            int ticketCount,
            Collection<ServiceClass> serviceClasses
    ) {

        TravelSolution.RouteInfo routeInfo = calculateRouteInfo(train, departure, arrival);
        List<TravelSolution.Mode> modes = serviceClasses.stream()
                .map(sc-> TravelSolution.Mode.newBuilder()
                        .setServiceClass(sc)
                        .setPrice(
                                priceCalculationStrategy.computePrice(routeInfo.getDistance(),
                                        sc.getIncrementFactor(),
                                        train.getType().getCostPerKm()))
                        .build())
                .toList();
        return TravelSolution.newBuilder()
                .setTrainId(train.getId())
                .setTrainName(train.getName())
                .setType(train.getType())
                .setRouteInfo(routeInfo)
                .setUserId(username)
                .setTicketCount(ticketCount)
                .addAllModes(modes)
                .build();
    }

    private TravelSolution.RouteInfo calculateRouteInfo(
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
        return TravelSolution.RouteInfo.newBuilder()
                .setPathId(path.getId())
                .setDepartureStation(departure)
                .setDepartureTime(departureTime)
                .setArrivalStation(arrival)
                .setArrivalTime(arrivalTime)
                .setDistance(arrivalDistance-departureDistance)
                .build();
    }



}
