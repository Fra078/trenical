package it.trenical.ticketry.clients.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.proto.railway.PathResponse;
import it.trenical.proto.railway.PathsQueryParams;
import it.trenical.proto.railway.RailwayServiceGrpc;
import it.trenical.ticketry.clients.RailwayClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    public void findPaths(String start, String end, Consumer<PathResponse> consumer) {
        Iterator<PathResponse> it = stub.getPaths(PathsQueryParams.newBuilder().setDeparture(start).setArrival(end).build());
        while (it.hasNext())
            consumer.accept(it.next());
    }

    public static synchronized RailwayGrpcClient getInstance() {
        if (instance == null)
            instance = new RailwayGrpcClient();
        return instance;
    }

}
