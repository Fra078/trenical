package it.trenical.user;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.trenical.server.database.DatabaseManager;
import it.trenical.user.managers.LoginManager;
import it.trenical.user.managers.UserManager;
import it.trenical.user.password.PasswordHashManager;
import it.trenical.user.password.PlainPasswordStrategy;
import it.trenical.user.repository.UserRepository;
import it.trenical.user.repository.jdbc.UserJdbcRepository;
import it.trenical.user.services.UserServiceImpl;

import java.io.IOException;
import java.util.List;

public class UserServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        DatabaseManager db = new DatabaseManager("./user-db");
        UserRepository userRepository = new UserJdbcRepository(db);
        LoginManager loginManager = new LoginManager(userRepository, setupPasswordHashManager());
        UserManager userManager = new UserManager(userRepository);
        Server server =ServerBuilder.forPort(5060)
                .addService(new UserServiceImpl(loginManager, userManager))
                .build().start();
        System.out.println("Server started");
        server.awaitTermination();
    }

    private static PasswordHashManager setupPasswordHashManager() {
        PlainPasswordStrategy plainStrategy = new PlainPasswordStrategy();
        return new PasswordHashManager(plainStrategy, List.of(plainStrategy));
    }
}
