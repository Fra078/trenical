package it.trenical.promotion.models.conditions;

import it.trenical.promotion.models.Condition;
import it.trenical.promotion.models.ConditionVisitor;
import it.trenical.promotion.models.TravelContext;

public class FidelityCondition implements Condition {

    @Override
    public boolean canApply(TravelContext travelCtx) {
        return travelCtx.isFidelty();
    }

    @Override
    public <T> T accept(ConditionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
