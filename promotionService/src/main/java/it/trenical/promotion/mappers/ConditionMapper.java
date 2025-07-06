package it.trenical.promotion.mappers;

import it.trenical.promotion.mappers.visitors.ConditionMapperVisitor;
import it.trenical.promotion.models.Condition;
import it.trenical.promotion.models.conditions.*;
import it.trenical.promotion.proto.PromotionCondition;
import it.trenical.promotion.proto.TravelPeriodCondition;

import java.util.stream.Collectors;

public class ConditionMapper {

    private final ConditionMapperVisitor visitor;

    public ConditionMapper(ConditionMapperVisitor visitor) {
        this.visitor = visitor;
    }

    public Condition fromProto(PromotionCondition proto) {
        return switch (proto.getConditionTypeCase()) {
            case PATH -> new PathCondition(proto.getPath().getPathId());
            case PERIOD -> {
                TravelPeriodCondition periodProto = proto.getPeriod();
                yield new PeriodCondition(periodProto.getStartDate(), periodProto.getEndDate());
            }
            case FIDELITY -> new FidelityCondition();
            case TRAIN_TYPE -> new TrainTypeCondition(proto.getTrainType().getTypeName());
            case DAY_OF_WEEK -> {
                var days = proto.getDayOfWeek().getDayList().stream()
                        .map(protoDay -> java.time.DayOfWeek.valueOf(protoDay.name()))
                        .collect(Collectors.toSet());
                yield new DayOfWeekCondition(days);
            }
            case MIN_TICKETS -> new MinCountCondition(proto.getMinTickets().getQuantity());
            default ->
                    throw new IllegalArgumentException("Tipo di condizione non supportato: " + proto.getConditionTypeCase());
        };
    }

    public PromotionCondition toProto(Condition condition) {
        return condition.accept(visitor);
    }
}
