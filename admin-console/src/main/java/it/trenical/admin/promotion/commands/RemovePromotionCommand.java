package it.trenical.admin.promotion.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.GetPromotionRequest;
import it.trenical.promotion.proto.PromotionMessage;
import it.trenical.promotion.proto.PromotionServiceGrpc;
import it.trenical.promotion.proto.RemovePromotionRequest;

public class RemovePromotionCommand extends Command {

    private final PromotionServiceGrpc.PromotionServiceBlockingStub stub;

    public RemovePromotionCommand(PromotionServiceGrpc.PromotionServiceBlockingStub stub) {
        super("promo rm", "Ottiene le informazioni su una promozione");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        String promotionId = args[0];
        stub.removePromotion(RemovePromotionRequest.newBuilder().setId(promotionId).build());
        System.out.println("Operazione completata!");
    }

    @Override
    public String getSyntax() {
        return getName() + " <id>";
    }
}
