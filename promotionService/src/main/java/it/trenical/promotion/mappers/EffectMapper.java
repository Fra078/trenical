package it.trenical.promotion.mappers;

import it.trenical.promotion.mappers.visitors.EffectMapperVisitor;
import it.trenical.promotion.models.Effect;
import it.trenical.promotion.models.effects.CountTicketEffect;
import it.trenical.promotion.models.effects.FixedAmountEffect;
import it.trenical.promotion.models.effects.FixedPriceEffect;
import it.trenical.promotion.models.effects.PercentageEffect;
import it.trenical.promotion.proto.CountTicketDiscountEffect;
import it.trenical.promotion.proto.PromotionEffect;

public class EffectMapper {

    private final EffectMapperVisitor visitor;

    public EffectMapper(EffectMapperVisitor visitor) {
        this.visitor = visitor;
    }

    public Effect createFromProto(PromotionEffect protoEffect) {
        return switch (protoEffect.getTypeCase()) {
            case PERCENTAGE_DISCOUNT -> new PercentageEffect(protoEffect.getPercentageDiscount().getPercentage());
            case FIXED_AMOUNT_DISCOUNT -> new FixedAmountEffect(protoEffect.getFixedAmountDiscount().getAmount());
            case FIXED_PRICE -> new FixedPriceEffect(protoEffect.getFixedPrice().getPrice());
            case COUNT_TICKET_DISCOUNT -> {
                CountTicketDiscountEffect countProto = protoEffect.getCountTicketDiscount();
                yield new CountTicketEffect(countProto.getDiscountPercentage(), countProto.getCount());
            }
            default -> throw new IllegalArgumentException("Not supported type " + protoEffect.getTypeCase());
        };
    }

    public PromotionEffect toProto(Effect effect) {
        return effect.accept(visitor);
    }
}
