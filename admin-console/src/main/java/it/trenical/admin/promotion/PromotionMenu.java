package it.trenical.admin.promotion;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.admin.promotion.commands.*;
import it.trenical.frontend.cli.Cli;
import it.trenical.promotion.proto.PromotionServiceGrpc;

public class PromotionMenu extends Cli {

    private final ManagedChannel channel =
            ManagedChannelBuilder.forAddress("localhost", 5606)
                    .usePlaintext().build();

    private final PromotionServiceGrpc.PromotionServiceBlockingStub stub =
            PromotionServiceGrpc.newBlockingStub(channel);

    public PromotionMenu() {
        super("PromotionMenu");
        registerCommands(
                new GetPromotionCommand(stub),
                new GetAllPromotionsCommand(stub),
                new CreatePromotionCommand(stub),
                new UpdatePromotionCommand(stub),
                new RemovePromotionCommand(stub),
                new CopyPromotionCommand(stub)
        );
    }

    public static void main(String[] args) {
        new PromotionMenu().start();
    }

}
