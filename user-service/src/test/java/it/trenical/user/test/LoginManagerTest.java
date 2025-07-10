package it.trenical.user.test;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import it.trenical.user.managers.LoginManager;
import it.trenical.user.models.User;
import it.trenical.user.password.HashPasswordStrategy;
import it.trenical.user.password.PasswordHashManager;
import it.trenical.user.password.PlainPasswordStrategy;
import it.trenical.user.proto.LoginResponse;
import it.trenical.user.proto.SigninRequest;
import it.trenical.user.proto.SignupRequest;
import it.trenical.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LoginManagerTest {

    private UserRepository userRepository;

    private LoginManager loginManager;

    @BeforeEach
    void setUp() {
        userRepository = new InMemoryUserRepository();
        HashPasswordStrategy mockPasswordStrategy = new PlainPasswordStrategy();
        PasswordHashManager passwordManager = new PasswordHashManager(mockPasswordStrategy, List.of(mockPasswordStrategy));
        loginManager = new LoginManager(userRepository, passwordManager);
    }

    @Test
    void testRegister_Success() {
        SignupRequest request = SignupRequest.newBuilder()
                .setUsername("testuser")
                .setPassword("password")
                .setFirstName("Test")
                .setLastName("User")
                .build();

        LoginResponse response = loginManager.register(request);
        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
        assertEquals("Test", response.getFirstName());
        assertEquals("User", response.getLastName());
        assertTrue(userRepository.findByUsername("testuser").isPresent());
    }

    @Test
    void testRegister_UserAlreadyExists() {
        User existingUser = User.builder()
                .setUsername("testuser")
                .setPasswordHash("...")
                .setFirstName("TEST")
                .setLastName("TETET").build();
        try {userRepository.save(existingUser);} catch (Exception ignored) {}

        SignupRequest request = SignupRequest.newBuilder()
                .setUsername("testuser")
                .setPassword("password")
                .setFirstName("Test")
                .setLastName("User")
                .build();

        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> {
            loginManager.register(request);
        });

        assertEquals(Status.ALREADY_EXISTS.getCode(), exception.getStatus().getCode());

    }

    @Test
    void testLogin_Success() {
        SignupRequest signupRequest = SignupRequest.newBuilder()
                .setUsername("testuser")
                .setPassword("password")
                .setFirstName("Test")
                .setLastName("User")
                .build();
        loginManager.register(signupRequest);

        SigninRequest loginRequest = SigninRequest.newBuilder()
                .setUsername("testuser")
                .setPassword("password")
                .build();

        LoginResponse response = loginManager.login(loginRequest);

        assertNotNull(response);
        assertEquals("testuser", response.getUsername());
    }

    @Test
    void testLogin_UserNotFound() {
        SigninRequest loginRequest = SigninRequest.newBuilder()
                .setUsername("testuser")
                .setPassword("password")
                .build();

        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> {
            loginManager.login(loginRequest);
        });
        assertEquals(Status.UNAUTHENTICATED.getCode(), exception.getStatus().getCode());
    }

    @Test
    void testLogin_WrongPassword() {
        SignupRequest signupRequest = SignupRequest.newBuilder()
                .setUsername("testuser")
                .setPassword("password")
                .setFirstName("Test")
                .setLastName("User")
                .build();
        loginManager.register(signupRequest);

        SigninRequest signinRequest = SigninRequest.newBuilder()
                .setUsername("testuser").setPassword("BADPASSWORD").build();

        StatusRuntimeException exception = assertThrows(StatusRuntimeException.class, () -> {
            loginManager.login(signinRequest);
        });
        assertEquals(Status.UNAUTHENTICATED.getCode(), exception.getStatus().getCode());
    }
}