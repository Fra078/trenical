package it.trenical.admin.promotion.creator.commands.effect;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.FixedAmountDiscountEffect;
import it.trenical.promotion.proto.FixedPriceDiscountEffect;
import it.trenical.promotion.proto.PromotionEffect;
import it.trenical.promotion.proto.PromotionMessage;

public class FixedPriceEffectCommand extends Command {

    private final PromotionMessage.Builder builder;

    public FixedPriceEffectCommand(PromotionMessage.Builder builder) {
        super("effect set fixed-price","");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        double price = Double.parseDouble(args[0]);
        if (price < 0)
            throw new BadCommandSyntaxException(getSyntax());
        builder.setEffect(
                PromotionEffect.newBuilder()
                    .setFixedPrice(
                            FixedPriceDiscountEffect.newBuilder()
                                    .setPrice(price)
                    )
        );
    }

    @Override
    public String getSyntax() {
        return getName() + " <price>";
    }
}
