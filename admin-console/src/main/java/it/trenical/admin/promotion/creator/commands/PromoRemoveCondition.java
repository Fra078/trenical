package it.trenical.admin.promotion.creator.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.PromotionCondition;
import it.trenical.promotion.proto.PromotionMessage;

public class PromoRemoveCondition extends Command {
    private final PromotionMessage.Builder builder;
    public PromoRemoveCondition(PromotionMessage.Builder builder) {
        super("condition rm", "Rimuove una condizione");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1) {
            throw new BadCommandSyntaxException(getSyntax());
        }
        String input = args[0];
        PromotionCondition.ConditionTypeCase enumCase = switch (input){
            case "days"->PromotionCondition.ConditionTypeCase.DAY_OF_WEEK;
            case "fidelity"->PromotionCondition.ConditionTypeCase.FIDELITY;
            case "min-tickets" ->PromotionCondition.ConditionTypeCase.MIN_TICKETS;
            case "path" ->PromotionCondition.ConditionTypeCase.PATH;
            case "period" ->PromotionCondition.ConditionTypeCase.PERIOD;
            case "train-type" ->PromotionCondition.ConditionTypeCase.TRAIN_TYPE;
            default -> throw new BadCommandSyntaxException(getSyntax());
        };

        Utils.removeConditionFromBuilder(builder, enumCase);
    }

    @Override
    public String getSyntax() {
        return getName() + " <conditionName>";
    }
}
