package it.trenical.promotion.models.effects;

import it.trenical.promotion.models.Effect;
import it.trenical.promotion.models.EffectVisitor;

public class PercentageEffect implements Effect {

    private final int discountPercentage;

    public PercentageEffect(int discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Percentage must be from 0 to 100.");
        }
        this.discountPercentage = discountPercentage;
    }

    @Override
    public double calculatePrice(double basePrice, int count) {
        double discountFactor = (100.0 - discountPercentage) / 100.0;
        return basePrice * count * discountFactor;
    }

    @Override
    public <T> T accept(EffectVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }
}
