package it.trenical.promotion.managers;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import it.trenical.promotion.mappers.PromotionMapper;
import it.trenical.promotion.models.Promotion;
import it.trenical.promotion.proto.GetPromotionRequest;
import it.trenical.promotion.proto.PromotionMessage;
import it.trenical.promotion.repository.PromotionRepository;

import java.util.function.Consumer;

public class PromotionManager {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    public PromotionManager(PromotionRepository promotionRepository, PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    public void registerPromotion(PromotionMessage request) {
        Promotion promotion = promotionMapper.fromProto(request);
        boolean done = promotionRepository.save(promotion);
        if (!done)
            throw Status.ALREADY_EXISTS
                    .withDescription("Already exists a promotion with id "+ request.getId())
                    .asRuntimeException();
    }

    public void findAllPromotions(Consumer<PromotionMessage> consumer) {
        promotionRepository.findAll((promo) -> {
            consumer.accept(promotionMapper.toProto(promo));
        });
    }

    public PromotionMessage findById(GetPromotionRequest request) {
        Promotion promo =  promotionRepository.findById(request.getId()).orElseThrow(
                Status.NOT_FOUND::asRuntimeException);
        return promotionMapper.toProto(promo);
    }

}
