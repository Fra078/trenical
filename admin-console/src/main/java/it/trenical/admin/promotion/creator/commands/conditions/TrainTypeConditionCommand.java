package it.trenical.admin.promotion.creator.commands.conditions;

import it.trenical.admin.promotion.creator.commands.Utils;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.PromotionCondition;
import it.trenical.promotion.proto.PromotionMessage;
import it.trenical.promotion.proto.TrainTypeCondition;
import it.trenical.promotion.proto.TravelPathCondition;

public class TrainTypeConditionCommand extends Command {

    private final PromotionMessage.Builder builder;

    public TrainTypeConditionCommand(PromotionMessage.Builder builder) {
        super("condition add train-type", "Limita la promo ad una tipologia di treno");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        String trainType = args[0];
        Utils.removeConditionFromBuilder(builder, PromotionCondition.ConditionTypeCase.TRAIN_TYPE);

        builder.addConditions(
                PromotionCondition.newBuilder()
                        .setTrainType(
                                TrainTypeCondition.newBuilder()
                                        .setTypeName(trainType).build()
                        )
        );
    }

    @Override
    public String getSyntax() {
        return getName() + " <typeName>";
    }
}
