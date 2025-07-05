package it.trenical.ticketry.managers;

import it.trenical.proto.railway.StopResponse;
import it.trenical.ticketry.clients.TrainClient;
import it.trenical.ticketry.mappers.TripMapper;
import it.trenical.ticketry.proto.TripMode;
import it.trenical.ticketry.proto.TripQueryParams;
import it.trenical.ticketry.proto.TripSolution;

import java.util.List;
import java.util.function.Consumer;

public class TripManager {

    private final TrainClient trainClient;

    public TripManager(TrainClient trainClient) {
        this.trainClient = trainClient;
    }

    public void getTripSolutions(TripQueryParams request, Consumer<TripSolution> consumer) {
        trainClient.getTrainForPath(TripMapper.mapToTrain(request), train->{
            TripSolution.Builder builder = TripSolution.newBuilder()
                                .setTrainId(train.getId())
                                .setTrainName(train.getName())
                                .setType(train.getType())
                                .setDepartureStation(request.getDeparture())
                                .setArrivalStation(request.getArrival());
            long baseTime = train.getDepartureTime();
            double departureDist = 0, arrivalDist = 0;
            double speed = train.getType().getSpeed();
            List<StopResponse> stops = train.getPath().getStopsList();
            for (StopResponse stop : stops) {
                arrivalDist += stop.getDistance();
                if (stop.getStation().getName().equals(request.getDeparture()))
                    departureDist = arrivalDist;
                else if (stop.getStation().getName().equals(request.getArrival()))
                    break;
            }
            long departureTime = baseTime + Math.round(departureDist * 3600 / speed);
            long arrivalTime = baseTime + Math.round(arrivalDist * 3600 / speed);
            builder.setArrivalTime(arrivalTime);
            builder.setDepartureTime(departureTime);
            double basePrice = (arrivalDist- departureDist)*train.getType().getCostPerKm();

            train.getSeatsList().stream()
                            .filter(cs-> !request.hasServiceClass() ||
                                    request.getServiceClass().equals(cs.getServiceClass().getName()))
                            .map(cs-> TripMode.newBuilder()
                                    .setServiceClass(cs.getServiceClass())
                                    .setTotalPrice(basePrice*cs.getServiceClass().getIncrementFactor())
                                    .build()
                            ).forEach(builder::addModes);

            consumer.accept(builder.build());
        });
    }

}
