package it.trenical.trainmanager.mapper;

import it.trenical.proto.train.ChangeTrainStationStopRequest;
import it.trenical.train.proto.TrainUpdate;
import it.trenical.train.proto.TrainUpdateTrackChange;

public final class UpdateMapper {
    private UpdateMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static TrainUpdate fromRequest(ChangeTrainStationStopRequest req){
        return TrainUpdate.newBuilder()
                .setTrainId(req.getTrainId())
                .setTrackChange(TrainUpdateTrackChange.newBuilder()
                        .setStationName(req.getStationName())
                        .setTrack(req.getTrackNumber())
                        .build())
                .build();
    }
}
