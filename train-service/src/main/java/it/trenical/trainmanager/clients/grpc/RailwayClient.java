package it.trenical.trainmanager.clients.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import it.trenical.proto.railway.GetPathRequest;
import it.trenical.proto.railway.PathResponse;
import it.trenical.proto.railway.RailwayServiceGrpc;

public final class RailwayClient {
    private static RailwayClient instance;
    private final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 5050)
            .usePlaintext()
            .build();
    private final RailwayServiceGrpc.RailwayServiceBlockingStub stub
            = RailwayServiceGrpc.newBlockingStub(channel);

    private RailwayClient() {}

    public PathResponse getPath(int pathId) {
        try {
            return stub.getPath(GetPathRequest.newBuilder().setId(pathId).build());
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND)
                return null;
            else throw e;
        }
    }

    public static synchronized RailwayClient getInstance() {
        if (instance == null)
            instance = new RailwayClient();
        return instance;
    }

}
