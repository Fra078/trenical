package it.trenical.user.managers;

import io.grpc.Status;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import it.trenical.server.jwt.JwtUtils;
import it.trenical.user.exceptions.UserAlreadyExistsException;
import it.trenical.user.mapper.UserMapper;
import it.trenical.user.models.User;
import it.trenical.user.password.PasswordHashManager;
import it.trenical.user.proto.LoginResponse;
import it.trenical.user.proto.SigninRequest;
import it.trenical.user.proto.SignupRequest;
import it.trenical.user.repository.UserRepository;

import java.util.Date;

public class LoginManager {

    private static final long EXPIRATION_TIME = 900_000;
    private final UserRepository userRepository;
    private final PasswordHashManager passwordManager;

    public LoginManager(UserRepository userRepository, PasswordHashManager passwordManager) {
        this.userRepository = userRepository;
        this.passwordManager = passwordManager;
    }

    public LoginResponse register(SignupRequest request) {
        User user = UserMapper.fromSignupRequest(
                request, passwordManager.getHash(request.getPassword()));
        try {
            userRepository.save(user);
            return UserMapper.toLoginResponse(user, generateJwtFromUser(user));
        } catch (UserAlreadyExistsException e) {
            throw Status.ALREADY_EXISTS.withDescription(e.getMessage()).asRuntimeException();
        }
    }

    public LoginResponse login(SigninRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> Status.UNAUTHENTICATED.withDescription("Username does not exists!").asRuntimeException());
        if (passwordManager.verify(request.getPassword(), user.passwordHash())) {
            return UserMapper.toLoginResponse(user, generateJwtFromUser(user));
        }
        throw Status.UNAUTHENTICATED.withDescription("Wrong password!").asRuntimeException();
    }


    private String generateJwtFromUser(User user) {
        return Jwts.builder()
                .setSubject(user.username())
                .setIssuer("TrenicalSystem")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(Keys.hmacShaKeyFor(JwtUtils.SECRET_KEY.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

}
