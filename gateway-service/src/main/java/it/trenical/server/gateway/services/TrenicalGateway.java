package it.trenical.server.gateway.services;

import io.grpc.stub.StreamObserver;
import it.trenical.common.proto.Empty;
import it.trenical.promotion.proto.*;
import it.trenical.server.gateway.client.GrpcClientManager;
import it.trenical.server.gateway.interceptors.JwtServerInterceptor;
import it.trenical.server.gateway.proto.TrenicalGatewayGrpc;
import it.trenical.ticketry.proto.TicketryServiceGrpc;
import it.trenical.ticketry.proto.TripQueryParams;
import it.trenical.travel.proto.TravelSolution;

public class TrenicalGateway extends TrenicalGatewayGrpc.TrenicalGatewayImplBase {

    private final TicketryServiceGrpc.TicketryServiceStub ticketryService;
    private final PromotionServiceGrpc.PromotionServiceStub promotionService;

    public TrenicalGateway(GrpcClientManager clientManager) {
        this.promotionService = clientManager.getPromotionStub();
        this.ticketryService = clientManager.getTicketryStub();
    }

    @Override
    public void queryTravelSolutions(TripQueryParams request, StreamObserver<TravelSolution> responseObserver) {
        TripQueryParams.Builder builder = TripQueryParams.newBuilder(request)
                .setUsername(JwtServerInterceptor.USER_ID.get());
        ticketryService.getTripSolutions(builder.build(), responseObserver);
    }

    @Override
    public void subscribeToLoyalty(Empty request, StreamObserver<Empty> responseObserver) {
        promotionService.subscribeToLoyalty(
                SubscribeToLoyaltyRequest.newBuilder()
                        .setUsername(JwtServerInterceptor.USER_ID.get())
                        .build(),
                responseObserver
        );
    }

    @Override
    public void unsubscribeToLoyalty(Empty request, StreamObserver<Empty> responseObserver) {
        promotionService.unsubscribeToLoyalty(
                UnsubscribeToLoyaltyRequest.newBuilder()
                        .setUsername(JwtServerInterceptor.USER_ID.get())
                        .build(),
                responseObserver
        );
    }

    @Override
    public void getLoyaltySubscriptionInfo(Empty request, StreamObserver<GetSubscriptionInfoResponse> responseObserver) {
        promotionService.getLoyaltySubscriptionInfo(
                GetSubscriptionInfoRequest.newBuilder()
                        .setUsername(JwtServerInterceptor.USER_ID.get())
                        .build(),
                responseObserver
        );
    }

    @Override
    public void listenToLoyaltyPromotions(Empty request, StreamObserver<PromotionMessage> responseObserver) {
        promotionService.listenToLoyaltyPromotions(
                ListenPromotionRequest.newBuilder()
                        .setUsername(JwtServerInterceptor.USER_ID.get())
                        .build(),
                responseObserver
        );
    }
}
