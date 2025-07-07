package it.trenical.admin.promotion.creator.commands.conditions;

import it.trenical.admin.promotion.creator.commands.Utils;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.MinTicketsCondition;
import it.trenical.promotion.proto.PromotionCondition;
import it.trenical.promotion.proto.PromotionMessage;

public class MinTicketConditionCommand extends Command {
    private final PromotionMessage.Builder builder;

    public MinTicketConditionCommand(PromotionMessage.Builder builder) {
        super("condition add min-tickets", "Limita la promo ad acquisti di un certo numero di biglietti");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        int min = Integer.parseInt(args[0]);

        Utils.removeConditionFromBuilder(builder, PromotionCondition.ConditionTypeCase.MIN_TICKETS);

        builder.addConditions(
                PromotionCondition.newBuilder().setMinTickets(
                        MinTicketsCondition.newBuilder().setQuantity(min).build()
                )
        );
    }
}
