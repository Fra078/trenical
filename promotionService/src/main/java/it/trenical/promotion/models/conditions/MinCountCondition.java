package it.trenical.promotion.models.conditions;

import it.trenical.promotion.models.Condition;
import it.trenical.promotion.models.ConditionVisitor;
import it.trenical.promotion.models.TravelContext;

public class MinCountCondition implements Condition {

    private final int count;

    public MinCountCondition(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    @Override
    public <T> T accept(ConditionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean canApply(TravelContext travelCtx) {
        return travelCtx.getTicketCount() >= count;
    }
}
