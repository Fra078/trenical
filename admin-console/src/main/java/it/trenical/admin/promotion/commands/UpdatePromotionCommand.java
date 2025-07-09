package it.trenical.admin.promotion.commands;

import it.trenical.admin.promotion.creator.PromotionCreator;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.GetPromotionRequest;
import it.trenical.promotion.proto.PromotionMessage;
import it.trenical.promotion.proto.PromotionServiceGrpc;

public class UpdatePromotionCommand extends Command {

    private final PromotionServiceGrpc.PromotionServiceBlockingStub stub;

    public UpdatePromotionCommand(PromotionServiceGrpc.PromotionServiceBlockingStub stub) {
        super("promo update", "Crea una nuova promozione");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        String sourceId = args[0];
        PromotionMessage src = stub.getPromotionById(GetPromotionRequest.newBuilder().setId(sourceId).build());
        PromotionMessage.Builder builder = PromotionMessage.newBuilder(src);
        new PromotionCreator(stub, builder, true).start();
    }

    @Override
    public String getSyntax() {
        return getName() + " <id>";
    }
}
