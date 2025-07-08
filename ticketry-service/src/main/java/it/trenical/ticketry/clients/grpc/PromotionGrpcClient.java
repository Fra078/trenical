package it.trenical.ticketry.clients.grpc;

import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import it.trenical.promotion.proto.ApplyPromotionRequest;
import it.trenical.promotion.proto.ApplyPromotionResponse;
import it.trenical.promotion.proto.PromotionServiceGrpc;
import it.trenical.promotion.proto.TravelContextMessage;
import it.trenical.server.future.FutureMapper;
import it.trenical.ticketry.clients.PromotionClient;
import it.trenical.travel.proto.TravelSolution;

import java.util.concurrent.CompletableFuture;


public class PromotionGrpcClient implements PromotionClient {

    private static PromotionGrpcClient instance;
    private final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 5606)
            .usePlaintext()
            .build();
    private final PromotionServiceGrpc.PromotionServiceFutureStub stub
            = PromotionServiceGrpc.newFutureStub(channel);

    private PromotionGrpcClient() {}


    public CompletableFuture<ApplyPromotionResponse> applyPromotions(TravelSolution solution){
        return FutureMapper.toCompletableFuture(stub.applyPromotions(
                ApplyPromotionRequest.newBuilder().setSolution(solution).build()));
    }

    public static synchronized PromotionGrpcClient getInstance() {
        if (instance == null)
            instance = new PromotionGrpcClient();
        return instance;
    }

}
