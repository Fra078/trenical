package it.trenical.trainmanager.models;

import java.util.Map;

public record TrainEntity(Integer id,
                          String name,
                          String type,
                          long departureTime,
                          int pathId,
                          Map<ServiceClassModel, Integer> classSeats,
                          Map<String, Integer> platformChoice) {

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(TrainEntity prototype) {
        return new Builder(prototype);
    }

    public static final class Builder {
        private Integer id;
        private String name;
        private String type;
        private Long departureTime;
        private int pathId;
        private Map<ServiceClassModel, Integer> classSeats;
        private Map<String, Integer> platformChoice;

        public TrainEntity build() {
            return new TrainEntity(id, name, type, departureTime, pathId, classSeats, platformChoice);
        }

        private Builder() {}

        private Builder(TrainEntity trainEntity) {
            setId(trainEntity.id);
            setName(trainEntity.name);
            setType(trainEntity.type);
            setDepartureTime(trainEntity.departureTime);
            setPathId(trainEntity.pathId);
            setClassSeats(trainEntity.classSeats);
            setPlatformChoice(trainEntity.platformChoice);
        }

        public Integer getId() {
            return id;
        }

        public Builder setId(Integer id) {
            this.id = id;
            return this;
        }

        public String getName() {
            return name;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public String getType() {
            return type;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public long getDepartureTime() {
            return departureTime;
        }

        public Builder setDepartureTime(long departureTime) {
            this.departureTime = departureTime;
            return this;
        }

        public int getPathId() {
            return pathId;
        }

        public Builder setPathId(int pathId) {
            this.pathId = pathId;
            return this;
        }

        public Map<ServiceClassModel, Integer> getClassSeats() {
            return classSeats;
        }

        public Builder setClassSeats(Map<ServiceClassModel, Integer> classSeats) {
            this.classSeats = Map.copyOf(classSeats);
            return this;
        }

        public Map<String, Integer> getPlatformChoice() {
            return platformChoice;
        }
        public Builder setPlatformChoice(Map<String, Integer> platformChoice) {
            this.platformChoice = Map.copyOf(platformChoice);
            return this;
        }

    }

}
