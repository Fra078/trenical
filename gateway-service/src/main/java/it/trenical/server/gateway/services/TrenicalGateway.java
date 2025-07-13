package it.trenical.server.gateway.services;

import io.grpc.stub.StreamObserver;
import it.trenical.common.proto.Empty;
import it.trenical.promotion.proto.*;
import it.trenical.proto.railway.RailwayServiceGrpc;
import it.trenical.proto.railway.StationResponse;
import it.trenical.proto.train.ListenToTrainRequest;
import it.trenical.proto.train.ServiceClass;
import it.trenical.proto.train.TrainManagerGrpc;
import it.trenical.proto.train.TrainTypeResponse;
import it.trenical.server.gateway.client.GrpcClientManager;
import it.trenical.server.gateway.interceptors.JwtServerInterceptor;
import it.trenical.server.gateway.proto.TrenicalGatewayGrpc;
import it.trenical.ticketry.proto.ListenToMyTrainsRequest;
import it.trenical.ticketry.proto.TicketryServiceGrpc;
import it.trenical.ticketry.proto.TripQueryParams;
import it.trenical.train.proto.TrainUpdate;
import it.trenical.travel.proto.TravelSolution;

public class TrenicalGateway extends TrenicalGatewayGrpc.TrenicalGatewayImplBase {

    private final TicketryServiceGrpc.TicketryServiceStub ticketryService;
    private final PromotionServiceGrpc.PromotionServiceStub promotionService;
    private final TrainManagerGrpc.TrainManagerStub trainService;
    private final RailwayServiceGrpc.RailwayServiceStub railwayService;

    public TrenicalGateway(GrpcClientManager clientManager) {
        this.promotionService = clientManager.getPromotionStub();
        this.ticketryService = clientManager.getTicketryStub();
        this.trainService = clientManager.getTrainStub();
        this.railwayService = clientManager.getRailwayStub();
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

    @Override
    public void getAllServiceClasses(Empty request, StreamObserver<ServiceClass> responseObserver) {
        trainService.getAllServiceClasses(com.google.protobuf.Empty.getDefaultInstance(), responseObserver);
    }

    @Override
    public void getAllTrainTypes(Empty request, StreamObserver<TrainTypeResponse> responseObserver) {
        trainService.getAllTrainTypes(com.google.protobuf.Empty.getDefaultInstance(), responseObserver);
    }

    @Override
    public void getAllStations(Empty request, StreamObserver<StationResponse> responseObserver) {
        railwayService.getAllStations(com.google.protobuf.Empty.getDefaultInstance(), responseObserver);
    }

    @Override
    public void listenToTrainUpdates(ListenToTrainRequest request, StreamObserver<TrainUpdate> responseObserver) {
        trainService.listenToTrainUpdates(request, responseObserver);
    }

    @Override
    public void listenToMyTrainUpdates(Empty request, StreamObserver<TrainUpdate> responseObserver) {
        ticketryService.listenToMyTrains(ListenToMyTrainsRequest.newBuilder()
                .setUsername(JwtServerInterceptor.USER_ID.get())
                .build(),
                responseObserver
        );
    }
}
