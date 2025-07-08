package it.trenical.promotion.models.conditions;

import it.trenical.promotion.models.Condition;
import it.trenical.promotion.models.ConditionVisitor;
import it.trenical.promotion.models.TravelContext;

public class TrainTypeCondition implements Condition {

    private final String trainType;

    public TrainTypeCondition(String trainType) {
        this.trainType = trainType;
    }

    public String getTrainType() {
        return trainType;
    }

    @Override
    public boolean canApply(TravelContext travelCtx) {
        return travelCtx.getTrainType().equals(trainType);
    }

    @Override
    public <T> T accept(ConditionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
