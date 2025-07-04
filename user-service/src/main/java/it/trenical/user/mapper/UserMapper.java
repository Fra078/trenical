package it.trenical.user.mapper;

import it.trenical.user.models.User;
import it.trenical.user.proto.LoginResponse;
import it.trenical.user.proto.SignupRequest;
import it.trenical.user.proto.UserResponse;
import it.trenical.user.proto.UserType;

public class UserMapper {
    private UserMapper(){
        throw new IllegalStateException("Utility class");
    }

    public static User fromSignupRequest(SignupRequest request, String passwordHash){
        return User.builder()
                .setUsername(request.getUsername())
                .setPasswordHash(passwordHash)
                .setFirstName(request.getFirstName())
                .setLastName(request.getLastName())
                .setType(User.Type.STANDARD)
                .build();
    }

    public static LoginResponse toLoginResponse(User user, String token){
        return LoginResponse.newBuilder()
                .setJwt(token)
                .setFirstName(user.firstName())
                .setLastName(user.lastName())
                .setUsername(user.username())
                .setType(toDto(user.type()))
                .build();
    }

    public static UserResponse toUserResponse(User user){
        return UserResponse.newBuilder()
                .setFirstName(user.firstName())
                .setLastName(user.lastName())
                .setUsername(user.username())
                .setType(toDto(user.type()))
                .build();
    }

    public static UserType toDto(User.Type type) {
        return switch (type) {
            case STANDARD -> UserType.STANDARD;
            case FIDELITY -> UserType.FIDELITY;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }
}
