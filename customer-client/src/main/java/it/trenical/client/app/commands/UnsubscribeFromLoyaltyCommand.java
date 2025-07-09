package it.trenical.client.app.commands;

import it.trenical.common.proto.Empty;
import it.trenical.frontend.cli.Command;
import it.trenical.server.gateway.proto.TrenicalGatewayGrpc;

public class UnsubscribeFromLoyaltyCommand extends Command {

    private final TrenicalGatewayGrpc.TrenicalGatewayBlockingStub stub;

    public UnsubscribeFromLoyaltyCommand(TrenicalGatewayGrpc.TrenicalGatewayBlockingStub stub) {
        super("loyalty unsubscribe", "Unsubscribe from loyalty program");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) {
        stub.unsubscribeToLoyalty(Empty.getDefaultInstance());
    }
}
