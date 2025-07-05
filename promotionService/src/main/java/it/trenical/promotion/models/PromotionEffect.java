package it.trenical.promotion.models;

import java.io.Serializable;

public interface PromotionEffect extends Serializable {

    double calculatePrice(double basePrice, int count);

}
