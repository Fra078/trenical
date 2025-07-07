package it.trenical.admin.promotion.creator.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.PromotionMessage;

public class PromoSetDescriptionCommand extends Command {

    private final PromotionMessage.Builder builder;

    public PromoSetDescriptionCommand(PromotionMessage.Builder builder) {
        super("description set", "Imposta la descrizione della promozione");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        builder.setDescription(args[0]);
    }

    @Override
    public String getSyntax() {
        return getName() + " <description>";
    }
}
