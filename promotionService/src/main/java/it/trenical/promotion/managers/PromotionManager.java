package it.trenical.promotion.managers;

import it.trenical.promotion.exceptions.AlreadyExistPromotionException;
import it.trenical.promotion.models.Condition;
import it.trenical.promotion.models.Promotion;
import it.trenical.promotion.models.TravelContext;
import it.trenical.promotion.repository.LoyaltyRepository;
import it.trenical.promotion.repository.PromotionRepository;
import it.trenical.travel.proto.TravelSolution;

import java.util.*;
import java.util.function.Consumer;

public class PromotionManager {

    private final PromotionRepository promotionRepository;
    private final LoyaltyRepository loyaltyRepository;

    public PromotionManager(
            PromotionRepository promotionRepository,
            LoyaltyRepository loyaltyRepository
    ) {
        this.promotionRepository = promotionRepository;
        this.loyaltyRepository = loyaltyRepository;
    }

    public void registerPromotion(Promotion promotion) {
        boolean done = promotionRepository.save(promotion);
        if (!done)
            throw new AlreadyExistPromotionException(promotion.getId());
    }

    public void updatePromotion(Promotion promotion) {
        boolean done = promotionRepository.update(promotion);
        if (!done)
            throw new NoSuchElementException("Not exists a promotion with id " + promotion.getId());

    }

    public void removePromotion(String id) {
        boolean done = promotionRepository.deleteById(id);
        if (!done)
            throw new NoSuchElementException("Not exists a promotion with id " + id);
    }

    public void findAllPromotions(Consumer<Promotion> consumer) {
        promotionRepository.findAll(consumer);
    }

    public Promotion findById(String promoId) {
        return promotionRepository.findById(promoId).orElseThrow(
                () -> new NoSuchElementException("Promo with id " + promoId + " not found!")
        );
    }

    public TravelSolution applyPromotion(TravelSolution msg){
        TravelContext.Builder builder = TravelContext.builder()
                .setPathId(msg.getRouteInfo().getPathId())
                .setDate(msg.getRouteInfo().getDepartureTime())
                .setTrainType(msg.getType().getName())
                .setIsFidelty(loyaltyRepository.isFidelityUser(msg.getUserId()))
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
