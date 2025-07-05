package it.trenical.trainmanager.clients.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import it.trenical.proto.railway.GetPathRequest;
import it.trenical.proto.railway.PathResponse;
import it.trenical.proto.railway.PathsQueryParams;
import it.trenical.proto.railway.RailwayServiceGrpc;
import it.trenical.trainmanager.clients.RailwayClient;

import java.util.Iterator;
import java.util.function.Consumer;

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
    public PathResponse getPath(int pathId) {
        try {
            return stub.getPath(GetPathRequest.newBuilder().setId(pathId).build());
        } catch (StatusRuntimeException e) {
            if (e.getStatus().getCode() == Status.Code.NOT_FOUND)
                return null;
            else throw e;
        }
    }

    @Override
    public void findPaths(String departure, String arrival, Consumer<PathResponse> consumer) {
        try {
            Iterator<PathResponse> it = stub.getPaths(PathsQueryParams.newBuilder()
                            .setDeparture(departure)
                            .setArrival(arrival).build());
            it.forEachRemaining(consumer);
        } catch (StatusRuntimeException e) {
            throw e;
        }
    }

    public static synchronized RailwayGrpcClient getInstance() {
        if (instance == null)
            instance = new RailwayGrpcClient();
        return instance;
    }

}
