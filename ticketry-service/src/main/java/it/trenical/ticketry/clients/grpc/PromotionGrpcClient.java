package it.trenical.ticketry.clients.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import it.trenical.promotion.proto.ApplyPromotionRequest;
import it.trenical.promotion.proto.ApplyPromotionResponse;
import it.trenical.promotion.proto.PromotionServiceGrpc;
import it.trenical.promotion.proto.TravelContextMessage;
import it.trenical.ticketry.clients.PromotionClient;


public class PromotionGrpcClient implements PromotionClient {

    private static PromotionGrpcClient instance;
    private final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 5606)
            .usePlaintext()
            .build();
    private final PromotionServiceGrpc.PromotionServiceStub stub
            = PromotionServiceGrpc.newStub(channel);

    private PromotionGrpcClient() {}


    public void applyPromotions(TravelContextMessage ctx){
        stub.applyPromotions(
                ApplyPromotionRequest.newBuilder().setContext(ctx).build(),
                observer
        );
    }

    public static synchronized PromotionGrpcClient getInstance() {
        if (instance == null)
            instance = new PromotionGrpcClient();
        return instance;
    }

}
