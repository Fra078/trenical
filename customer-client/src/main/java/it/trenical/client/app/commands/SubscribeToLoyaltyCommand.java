package it.trenical.client.app.commands;

import it.trenical.common.proto.Empty;
import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.server.gateway.proto.TrenicalGatewayGrpc;

public class SubscribeToLoyaltyCommand extends Command {

    private final TrenicalGatewayGrpc.TrenicalGatewayBlockingStub stub;

    public SubscribeToLoyaltyCommand(TrenicalGatewayGrpc.TrenicalGatewayBlockingStub stub) {
        super("loyalty subscribe", "Subscribe to loyalty program");
        this.stub = stub;
    }

    @Override
    protected void action(String[] args) {
        stub.subscribeToLoyalty(Empty.getDefaultInstance());
    }
}
