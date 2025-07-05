package it.trenical.promotion.models.effects;

import it.trenical.promotion.models.PromotionEffect;

public class CountTicketDiscountEffect implements PromotionEffect {

    private final double factor;
    private final int applyCount;

    public CountTicketDiscountEffect(int percentage, int count) {
        this.factor = percentage/100.0;
        this.applyCount = count;
    }

    @Override
    public double calculatePrice(double basePrice, int count) {
        if (count < applyCount)
            return basePrice*count;
        double base = (count-applyCount)*basePrice;
        return basePrice*factor*applyCount + base;
    }
}
