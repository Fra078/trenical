package it.trenical.promotion.factory;

import it.trenical.promotion.models.Condition;
import it.trenical.promotion.models.conditions.*;
import it.trenical.promotion.proto.PromotionCondition;

import java.time.DayOfWeek;
import java.util.stream.Collectors;


public final class ConditionFactory {

    private ConditionFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static Condition buildFromDto(PromotionCondition dto) {
        return switch (dto.getConditionTypeCase()){
            case PATH -> new PathCondition(dto.getPath().getPathId());
            case PERIOD -> new PeriodCondition(dto.getPeriod().getStartDate(), dto.getPeriod().getEndDate());
            case FIDELITY -> new FidelityCondition();
            case TRAIN_TYPE -> new TrainTypeCondition(dto.getTrainType().getTypeName());
            case DAY_OF_WEEK -> new DayOfWeekCondition(
                    dto.getDayOfWeek().getDayList().stream()
                            .map(Enum::name)
                            .map(DayOfWeek::valueOf)
                            .collect(Collectors.toUnmodifiableSet())
            );
            case MIN_TICKETS -> new MinCountCondition(dto.getMinTickets().getQuantity());
            case CONDITIONTYPE_NOT_SET -> null;
        };
    }

}
