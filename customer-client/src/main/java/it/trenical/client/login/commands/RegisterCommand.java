package it.trenical.client.login.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.user.proto.LoginResponse;
import it.trenical.user.proto.SignupRequest;
import it.trenical.user.proto.UserServiceGrpc;

import java.util.function.Consumer;

public class RegisterCommand extends Command {
    private final UserServiceGrpc.UserServiceBlockingStub stub;
    private final Consumer<LoginResponse> onLoggedIn;

    public RegisterCommand(UserServiceGrpc.UserServiceBlockingStub stub, Consumer<LoginResponse> onLoggedIn) {
        super("register", "Registra un nuovo utente");
        this.stub = stub;
        this.onLoggedIn = onLoggedIn;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 4)
            throw new BadCommandSyntaxException(getSyntax());
        SignupRequest.Builder builder = SignupRequest.newBuilder();
        builder.setUsername(args[0]);
        builder.setPassword(args[1]);
        builder.setFirstName(args[2]);
        builder.setLastName(args[3]);
        LoginResponse response = stub.register(builder.build());
        onLoggedIn.accept(response);
    }

    @Override
    public String getSyntax() {
        return getName() + " <username> <password> <firstname> <lastname>";
    }
}
