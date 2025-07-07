package it.trenical.admin.promotion.creator.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.PromotionMessage;

public class PromoGetStatusCommand extends Command {

    private final PromotionMessage.Builder builder;

    public PromoGetStatusCommand(PromotionMessage.Builder builder) {
        super("status", "Mostra lo stato attuale");
        this.builder = builder;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        System.out.println(builder.build());
    }
}
