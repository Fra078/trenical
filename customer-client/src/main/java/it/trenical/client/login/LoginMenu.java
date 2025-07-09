package it.trenical.client.login;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.client.app.AppMenu;
import it.trenical.client.login.commands.LoginCommand;
import it.trenical.client.login.commands.RegisterCommand;
import it.trenical.frontend.authentication.BearerToken;
import it.trenical.frontend.cli.Cli;
import it.trenical.server.gateway.proto.LoginServiceGrpc;
import it.trenical.server.gateway.proto.TrenicalGatewayGrpc;
import it.trenical.user.proto.LoginResponse;

public class LoginMenu extends Cli {

    private final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 6060).usePlaintext().build();
    private final LoginServiceGrpc.LoginServiceBlockingStub stub =
            LoginServiceGrpc.newBlockingStub(channel);

    public LoginMenu() {
        super("Trenical/Login");
        registerCommands(
                new LoginCommand(stub,this::onLoggedIn),
                new RegisterCommand(stub, this::onLoggedIn)
        );
    }

    private void onLoggedIn(LoginResponse response) {
        new AppMenu(TrenicalGatewayGrpc.newBlockingStub(channel)
                        .withCallCredentials(new BearerToken(response.getJwt()))
        ).start();
    }
}
