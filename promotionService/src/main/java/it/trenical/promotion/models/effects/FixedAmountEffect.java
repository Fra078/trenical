package it.trenical.promotion.models.effects;

import it.trenical.promotion.models.Effect;
import it.trenical.promotion.models.EffectVisitor;

public class FixedAmountEffect implements Effect {
    private final double amount;

    public FixedAmountEffect(double amount) {
        this.amount = amount;
    }

    @Override
    public double calculatePrice(double basePrice, int count) {
        return Math.max(0, basePrice-amount)*count;
    }

    @Override
    public <T> T accept(EffectVisitor<T> visitor) {
        return visitor.visit(this);
    }

    public double getAmount() {
        return amount;
    }
}
