package it.trenical.promotion.models.conditions;

import it.trenical.promotion.models.Condition;
import it.trenical.promotion.models.ConditionVisitor;
import it.trenical.promotion.models.TravelContext;

public class PathCondition implements Condition {
    private final int pathId;
    public PathCondition(int pathId) {this.pathId = pathId;}

    @Override
    public boolean canApply(TravelContext travelCtx) {
        return travelCtx.getPathId() == this.pathId;
    }

    public int getPathId() {
        return pathId;
    }

    @Override
    public <T> T accept(ConditionVisitor<T> visitor) {
        return visitor.visit(this);
    }
}
