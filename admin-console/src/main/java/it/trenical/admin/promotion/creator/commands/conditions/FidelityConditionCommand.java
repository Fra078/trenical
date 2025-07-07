package it.trenical.admin.promotion.creator.commands.conditions;

import it.trenical.admin.promotion.creator.commands.Utils;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.FidelityCustomerCondition;
import it.trenical.promotion.proto.PromotionCondition;
import it.trenical.promotion.proto.PromotionMessage;
import it.trenical.promotion.proto.TravelPathCondition;

public class FidelityConditionCommand extends Command {

    private final PromotionMessage.Builder builder;

    public FidelityConditionCommand(PromotionMessage.Builder builder) {
        super("condition add fidelity", "Limita la promo ai clienti fedeltà");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        Utils.removeConditionFromBuilder(builder, PromotionCondition.ConditionTypeCase.FIDELITY);
        builder.addConditions(
                PromotionCondition.newBuilder()
                        .setFidelity(FidelityCustomerCondition.getDefaultInstance())
        );
    }
}
