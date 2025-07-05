package it.trenical.promotion.models;

import java.io.Serializable;

public interface Condition extends Serializable {

    boolean canApply(TravelContext travelCtx);

}
