package it.trenical.admin.promotion.creator.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.PromotionMessage;
import it.trenical.promotion.proto.PromotionServiceGrpc;

public class PromoConfirmCommand extends Command {

    private final PromotionMessage.Builder builder;
    private final PromotionServiceGrpc.PromotionServiceBlockingStub stub;
    private final Runnable exitAction;

    public PromoConfirmCommand(
            PromotionServiceGrpc.PromotionServiceBlockingStub stub,
            PromotionMessage.Builder builder,
            Runnable exitAction
    ) {
        super("confirm", "Conferma i dati della promo");
        this.builder = builder;
        this.stub = stub;
        this.exitAction = exitAction;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        stub.registerPromotion(builder.build());
        System.out.println("Operazione completata!");
        exitAction.run();
    }
}
