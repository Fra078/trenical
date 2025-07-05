package it.trenical.promotion.models.effects;

import it.trenical.promotion.models.PromotionEffect;

public class PercentageEffect implements PromotionEffect {

    private final double factor;

    public PercentageEffect(int percentage) {
        factor = (double) percentage / 100;
    }

    @Override
    public double calculatePrice(double basePrice, int count) {
        return basePrice * factor * count;
    }
}
