package it.trenical.admin.promotion.creator.commands.conditions;

import it.trenical.admin.promotion.creator.commands.Utils;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.PromotionCondition;
import it.trenical.promotion.proto.PromotionMessage;
import it.trenical.promotion.proto.TravelPathCondition;

public class PathConditionCommand extends Command {

    private final PromotionMessage.Builder builder;

    public PathConditionCommand(PromotionMessage.Builder builder) {
        super("condition add path", "Limita la promo ad una tratta");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        int pathId = Integer.parseInt(args[0]);
        Utils.removeConditionFromBuilder(builder, PromotionCondition.ConditionTypeCase.PATH);
        builder.addConditions(
                PromotionCondition.newBuilder()
                        .setPath(
                                TravelPathCondition.newBuilder()
                                        .setPathId(pathId).build()
                        )
        );
    }

    @Override
    public String getSyntax() {
        return getName() + " <pathId>";
    }
}
