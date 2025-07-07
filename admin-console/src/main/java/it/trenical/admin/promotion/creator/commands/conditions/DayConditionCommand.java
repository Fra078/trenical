package it.trenical.admin.promotion.creator.commands.conditions;

import it.trenical.admin.promotion.creator.commands.Utils;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.DayOfWeek;
import it.trenical.promotion.proto.DayOfWeekCondition;
import it.trenical.promotion.proto.PromotionCondition;
import it.trenical.promotion.proto.PromotionMessage;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class DayConditionCommand extends Command {
    private final PromotionMessage.Builder builder;

    public DayConditionCommand(PromotionMessage.Builder builder) {
        super("condition add days", "Limita la promo a giorni della settimana");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length < 1)
            throw new BadCommandSyntaxException(getSyntax());
        Set<DayOfWeek> days = Arrays.stream(args)
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toSet());

        Utils.removeConditionFromBuilder(builder, PromotionCondition.ConditionTypeCase.DAY_OF_WEEK);

        builder.addConditions(
                PromotionCondition.newBuilder().setDayOfWeek(
                        DayOfWeekCondition.newBuilder().addAllDay(days).build()
                ).build()
        );
    }

    @Override
    public String getSyntax() {
        return getName() + " MONDAY TUESDAY ... SUNDAY";
    }
}
