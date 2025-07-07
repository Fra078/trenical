package it.trenical.admin.promotion.creator.commands;

import it.trenical.promotion.proto.PromotionCondition;
import it.trenical.promotion.proto.PromotionMessage;

import java.util.List;

public final class Utils {
    private Utils() {
        throw new IllegalStateException("Utility class");
    }

    public static void removeConditionFromBuilder(
            PromotionMessage.Builder builder,
            PromotionCondition.ConditionTypeCase type
    ) {
        List<PromotionCondition> list = builder.getConditionsList();
        for (int i = 0; i < list.size(); i++) {
            PromotionCondition condition = list.get(i);
            if (condition.getConditionTypeCase().equals(type)) {
                builder.removeConditions(i);
                break;
            }
        }
    }
}
