package it.trenical.client.app.commands;

import it.trenical.common.proto.Empty;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.promotion.proto.PromotionMessage;
import it.trenical.server.gateway.proto.TrenicalGatewayGrpc;

import java.util.Iterator;
import java.util.Scanner;

public class ListenToPromotionsCommand extends Command {

    private TrenicalGatewayGrpc.TrenicalGatewayBlockingStub stub;

    public ListenToPromotionsCommand(TrenicalGatewayGrpc.TrenicalGatewayBlockingStub stub) {
        super("promo observe", "Follow updates about promotions");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        Thread thread = new Thread(() -> {
            try {
                Iterator<PromotionMessage> it = stub.listenToLoyaltyPromotions(Empty.getDefaultInstance());
                while (it.hasNext()) {
                    System.out.println("PROMO: {");
                    System.out.println(it.next());
                    System.out.println("}");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();

        new Scanner(System.in).nextLine();
        thread.interrupt();
        System.out.println();
    }
}
