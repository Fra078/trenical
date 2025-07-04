package it.trenical.client.login.commands;

import it.trenical.frontend.cli.Command;
import it.trenical.frontend.cli.exceptions.BadCommandSyntaxException;
import it.trenical.user.proto.LoginResponse;
import it.trenical.user.proto.SigninRequest;
import it.trenical.user.proto.SignupRequest;
import it.trenical.user.proto.UserServiceGrpc;

import java.util.function.Consumer;

public class LoginCommand extends Command {
    private final UserServiceGrpc.UserServiceBlockingStub stub;
    private final Consumer<LoginResponse> onLoggedIn;

    public LoginCommand(UserServiceGrpc.UserServiceBlockingStub stub, Consumer<LoginResponse> onLoggedIn) {
        super("login", "Accedi al servizio");
        this.stub = stub;
        this.onLoggedIn = onLoggedIn;
    }

    @Override
    protected void action(String[] args) throws BadCommandSyntaxException {
        if (args.length != 2)
            throw new BadCommandSyntaxException(getSyntax());
        SigninRequest.Builder builder = SigninRequest.newBuilder();
        builder.setUsername(args[0]);
        builder.setPassword(args[1]);
        LoginResponse response = stub.login(builder.build());
        onLoggedIn.accept(response);
    }

    @Override
    public String getSyntax() {
        return getName() + " <username> <password>";
    }
}
