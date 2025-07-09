package it.trenical.server.gateway;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptors;
import it.trenical.server.gateway.client.GrpcClientManager;
import it.trenical.server.gateway.interceptors.JwtServerInterceptor;
import it.trenical.server.gateway.services.LoginService;
import it.trenical.server.gateway.services.TrenicalGateway;

import java.io.IOException;

public class GatewayServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        GrpcClientManager grpcClientManager = new GrpcClientManager();
        Server server = ServerBuilder.forPort(6060)
                .addService(new LoginService(grpcClientManager))
                .addService(ServerInterceptors.intercept(
                        new TrenicalGateway(grpcClientManager),
                        new JwtServerInterceptor()
                ))
                .build().start();

        server.awaitTermination();
    }

}
