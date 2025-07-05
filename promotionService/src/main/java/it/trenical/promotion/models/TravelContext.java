package it.trenical.promotion.models;

public record TravelContext(
        int pathId,
        long date,
        String trainType,
        boolean isFidelty,
        int ticketCount
) { }
