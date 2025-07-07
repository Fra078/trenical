package it.trenical.admin.promotion.creator.commands.effect;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.PercentageDiscountEffect;
import it.trenical.promotion.proto.PromotionEffect;
import it.trenical.promotion.proto.PromotionMessage;
import it.trenical.promotion.proto.PromotionServiceGrpc;

public class PercentageEffectCommand extends Command {

    private final PromotionMessage.Builder builder;

    public PercentageEffectCommand(PromotionMessage.Builder builder) {
        super("effect set percentage","");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        int percentage = Integer.parseInt(args[0]);
        if (percentage < 0 || percentage > 100)
            throw new BadCommandSyntaxException(getSyntax());
        builder.setEffect(
                PromotionEffect.newBuilder()
                    .setPercentageDiscount(
                            PercentageDiscountEffect.newBuilder()
                                    .setPercentage(percentage)
                    )
        );
    }

    @Override
    public String getSyntax() {
        return getName() + " <value [0;100]>";
    }
}
