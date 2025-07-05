package it.trenical.promotion.models.effects;

import it.trenical.promotion.models.PromotionEffect;

public class FixedPriceEffect implements PromotionEffect {
    private final double price;
    public FixedPriceEffect(double price) {
        this.price = price;
    }

    @Override
    public double calculatePrice(double basePrice, int count) {
        return basePrice*count;
    }
}
