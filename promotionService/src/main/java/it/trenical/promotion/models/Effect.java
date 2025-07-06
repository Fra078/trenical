package it.trenical.promotion.models;

import java.io.Serializable;

public interface Effect extends Serializable {

    double calculatePrice(double basePrice, int count);

    <T> T accept(EffectVisitor<T> visitor);
}
