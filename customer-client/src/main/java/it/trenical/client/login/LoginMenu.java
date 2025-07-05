package it.trenical.client.login;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.client.login.commands.LoginCommand;
import it.trenical.client.login.commands.RegisterCommand;
import it.trenical.client.ticketry.TicketryMenu;
import it.trenical.frontend.cli.Cli;
import it.trenical.user.proto.LoginResponse;
import it.trenical.user.proto.UserServiceGrpc;

public class LoginMenu extends Cli {

    private final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 5060)
            .usePlaintext()
            .build();
    private final UserServiceGrpc.UserServiceBlockingStub stub =
            UserServiceGrpc.newBlockingStub(channel);

    public LoginMenu() {
        super("Trenical");
        registerCommands(
                new LoginCommand(stub,this::onLoggedIn),
                new RegisterCommand(stub, this::onLoggedIn)
        );
    }

    private void onLoggedIn(LoginResponse response) {
        System.out.println("Benvenuto in Trenical " + response.getUsername());
        new TicketryMenu(response.getJwt()).start();
    }

    public static void main(String[] args) {
        new LoginMenu().start();
    }
}
