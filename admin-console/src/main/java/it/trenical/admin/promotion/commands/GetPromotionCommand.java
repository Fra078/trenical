package it.trenical.admin.promotion.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.GetPromotionRequest;
import it.trenical.promotion.proto.PromotionMessage;
import it.trenical.promotion.proto.PromotionServiceGrpc;

public class GetPromotionCommand extends Command {

    private final PromotionServiceGrpc.PromotionServiceBlockingStub stub;

    public GetPromotionCommand(PromotionServiceGrpc.PromotionServiceBlockingStub stub) {
        super("promo", "Ottiene le informazioni su una promozione");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        String promotionId = args[0];
        PromotionMessage promo = stub.getPromotionById(GetPromotionRequest.newBuilder().setId(promotionId).build());
        System.out.println(promo);
    }

    @Override
    public String getSyntax() {
        return getName() + " <id>";
    }
}
