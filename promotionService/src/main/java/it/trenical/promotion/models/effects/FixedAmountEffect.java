package it.trenical.promotion.models.effects;

import it.trenical.promotion.models.PromotionEffect;

public class FixedAmountEffect implements PromotionEffect {
    private final double amount;

    public FixedAmountEffect(double amount) {
        this.amount = amount;
    }

    @Override
    public double calculatePrice(double basePrice, int count) {
        return Math.max(0, basePrice-amount)*count;
    }
}
