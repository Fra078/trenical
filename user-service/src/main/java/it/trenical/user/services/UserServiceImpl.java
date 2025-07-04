package it.trenical.user.services;

import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import it.trenical.user.managers.LoginManager;
import it.trenical.user.managers.UserManager;
import it.trenical.user.proto.*;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    private final LoginManager loginManager;
    private final UserManager userManager;

    public UserServiceImpl(LoginManager loginManager, UserManager userManager) {
        this.loginManager = loginManager;
        this.userManager = userManager;
    }

    @Override
    public void register(SignupRequest request, StreamObserver<LoginResponse> responseObserver) {
        try {
            responseObserver.onNext(loginManager.register(request));
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e){
            responseObserver.onError(e);
        }
    }

    @Override
    public void login(SigninRequest request, StreamObserver<LoginResponse> responseObserver) {
        try {
            responseObserver.onNext(loginManager.login(request));
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e){
            responseObserver.onError(e);
        }
    }

    @Override
    public void getUser(GetUserRequest request, StreamObserver<UserResponse> responseObserver) {
        try {
            responseObserver.onNext(userManager.getUser(request));
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e){
            responseObserver.onError(e);
        }
    }
}
