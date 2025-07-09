package it.trenical.promotion.repository;

import it.trenical.promotion.models.Promotion;

import java.util.Optional;
import java.util.function.Consumer;

public interface PromotionRepository {
    boolean save(Promotion promotion);
    boolean update(Promotion promotion);
    void findAll(Consumer<Promotion> consumer);
    Optional<Promotion> findById(String id);

    boolean deleteById(String id);
}
