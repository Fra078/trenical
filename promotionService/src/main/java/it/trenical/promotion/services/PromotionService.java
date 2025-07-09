package it.trenical.promotion.services;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import it.trenical.common.proto.Empty;
import it.trenical.promotion.exceptions.AlreadyExistPromotionException;
import it.trenical.promotion.managers.FidelityProgramManager;
import it.trenical.promotion.managers.PromotionBroadcastManager;
import it.trenical.promotion.managers.PromotionManager;
import it.trenical.promotion.mappers.PromotionMapper;
import it.trenical.promotion.models.Promotion;
import it.trenical.promotion.models.conditions.FidelityCondition;
import it.trenical.promotion.observer.Broadcast;
import it.trenical.promotion.observer.StreamObserverAdapter;
import it.trenical.promotion.proto.*;

import java.util.NoSuchElementException;
import java.util.Optional;

public class PromotionService extends PromotionServiceGrpc.PromotionServiceImplBase {

    private final PromotionManager manager;
    private final FidelityProgramManager fidelityProgramManager;
    private final PromotionBroadcastManager broadcastManager;
    private final PromotionMapper promotionMapper;

    public PromotionService(
            PromotionManager manager,
            FidelityProgramManager fidelityProgramManager,
            PromotionBroadcastManager broadcastManager,
            PromotionMapper promotionMapper
    ) {
        this.manager = manager;
        this.fidelityProgramManager = fidelityProgramManager;
        this.promotionMapper = promotionMapper;
        this.broadcastManager = broadcastManager;
    }

    @Override
    public void applyPromotions(ApplyPromotionRequest request, StreamObserver<ApplyPromotionResponse> responseObserver) {
        try {
            ApplyPromotionResponse response = ApplyPromotionResponse.newBuilder()
                    .setSolution(manager.applyPromotion(request.getSolution()))
                    .build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (StatusRuntimeException e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void registerPromotion(PromotionMessage request, StreamObserver<Empty> responseObserver) {
        try {
            Promotion promotion = promotionMapper.fromProto(request);
            manager.registerPromotion(promotion);
            if (promotion.getConditions().stream().anyMatch(c->c instanceof FidelityCondition))
                broadcastManager.broadcast(promotion);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (AlreadyExistPromotionException exc){
            responseObserver.onError(Status.ALREADY_EXISTS.withDescription(exc.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void getAllPromotions(GetAllPromotionsRequest request, StreamObserver<PromotionMessage> responseObserver) {
        manager.findAllPromotions((promo)->
                responseObserver.onNext(promotionMapper.toProto(promo))
        );
        responseObserver.onCompleted();
    }

    @Override
    public void getPromotionById(GetPromotionRequest request, StreamObserver<PromotionMessage> responseObserver) {
        try {
            Promotion response = manager.findById(request.getId());
            responseObserver.onNext(promotionMapper.toProto(response));
            responseObserver.onCompleted();
        } catch (NoSuchElementException exc){
            responseObserver.onError(Status.NOT_FOUND.withDescription(exc.getMessage()).asRuntimeException());
        }
    }


    @Override
    public void subscribeToFidelity(SubscribeToFidelityRequest request, StreamObserver<Empty> responseObserver) {
        boolean done = fidelityProgramManager.subscribeToProgram(request.getUsername());
        if (!done) {
            responseObserver.onError(Status.ALREADY_EXISTS.withDescription("User is already subscribed").asRuntimeException());
            return;
        }
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void unsubscribeToFidelity(UnsubscribeToFidelityRequest request, StreamObserver<Empty> responseObserver) {
        boolean done = fidelityProgramManager.unsubscribeToProgram(request.getUsername());
        if (!done) {
            responseObserver.onError(Status.NOT_FOUND.withDescription("User is not subscribed").asRuntimeException());
            return;
        }
        broadcastManager.disconnectUser(request.getUsername());
        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void getFidelitySubscriptionInfo(GetSubscriptionInfoRequest request, StreamObserver<GetSubscriptionInfoResponse> responseObserver) {
        try {
            Optional<Long> subscriptionDate = fidelityProgramManager.getSubscriptionInfo(request.getUsername());
            responseObserver.onNext(GetSubscriptionInfoResponse.newBuilder()
                                        .setSubscribed(subscriptionDate.isPresent())
                                        .setFromDate(subscriptionDate.orElse(0L))
                                        .build());
            responseObserver.onCompleted();
        } catch (StatusRuntimeException exc){
            responseObserver.onError(exc);
        }
    }

    @Override
    public void listenToFidelityPromotions(ListenPromotionRequest request, StreamObserver<PromotionMessage> responseObserver) {
        if (fidelityProgramManager.getSubscriptionInfo(request.getUsername()).isEmpty()) {
            responseObserver.onError(Status.PERMISSION_DENIED.withDescription("User is not subscribed").asRuntimeException());
            return;
        }
        broadcastManager.register(request.getUsername(), new StreamObserverAdapter<>(responseObserver, promotionMapper::toProto));
        /*((ServerCallStreamObserver<PromotionMessage>) responseObserver).setOnCancelHandler(()->
                broadcastManager.disconnectUser(request.getUsername()));*/
    }
}
