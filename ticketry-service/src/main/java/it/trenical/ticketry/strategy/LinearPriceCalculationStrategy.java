package it.trenical.ticketry.strategy;

public class LinearPriceCalculationStrategy implements PriceCalculationStrategy {
    @Override
    public double computePrice(double distance, double classFactor, double typeCostPerKm) {
        return distance * classFactor * typeCostPerKm;
    }
}
