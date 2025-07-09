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
    public void subscribeToFidelity(Empty request, StreamObserver<Empty> responseObserver) {
        promotionService.subscribeToFidelity(
                SubscribeToFidelityRequest.newBuilder()
                        .setUsername(JwtServerInterceptor.USER_ID.get())
                        .build(),
                responseObserver
        );
    }

    @Override
    public void unsubscribeToFidelity(Empty request, StreamObserver<Empty> responseObserver) {
        promotionService.unsubscribeToFidelity(
                UnsubscribeToFidelityRequest.newBuilder()
                        .setUsername(JwtServerInterceptor.USER_ID.get())
                        .build(),
                responseObserver
        );
    }

    @Override
    public void getFidelitySubscriptionInfo(Empty request, StreamObserver<GetSubscriptionInfoResponse> responseObserver) {
        promotionService.getFidelitySubscriptionInfo(
                GetSubscriptionInfoRequest.newBuilder()
                        .setUsername(JwtServerInterceptor.USER_ID.get())
                        .build(),
                responseObserver
        );
    }

    @Override
    public void listenToFidelityPromotions(Empty request, StreamObserver<PromotionMessage> responseObserver) {
        promotionService.listenToFidelityPromotions(
                ListenPromotionRequest.newBuilder()
                        .setUsername(JwtServerInterceptor.USER_ID.get())
                        .build(),
                responseObserver
        );
    }
}
