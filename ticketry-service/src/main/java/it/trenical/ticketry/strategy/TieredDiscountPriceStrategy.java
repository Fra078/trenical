package it.trenical.ticketry.strategy;

public class TieredDiscountPriceStrategy implements PriceCalculationStrategy {

    private final double distancePerTier;
    private final double discountPerTier;
    private final int maxDiscountTiers;


    public TieredDiscountPriceStrategy(double distancePerTier, double discountPerTier, int maxDiscountTiers) {
        if (distancePerTier <= 0) {
            throw new IllegalArgumentException("Tier distance must be positive!");
        }
        this.distancePerTier = distancePerTier;
        this.discountPerTier = discountPerTier;
        this.maxDiscountTiers = maxDiscountTiers;
    }

    @Override
    public double computePrice(double distance, double classFactor, double typeCostPerKm) {
        double totalCost = 0.0;
        double remainingDistance = distance;
        int currentTier = 0;
        final double basePricePerKm = classFactor * typeCostPerKm;

        while (remainingDistance >= distancePerTier) {
            double currentDiscount = Math.min(currentTier, maxDiscountTiers) * discountPerTier;
            double effectivePrice = basePricePerKm * (1.0 - currentDiscount);
            totalCost += distancePerTier * effectivePrice;
            remainingDistance -= distancePerTier;
            currentTier++;
        }

       if (remainingDistance > 0) {
            double finalDiscount = Math.min(currentTier, maxDiscountTiers) * discountPerTier;
            double effectivePrice = basePricePerKm * (1.0 - finalDiscount);
            totalCost += remainingDistance * effectivePrice;
        }

        return totalCost;
    }
}