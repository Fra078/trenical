package it.trenical.ticketry.mappers;

import it.trenical.proto.train.StationPair;
import it.trenical.proto.train.TrainQueryParameters;
import it.trenical.ticketry.proto.TripQueryParams;

public class TripMapper {
    private TripMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static TrainQueryParameters mapToTrain(TripQueryParams params) {
        TrainQueryParameters.Builder builder = TrainQueryParameters.newBuilder();
        if (params.hasServiceClass())
            builder.setServiceClass(params.getServiceClass());
        if (params.hasTrainType())
            builder.setType(params.getTrainType());
        builder.setDateRange(params.getDate());
        builder.setStations(StationPair.newBuilder()
                .setDeparture(params.getDeparture())
                .setArrival(params.getArrival())
                .build());
        return builder.build();
    }
}
