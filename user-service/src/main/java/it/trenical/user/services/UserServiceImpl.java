package it.trenical.user.services;

import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import it.trenical.user.managers.LoginManager;
import it.trenical.user.proto.LoginResponse;
import it.trenical.user.proto.SigninRequest;
import it.trenical.user.proto.SignupRequest;
import it.trenical.user.proto.UserServiceGrpc;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    private final LoginManager loginManager;

    public UserServiceImpl(LoginManager loginManager) {
        this.loginManager = loginManager;
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
}
