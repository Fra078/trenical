package it.trenical.promotion.models.conditions;

import it.trenical.promotion.models.Condition;
import it.trenical.promotion.models.TravelContext;

public class MinCountCondition implements Condition {

    private final int count;

    public MinCountCondition(int count) {
        this.count = count;
    }

    @Override
    public boolean canApply(TravelContext travelCtx) {
        return travelCtx.ticketCount() >= count;
    }
}
