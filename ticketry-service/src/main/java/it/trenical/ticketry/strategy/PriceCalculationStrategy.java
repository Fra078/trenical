package it.trenical.ticketry.strategy;

public interface PriceCalculationStrategy {

    double computePrice(double distance, double classFactor, double typeCostPerKm);

}
