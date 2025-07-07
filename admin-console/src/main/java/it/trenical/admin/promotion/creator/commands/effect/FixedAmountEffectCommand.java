package it.trenical.admin.promotion.creator.commands.effect;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.FixedAmountDiscountEffect;
import it.trenical.promotion.proto.PercentageDiscountEffect;
import it.trenical.promotion.proto.PromotionEffect;
import it.trenical.promotion.proto.PromotionMessage;

public class FixedAmountEffectCommand extends Command {

    private final PromotionMessage.Builder builder;

    public FixedAmountEffectCommand(PromotionMessage.Builder builder) {
        super("effect set fixed-amount","");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        double amount = Double.parseDouble(args[0]);
        if (amount < 0)
            throw new BadCommandSyntaxException(getSyntax());
        builder.setEffect(
                PromotionEffect.newBuilder()
                    .setFixedAmountDiscount(
                            FixedAmountDiscountEffect.newBuilder()
                                    .setAmount(amount)
                    )
        );
    }

    @Override
    public String getSyntax() {
        return getName() + " <value>";
    }
}
