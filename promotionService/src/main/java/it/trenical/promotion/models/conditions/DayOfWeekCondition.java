package it.trenical.promotion.models.conditions;

import it.trenical.promotion.models.Condition;
import it.trenical.promotion.models.ConditionVisitor;
import it.trenical.promotion.models.TravelContext;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Set;

public class DayOfWeekCondition implements Condition {

    private final Set<DayOfWeek> acceptableDays;

    public DayOfWeekCondition(Set<DayOfWeek> acceptableDays) {
        this.acceptableDays = Set.copyOf(acceptableDays);
    }

    public Set<DayOfWeek> getAcceptableDays() {
        return acceptableDays;
    }

    @Override
    public <T> T accept(ConditionVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean canApply(TravelContext travelCtx) {
        DayOfWeek day = Instant.ofEpochSecond(travelCtx.date())
                .atZone(ZoneId.systemDefault()).getDayOfWeek();
        return acceptableDays.contains(day);
    }
}
