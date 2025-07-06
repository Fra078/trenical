package it.trenical.promotion.models;

import it.trenical.promotion.models.conditions.*;

public interface ConditionVisitor<T> {
    T visit(DayOfWeekCondition condition);
    T visit(FidelityCondition condition);
    T visit(MinCountCondition condition);
    T visit(PathCondition condition);
    T visit(PeriodCondition condition);
    T visit(TrainTypeCondition condition);
}