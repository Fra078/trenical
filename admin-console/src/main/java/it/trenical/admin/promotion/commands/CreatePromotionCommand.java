package it.trenical.admin.promotion.commands;

import it.trenical.admin.promotion.creator.PromotionCreator;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.PromotionServiceGrpc;

public class CreatePromotionCommand extends Command {

    private final PromotionServiceGrpc.PromotionServiceBlockingStub stub;

    public CreatePromotionCommand(PromotionServiceGrpc.PromotionServiceBlockingStub stub) {
        super("promo create", "Crea una nuova promozione");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 1)
            throw new BadCommandSyntaxException(getSyntax());
        String promotionId = args[0];
        new PromotionCreator(stub, promotionId).start();
    }

    @Override
    public String getSyntax() {
        return getName() + " <id>";
    }
}
