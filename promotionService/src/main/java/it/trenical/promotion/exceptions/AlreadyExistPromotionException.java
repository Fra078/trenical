package it.trenical.promotion.exceptions;

import it.trenical.promotion.models.Promotion;

public class AlreadyExistPromotionException extends RuntimeException {
    public AlreadyExistPromotionException(String promoId) {
        super("Promotion with id " + promoId + " already exists!");
    }
}
