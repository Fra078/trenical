package it.trenical.ticketry.clients.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import it.trenical.proto.railway.GetPathRequest;
import it.trenical.proto.railway.PathResponse;
import it.trenical.proto.railway.RailwayServiceGrpc;
import it.trenical.ticketry.clients.RailwayClient;

import java.util.List;

public final class RailwayGrpcClient implements RailwayClient {
    private static RailwayGrpcClient instance;
    private final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 5050)
            .usePlaintext()
            .build();
    private final RailwayServiceGrpc.RailwayServiceBlockingStub stub
            = RailwayServiceGrpc.newBlockingStub(channel);

    private RailwayGrpcClient() {}

    @Override
    public List<Integer> findPaths(String start, String end) {
        stub.getAllPaths()
        return List.of();
    }

    public static synchronized RailwayGrpcClient getInstance() {
        if (instance == null)
            instance = new RailwayGrpcClient();
        return instance;
    }

}
