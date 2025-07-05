package it.trenical.promotion.models.conditions;

import it.trenical.promotion.models.Condition;
import it.trenical.promotion.models.TravelContext;

public class PeriodCondition implements Condition {

    private final long start, end;

    public PeriodCondition(long start, long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean canApply(TravelContext travelCtx) {
        return start <= travelCtx.date() && end >= travelCtx.date();
    }
}
