package it.trenical.promotion.test;

import it.trenical.promotion.exceptions.AlreadyExistPromotionException;
import it.trenical.promotion.managers.PromotionManager;
import it.trenical.promotion.models.Promotion;
import it.trenical.promotion.models.effects.PercentageEffect;
import it.trenical.promotion.repository.LoyaltyRepository;
import it.trenical.promotion.repository.PromotionRepository;
import it.trenical.promotion.repository.map.LoyaltyMapRepository;
import it.trenical.promotion.repository.map.PromotionRepositoryMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class PromotionManagerTest {

    private PromotionManager promotionManager;
    private PromotionRepository promotionRepository;
    private LoyaltyRepository loyaltyRepository;

    @BeforeEach
    void setUp() {
        promotionRepository = new PromotionRepositoryMap(new HashMap<>());
        loyaltyRepository = new LoyaltyMapRepository(new HashMap<>());
        promotionManager = new PromotionManager(promotionRepository, loyaltyRepository);
    }

    @Test
    void testRegisterPromotion() {
        Promotion promotion = Promotion.builder()
                .setId("PROMO1")
                .setName("Sconto 10%")
                .setEffect(new PercentageEffect(10))
                .build();
        promotionManager.registerPromotion(promotion);
        assertNotNull(promotionRepository.findById("PROMO1").orElse(null));
    }

    @Test
    void testRegisterExistingPromotion() {
        Promotion promotion = Promotion.builder()
                .setId("PROMO1")
                .setName("Sconto 10%")
                .setEffect(new PercentageEffect(10))
                .build();
        promotionManager.registerPromotion(promotion);
        assertThrows(AlreadyExistPromotionException.class, () -> promotionManager.registerPromotion(promotion));
    }

    @Test
    void testUpdatePromotion() {
        Promotion promotion = Promotion.builder()
                .setId("PROMO1")
                .setName("Sconto 10%")
                .setEffect(new PercentageEffect(10))
                .build();
        promotionRepository.save(promotion);

        Promotion updatedPromotion = Promotion.builder(promotion)
                .setName("Sconto 20%")
                .setEffect(new PercentageEffect(20))
                .build();
        promotionManager.updatePromotion(updatedPromotion);

        Promotion fetched = promotionRepository.findById("PROMO1").get();
        assertEquals("Sconto 20%", fetched.getName());
    }

    @Test
    void testUpdateNonExistingPromotion() {
        Promotion promotion = Promotion.builder()
                .setId("PROMO1")
                .setName("Sconto 10%")
                .setEffect(new PercentageEffect(10))
                .build();
        assertThrows(NoSuchElementException.class, () -> promotionManager.updatePromotion(promotion));
    }

    @Test
    void testRemovePromotion() {
        Promotion promotion = Promotion.builder()
                .setId("PROMO1")
                .setName("Sconto 10%")
                .setEffect(new PercentageEffect(10))
                .build();
        promotionRepository.save(promotion);
        promotionManager.removePromotion("PROMO1");
        assertTrue(promotionRepository.findById("PROMO1").isEmpty());
    }

    @Test
    void testRemoveNonExistingPromotion() {
        assertThrows(NoSuchElementException.class, () -> promotionManager.removePromotion("PROMO1"));
    }
}