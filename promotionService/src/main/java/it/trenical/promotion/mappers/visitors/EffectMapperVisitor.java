package it.trenical.promotion.mappers.visitors;

import it.trenical.promotion.models.effects.*;
import it.trenical.promotion.models.EffectVisitor;
import it.trenical.promotion.proto.*; // Importa le tue classi proto generate

public class EffectMapperVisitor implements EffectVisitor<PromotionEffect> {

    @Override
    public PromotionEffect visit(CountTicketEffect effect) {
        var proto = CountTicketDiscountEffect.newBuilder()
            .setCount(effect.getApplyCount())
            .setDiscountPercentage(effect.getPercentage())
            .build();
        return PromotionEffect.newBuilder().setCountTicketDiscount(proto).build();
    }

    @Override
    public PromotionEffect visit(FixedAmountEffect effect) {
        var proto = FixedAmountDiscountEffect.newBuilder().setAmount(effect.getAmount()).build();
        return PromotionEffect.newBuilder().setFixedAmountDiscount(proto).build();
    }

    @Override
    public PromotionEffect visit(FixedPriceEffect effect) {
        var proto = FixedPriceDiscountEffect.newBuilder().setPrice(effect.getPrice()).build();
        return PromotionEffect.newBuilder().setFixedPrice(proto).build();
    }

    @Override
    public PromotionEffect visit(PercentageEffect effect) {
        var proto = PercentageDiscountEffect.newBuilder().setPercentage(effect.getDiscountPercentage()).build();
        return PromotionEffect.newBuilder().setPercentageDiscount(proto).build();
    }
}