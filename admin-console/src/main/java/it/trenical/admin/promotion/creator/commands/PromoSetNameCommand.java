package it.trenical.admin.promotion.creator.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.PromotionMessage;

public class PromoSetNameCommand extends Command {

    private final PromotionMessage.Builder builder;

    public PromoSetNameCommand(PromotionMessage.Builder builder) {
        super("name set", "Imposta il nome della promozione");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        String promotionId = args[0];
        builder.setName(promotionId);
    }

    @Override
    public String getSyntax() {
        return getName() + " <name>";
    }
}
