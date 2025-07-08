package it.trenical.promotion.models.conditions;

import it.trenical.promotion.models.Condition;
import it.trenical.promotion.models.ConditionVisitor;
import it.trenical.promotion.models.TravelContext;

public class PeriodCondition implements Condition {

    private final long start, end;

    public PeriodCondition(long start, long end) {
        this.start = start;
        this.end = end;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    @Override
    public boolean canApply(TravelContext travelCtx) {
        return start <= travelCtx.getDate() && end >= travelCtx.getDate();
    }

    @Override
    public <T> T accept(ConditionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
