package it.trenical.client.app.commands;

import it.trenical.common.proto.Empty;
import it.trenical.frontend.cli.Command;
import it.trenical.promotion.proto.GetSubscriptionInfoResponse;
import it.trenical.server.gateway.proto.TrenicalGatewayGrpc;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class LoyaltyInfoCommand extends Command {

    private final TrenicalGatewayGrpc.TrenicalGatewayBlockingStub stub;

    public LoyaltyInfoCommand(TrenicalGatewayGrpc.TrenicalGatewayBlockingStub stub) {
        super("loyalty info", "Get info about your subscription to loyalty program");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) {
        GetSubscriptionInfoResponse response = stub.getLoyaltySubscriptionInfo(Empty.getDefaultInstance());
        if (response.getSubscribed()) {
            String date = Instant.ofEpochSecond(response.getFromDate()).atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            System.out.println("Iscritto al programma FedeltaTreno dal " + date);
        } else
            System.out.println("Non iscritto al programma FedeltaTreno");
    }
}
