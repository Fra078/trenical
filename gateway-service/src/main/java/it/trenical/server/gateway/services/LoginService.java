package it.trenical.server.gateway.services;

import io.grpc.stub.StreamObserver;
import it.trenical.server.gateway.client.GrpcClientManager;
import it.trenical.server.gateway.proto.LoginServiceGrpc;
import it.trenical.user.proto.LoginResponse;
import it.trenical.user.proto.SigninRequest;
import it.trenical.user.proto.SignupRequest;
import it.trenical.user.proto.UserServiceGrpc;

public class LoginService extends LoginServiceGrpc.LoginServiceImplBase {

    private final UserServiceGrpc.UserServiceStub userServiceStub;

    public LoginService(GrpcClientManager grpcClientManager) {
        this.userServiceStub = grpcClientManager.getUserStub();
    }

    @Override
    public void login(SigninRequest request, StreamObserver<LoginResponse> responseObserver) {
        userServiceStub.login(request, responseObserver);
    }

    @Override
    public void register(SignupRequest request, StreamObserver<LoginResponse> responseObserver) {
        userServiceStub.register(request, responseObserver);
    }

}
