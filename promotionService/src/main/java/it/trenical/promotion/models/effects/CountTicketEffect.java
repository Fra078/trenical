package it.trenical.promotion.models.effects;

import it.trenical.promotion.models.Effect;
import it.trenical.promotion.models.EffectVisitor;

public class CountTicketEffect implements Effect {

    private final int discountPercentage;
    private final int applyCount;

    public CountTicketEffect(int discountPercentage, int count) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new IllegalArgumentException("Percentage must be from 0 to 100.");
        }
        this.discountPercentage = discountPercentage;
        this.applyCount = count;
    }

    @Override
    public <T> T accept(EffectVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public double calculatePrice(double basePrice, int count) {
        double discountFactor = (100.0 - discountPercentage) / 100.0;
        if (count <= applyCount)
            return basePrice * discountFactor * count;
        return basePrice * (count - applyCount * discountPercentage);
    }

    public int getPercentage() {
        return discountPercentage;
    }

    public int getApplyCount() {
        return applyCount;
    }
}
