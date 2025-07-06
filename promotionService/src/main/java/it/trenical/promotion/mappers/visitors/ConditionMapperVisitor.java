package it.trenical.promotion.mappers.visitors;

import it.trenical.promotion.models.ConditionVisitor;
import it.trenical.promotion.models.conditions.*;
import it.trenical.promotion.models.conditions.DayOfWeekCondition;
import it.trenical.promotion.models.conditions.TrainTypeCondition;
import it.trenical.promotion.proto.*;

import java.util.stream.Collectors;

public class ConditionMapperVisitor implements ConditionVisitor<PromotionCondition> {
    @Override
    public PromotionCondition visit(DayOfWeekCondition condition) {
        var protoDays = condition.getAcceptableDays().stream()
                .map(javaDay -> DayOfWeek.valueOf(javaDay.name()))
                .collect(Collectors.toList());
        var dayOfWeekProto = it.trenical.promotion.proto.DayOfWeekCondition.newBuilder().addAllDay(protoDays).build();
        return PromotionCondition.newBuilder().setDayOfWeek(dayOfWeekProto).build();
    }

    @Override
    public PromotionCondition visit(FidelityCondition condition) {
        var fidelityProto = FidelityCustomerCondition.newBuilder().build();
        return PromotionCondition.newBuilder().setFidelity(fidelityProto).build();
    }

    @Override
    public PromotionCondition visit(MinCountCondition condition) {
        var minTicketsProto = MinTicketsCondition.newBuilder().setQuantity(condition.getCount()).build();
        return PromotionCondition.newBuilder().setMinTickets(minTicketsProto).build();
    }

    @Override
    public PromotionCondition visit(PathCondition condition) {
        var pathProto = TravelPathCondition.newBuilder().setPathId(condition.getPathId()).build();
        return PromotionCondition.newBuilder().setPath(pathProto).build();
    }

    @Override
    public PromotionCondition visit(PeriodCondition condition) {
        var periodProto = TravelPeriodCondition.newBuilder()
                .setStartDate(condition.getStart())
                .setEndDate(condition.getEnd())
                .build();
        return PromotionCondition.newBuilder().setPeriod(periodProto).build();
    }

    @Override
    public PromotionCondition visit(TrainTypeCondition condition) {
        var trainProto = it.trenical.promotion.proto.TrainTypeCondition.newBuilder()
                .setTypeName(condition.getTrainType())
                .build();
        return PromotionCondition.newBuilder().setTrainType(trainProto).build();
    }

}
