package it.trenical.promotion.models.effects;

import it.trenical.promotion.models.Effect;
import it.trenical.promotion.models.EffectVisitor;

public class FixedPriceEffect implements Effect {
    private final double price;
    public FixedPriceEffect(double price) {
        this.price = price;
    }

    @Override
    public double calculatePrice(double basePrice, int count) {
        return price*count;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public <T> T accept(EffectVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
