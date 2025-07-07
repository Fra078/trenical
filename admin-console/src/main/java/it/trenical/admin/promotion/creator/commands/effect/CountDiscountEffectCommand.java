package it.trenical.admin.promotion.creator.commands.effect;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.CountTicketDiscountEffect;
import it.trenical.promotion.proto.PercentageDiscountEffect;
import it.trenical.promotion.proto.PromotionEffect;
import it.trenical.promotion.proto.PromotionMessage;

public class CountDiscountEffectCommand extends Command {
    private final PromotionMessage.Builder builder;

    public CountDiscountEffectCommand(PromotionMessage.Builder builder) {
        super("effect set count-discount","");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 2)
            throw new BadCommandSyntaxException(getSyntax());
        int count = Integer.parseInt(args[0]);
        if (count <= 0)
            throw new BadCommandSyntaxException(getSyntax());
        int percentage = Integer.parseInt(args[1]);
        if (percentage < 0 || percentage > 100)
            throw new BadCommandSyntaxException(getSyntax());
        builder.setEffect(
                PromotionEffect.newBuilder()
                        .setCountTicketDiscount(
                                CountTicketDiscountEffect.newBuilder()
                                        .setCount(count)
                                        .setDiscountPercentage(percentage)
                        )
        );
    }

    @Override
    public String getSyntax() {
        return getName() + " <count> <value [0;100]>";
    }

}
