package it.trenical.client.app;

import it.trenical.client.app.commands.QueryTripCommand;
import it.trenical.frontend.cli.Cli;
import it.trenical.server.gateway.proto.TrenicalGatewayGrpc;

public class AppMenu extends Cli {

    public AppMenu(TrenicalGatewayGrpc.TrenicalGatewayBlockingStub gateway) {
        super("TrenicalCli");
        registerCommands(
                new QueryTripCommand(gateway)
        );
    }

}
