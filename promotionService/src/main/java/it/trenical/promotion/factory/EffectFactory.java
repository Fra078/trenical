package it.trenical.promotion.factory;

import it.trenical.promotion.models.PromotionEffect;
import it.trenical.promotion.models.effects.CountTicketDiscountEffect;
import it.trenical.promotion.models.effects.FixedAmountEffect;
import it.trenical.promotion.models.effects.FixedPriceEffect;
import it.trenical.promotion.models.effects.PercentageEffect;
import it.trenical.promotion.proto.Promotion;

public final class EffectFactory {
    private EffectFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static PromotionEffect buildFromPromotionDto(Promotion promo) {
        return switch (promo.getEffectTypeCase()){
            case PERCENTAGE_DISCOUNT ->
                    new PercentageEffect(promo.getPercentageDiscount().getPercentage());
            case FIXED_AMOUNT_DISCOUNT ->
                new FixedAmountEffect(promo.getFixedAmountDiscount().getAmount());
            case FIXED_PRICE ->
                    new FixedPriceEffect(promo.getFixedPrice().getPrice());
            case COUNT_TICKET_DISCOUNT ->
                    new CountTicketDiscountEffect(
                            promo.getCountTicketDiscount().getDiscountPercentage(),
                            promo.getCountTicketDiscount().getCount());
            case EFFECTTYPE_NOT_SET -> null;
        };
    }
}
