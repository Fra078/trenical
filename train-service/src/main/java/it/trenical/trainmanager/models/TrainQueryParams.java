package it.trenical.trainmanager.models;

public record TrainQueryParams(
        String type,
        Long dateFrom,
        Long dateTo,
        Integer pathId,
        String serviceClass
) {

    public static TrainQueryParams.Builder builder() {
        return new TrainQueryParams.Builder();
    }

    public static TrainQueryParams.Builder builder(TrainQueryParams prototype){
        return new TrainQueryParams.Builder(prototype);
    }

    public static class Builder {
        private String type;
        private Long dateFrom;
        private Long dateTo;
        private Integer pathId;
        private String serviceClass;

        private Builder() {}

        private Builder(TrainQueryParams trainQueryParams) {
            this.type = trainQueryParams.type;
            this.dateFrom = trainQueryParams.dateFrom;
            this.dateTo = trainQueryParams.dateTo;
            this.pathId = trainQueryParams.pathId;
            this.serviceClass = trainQueryParams.serviceClass;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }
        public Builder setDateRange(long dateFrom, long dateTo) {
            this.dateFrom = dateFrom;
            this.dateTo = dateTo;
            return this;
        }
        public Builder setPathId(Integer pathId) {
            this.pathId = pathId;
            return this;
        }
        public Builder setServiceClass(String serviceClass) {
            this.serviceClass = serviceClass;
            return this;
        }
        public TrainQueryParams build() {
            return new TrainQueryParams(type, dateFrom, dateTo, pathId, serviceClass);
        }

        public String getType() {
            return type;
        }
        public Long getDateFrom() {
            return dateFrom;
        }
        public Long getDateTo() {
            return dateTo;
        }
        public Integer getPathId() {
            return pathId;
        }
        public String getServiceClass() {
            return serviceClass;
        }
    }
}
