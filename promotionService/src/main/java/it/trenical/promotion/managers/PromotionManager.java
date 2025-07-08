package it.trenical.promotion.managers;

import io.grpc.Status;
import it.trenical.promotion.mappers.PromotionMapper;
import it.trenical.promotion.models.Condition;
import it.trenical.promotion.models.Promotion;
import it.trenical.promotion.models.TravelContext;
import it.trenical.promotion.proto.GetPromotionRequest;
import it.trenical.promotion.proto.PromotionMessage;
import it.trenical.promotion.repository.FidelityProgramRepository;
import it.trenical.promotion.repository.PromotionRepository;
import it.trenical.travel.proto.TravelSolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PromotionManager {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;
    private final FidelityProgramRepository fidelityProgramRepository;

    public PromotionManager(PromotionRepository promotionRepository, PromotionMapper promotionMapper, FidelityProgramRepository fidelityProgramRepository) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
        this.fidelityProgramRepository = fidelityProgramRepository;
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

    public TravelSolution applyPromotion(TravelSolution msg){
        TravelContext.Builder builder = TravelContext.builder()
                .setPathId(msg.getRouteInfo().getPathId())
                .setDate(msg.getRouteInfo().getDepartureTime())
                .setTrainType(msg.getType().getName())
                .setIsFidelty(fidelityProgramRepository.isFidelityUser(msg.getUserId()))
                .setTicketCount(msg.getTicketCount());


        List<TravelContext> contexts = new ArrayList<>();
        Map<String, TravelSolution.Mode> classModes = new HashMap<>();
        msg.getModesList().forEach(mode -> {
            classModes.put(mode.getServiceClass().getName(), mode);
            builder.setServiceClass(mode.getServiceClass().getName());
            builder.setBasePrice(mode.getPrice());
            contexts.add(builder.build());
            contexts.add(builder.build());
        });
        promotionRepository.findAll((promo) -> {
            t: for (TravelContext context : contexts) {
                for (Condition c: promo.getConditions())
                    if (!c.canApply(context)){
                        System.err.println("Can't apply condition " + c + " for context " + context);
                        continue t;
                    }
                double price = promo.getEffect().calculatePrice(context.getBasePrice(), context.getTicketCount());
                TravelSolution.Mode currentMode = classModes.get(context.getServiceClass());
                double currentPrice = currentMode.hasPromo() ? currentMode.getPromo().getFinalPrice() : context.getBasePrice();

                if (price < currentPrice){
                    TravelSolution.Mode newMode = TravelSolution.Mode.newBuilder(currentMode)
                            .setPromo(TravelSolution.AppliedPromo.newBuilder()
                                    .setId(promo.getId())
                                    .setName(promo.getName())
                                    .setFinalPrice(price)
                                    .build()).build();
                    System.out.println("Applicata la promo " + newMode);
                    classModes.put(context.getServiceClass(), newMode);
                }
            }
        });
        return TravelSolution.newBuilder(msg).clearModes().addAllModes(classModes.values()).build();
    }

}
