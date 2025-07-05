package it.trenical.promotion.models.conditions;

import it.trenical.promotion.models.Condition;
import it.trenical.promotion.models.TravelContext;

public class TrainTypeCondition implements Condition {

    private final String trainType;

    public TrainTypeCondition(String trainType) {
        this.trainType = trainType;
    }

    @Override
    public boolean canApply(TravelContext travelCtx) {
        return travelCtx.trainType().equals(trainType);
    }
}
