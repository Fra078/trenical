package it.trenical.promotion.repository.map;

import it.trenical.promotion.models.Promotion;
import it.trenical.promotion.repository.PromotionRepository;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class PromotionRepositoryMap implements PromotionRepository {
    private final Map<String, Promotion> map;

    public PromotionRepositoryMap(Map<String, Promotion> map) {
        this.map = map;
    }

    @Override
    public boolean save(Promotion promotion) {
        return map.putIfAbsent(promotion.getName(), promotion) == null;
    }

    @Override
    public void findAll(Consumer<Promotion> consumer) {
        map.values().forEach(consumer);
    }

    @Override
    public Optional<Promotion> findById(String id) {
        return Optional.ofNullable(map.get(id));
    }
}
