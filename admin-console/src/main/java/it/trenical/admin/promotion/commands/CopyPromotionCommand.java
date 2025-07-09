package it.trenical.admin.promotion.commands;

import it.trenical.admin.promotion.creator.PromotionCreator;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.GetPromotionRequest;
import it.trenical.promotion.proto.PromotionMessage;
import it.trenical.promotion.proto.PromotionServiceGrpc;

public class CopyPromotionCommand extends Command {

    private final PromotionServiceGrpc.PromotionServiceBlockingStub stub;

    public CopyPromotionCommand(PromotionServiceGrpc.PromotionServiceBlockingStub stub) {
        super("promo copy", "Crea una nuova promozione");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 2)
            throw new BadCommandSyntaxException(getSyntax());
        String sourceId = args[0];
        String newId = args[1];
        PromotionMessage src = stub.getPromotionById(GetPromotionRequest.newBuilder().setId(sourceId).build());
        PromotionMessage.Builder builder = PromotionMessage.newBuilder(src).setId(newId);
        new PromotionCreator(stub, builder, false).start();
    }

    @Override
    public String getSyntax() {
        return getName() + " <sourceId> <newId>";
    }
}
