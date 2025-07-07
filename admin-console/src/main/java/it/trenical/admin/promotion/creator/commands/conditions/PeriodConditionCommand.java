package it.trenical.admin.promotion.creator.commands.conditions;

import it.trenical.admin.promotion.creator.commands.Utils;
import it.trenical.common.proto.Common;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.PromotionCondition;
import it.trenical.promotion.proto.PromotionMessage;
import it.trenical.promotion.proto.TravelPathCondition;
import it.trenical.promotion.proto.TravelPeriodCondition;
import it.trenical.ticketry.proto.TripQueryParams;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class PeriodConditionCommand extends Command {

    private final PromotionMessage.Builder builder;

    public PeriodConditionCommand(PromotionMessage.Builder builder) {
        super("condition add period", "Limita la promo ad un periodo temporale");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 2)
            throw new BadCommandSyntaxException(getSyntax());
        long start = toEpochSeconds(args[0]);
        long end = toEpochSeconds(args[1]);
        Utils.removeConditionFromBuilder(builder, PromotionCondition.ConditionTypeCase.PERIOD);

        builder.addConditions(
                PromotionCondition.newBuilder()
                        .setPeriod(
                                TravelPeriodCondition.newBuilder()
                                        .setStartDate(start)
                                        .setEndDate(end)
                                        .build()
                        )
        );
    }

    @Override
    public String getSyntax() {
        return getName() + " <from: gg/mm/aaaa> <to: gg/mm/aaaa>";
    }

    private long toEpochSeconds(String dateStr) {
        return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                .atStartOfDay(ZoneId.systemDefault())
                .toEpochSecond();
    }

}
