package it.trenical.promotion.models;

import it.trenical.promotion.models.effects.CountTicketEffect;
import it.trenical.promotion.models.effects.FixedAmountEffect;
import it.trenical.promotion.models.effects.FixedPriceEffect;
import it.trenical.promotion.models.effects.PercentageEffect;
import it.trenical.promotion.proto.PromotionEffect;

public interface EffectVisitor<T> {
    T visit(CountTicketEffect effect);
    T visit(FixedAmountEffect effect);
    T visit(PercentageEffect effect);
    T visit(FixedPriceEffect effect);
}
