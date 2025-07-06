package it.trenical.promotion.mappers;

import it.trenical.promotion.models.Promotion;
import it.trenical.promotion.proto.*;

import java.util.stream.Collectors;

public class PromotionMapper {

    private final ConditionMapper conditionMapper;
    private final EffectMapper effectMapper;

    public PromotionMapper(ConditionMapper conditionMapper, EffectMapper effectMapper) {
        this.conditionMapper = conditionMapper;
        this.effectMapper = effectMapper;
    }

    public Promotion fromProto(PromotionMessage request) {
        return Promotion.builder()
                .setId(request.getId())
                .setName(request.getName())
                .setDescription(request.getDescription())
                .setEffect(effectMapper.createFromProto(request.getEffect()))
                .addAllConditions(
                        request.getConditionsList().stream()
                                .map(conditionMapper::fromProto)
                                .collect(Collectors.toList()))
                .build();
    }

    public PromotionMessage toProto(Promotion promo) {
        return PromotionMessage.newBuilder()
                .setId(promo.getId())
                .setName(promo.getName())
                .setDescription(promo.getDescription())
                .setEffect(effectMapper.toProto(promo.getEffect()))
                .addAllConditions(promo.getConditions().stream()
                        .map(conditionMapper::toProto).toList())
                .build();
    }
}
