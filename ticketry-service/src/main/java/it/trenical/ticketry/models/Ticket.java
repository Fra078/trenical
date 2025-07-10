package it.trenical.ticketry.models;

public class Ticket {

    private final int trainId;
    private final int id;
    private final String className;
    private final String transactionId;
    private final String promoId;
    private final String customerId;
    private final String departure;
    private final String arrival;
    private final Status status;

    public enum Status {
        CONFIRMED,
        WAITING,
        BOOKED,
        CANCELLED
    }

    private Ticket(Builder builder) {
        this.trainId = builder.trainId;
        this.id = builder.id;
        this.className = builder.className;
        this.transactionId = builder.transactionId;
        this.promoId = builder.promoId;
        this.customerId = builder.customerId;
        this.departure = builder.departure;
        this.arrival = builder.arrival;
        this.status = builder.status;
    }

    public int getTrainId() {
        return trainId;
    }

    public int getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getPromoId() {
        return promoId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getDeparture() {
        return departure;
    }

    public String getArrival() {
        return arrival;
    }

    public Status getStatus() {
        return status;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(Ticket prototype) {
        return new Builder(prototype);
    }

    public static class Builder {
        private int trainId;
        private int id;
        private String className;
        private String transactionId;
        private String promoId;
        private String customerId;
        private String departure;
        private String arrival;
        private Status status;

        private Builder() {}

        private Builder(Ticket prototype) {
            this.trainId = prototype.trainId;
            this.id = prototype.id;
            this.className = prototype.className;
            this.transactionId = prototype.transactionId;
            this.promoId = prototype.promoId;
            this.customerId = prototype.customerId;
            this.departure = prototype.departure;
            this.arrival = prototype.arrival;
            this.status = prototype.status;
        }

        public Builder trainId(int trainId) {
            this.trainId = trainId;
            return this;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder className(String className) {
            this.className = className;
            return this;
        }

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder promoId(String promoId) {
            this.promoId = promoId;
            return this;
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder departure(String departure) {
            this.departure = departure;
            return this;
        }

        public Builder arrival(String arrival) {
            this.arrival = arrival;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Ticket build() {
            return new Ticket(this);
        }
    }
}