package it.trenical.admin.promotion.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.GetAllPromotionsRequest;
import it.trenical.promotion.proto.PromotionMessage;
import it.trenical.promotion.proto.PromotionServiceGrpc;

import java.util.Iterator;

public class GetAllPromotionsCommand extends Command {

    private final PromotionServiceGrpc.PromotionServiceBlockingStub stub;

    public GetAllPromotionsCommand(PromotionServiceGrpc.PromotionServiceBlockingStub stub) {
        super("promo list", "Lista tutte le promozioni disponibili");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        Iterator<PromotionMessage> it = stub.getAllPromotions(
                GetAllPromotionsRequest.newBuilder().setShowDisabled(true).build()
        );
        while (it.hasNext()) {
            PromotionMessage message = it.next();
            System.out.println(message);
        }
    }
}
