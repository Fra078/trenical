package it.trenical.promotion.models.conditions;

import it.trenical.promotion.models.Condition;
import it.trenical.promotion.models.TravelContext;

public class PathCondition implements Condition {
    private final int pathId;
    public PathCondition(int pathId) {this.pathId = pathId;}

    @Override
    public boolean canApply(TravelContext travelCtx) {
        return travelCtx.pathId() == this.pathId;
    }
}
