package it.trenical.server.gateway.interceptors;

import io.grpc.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.trenical.server.jwt.JwtUtils;

import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

public class JwtServerInterceptor implements ServerInterceptor {
    public static final Context.Key<String> USER_ID = Context.key("USER_ID");
    public static final Metadata.Key<String> AUTH_HEADER_KEY =
            Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);

    private final SecretKey secretKey = Keys.hmacShaKeyFor(
            JwtUtils.SECRET_KEY.getBytes(StandardCharsets.UTF_8)
    );

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        String authHeader = headers.get(AUTH_HEADER_KEY);

        if (authHeader == null) {
            call.close(Status.UNAUTHENTICATED.withDescription("Authorization token is missing"), new Metadata());
            return new ServerCall.Listener<>() {
            };
        }

        if (!authHeader.startsWith("Bearer ")) {
            call.close(Status.UNAUTHENTICATED.withDescription("Unknown authentication type, expected Bearer token"), new Metadata());
            return new ServerCall.Listener<>() {
            };
        }

        String token = authHeader.substring(7);

        try {
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = jws.getBody();
            Context ctx = Context.current().withValue(USER_ID, claims.getSubject());

            return Contexts.interceptCall(ctx, call, headers, next);

        } catch (JwtException e) {
            System.err.println("Verifica JWT fallita: " + e.getMessage());
            call.close(Status.UNAUTHENTICATED.withDescription("Token validation failed: " + e.getMessage()), new Metadata());
            return new ServerCall.Listener<>() {
            };
        }
    }
}