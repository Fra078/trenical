package it.trenical.promotion.models;

public class TravelContext {

    private final int pathId;
    private final long date;
    private final double basePrice;
    private final String trainType;
    private final boolean isFidelty;
    private final int ticketCount;
    private final String serviceClass;

    private TravelContext(Builder builder) {
        this.pathId = builder.pathId;
        this.date = builder.date;
        this.basePrice = builder.basePrice;
        this.trainType = builder.trainType;
        this.isFidelty = builder.isFidelty;
        this.ticketCount = builder.ticketCount;
        this.serviceClass = builder.serviceClass;
    }

    public int getPathId() { return pathId; }
    public long getDate() { return date; }
    public double getBasePrice() { return basePrice; }
    public String getTrainType() { return trainType; }
    public boolean isFidelty() { return isFidelty; }
    public int getTicketCount() { return ticketCount; }
    public String getServiceClass() { return serviceClass; }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(TravelContext prototype) {
        return new Builder(prototype);
    }

    public static final class Builder {
        private int pathId;
        private long date;
        private double basePrice;
        private String trainType;
        private boolean isFidelty;
        private int ticketCount;
        private String serviceClass;

        private Builder() {}

        public Builder(TravelContext prototype) {
            this.pathId = prototype.pathId;
            this.date = prototype.date;
            this.basePrice = prototype.basePrice;
            this.trainType = prototype.trainType;
            this.isFidelty = prototype.isFidelty;
            this.ticketCount = prototype.ticketCount;
            this.serviceClass = prototype.serviceClass;
        }

        public Builder setPathId(int pathId) {
            this.pathId = pathId;
            return this;
        }

        public Builder setDate(long date) {
            this.date = date;
            return this;
        }

        public Builder setBasePrice(double basePrice) {
            this.basePrice = basePrice;
            return this;
        }

        public Builder setTrainType(String trainType) {
            this.trainType = trainType;
            return this;
        }

        public Builder setIsFidelty(boolean isFidelty) {
            this.isFidelty = isFidelty;
            return this;
        }

        public Builder setTicketCount(int ticketCount) {
            this.ticketCount = ticketCount;
            return this;
        }

        public Builder setServiceClass(String serviceClass) {
            this.serviceClass = serviceClass;
            return this;
        }

        public TravelContext build() {
            return new TravelContext(this);
        }
    }
}