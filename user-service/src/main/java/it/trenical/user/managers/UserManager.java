package it.trenical.user.managers;

import io.grpc.Status;
import it.trenical.user.mapper.UserMapper;
import it.trenical.user.models.User;
import it.trenical.user.proto.GetUserRequest;
import it.trenical.user.proto.UserResponse;
import it.trenical.user.repository.UserRepository;

public class UserManager {

    private final UserRepository userRepository;

    public UserManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUser(GetUserRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> Status.NOT_FOUND.withDescription("User not found").asRuntimeException()
        );
        return UserMapper.toUserResponse(user);
    }

}
