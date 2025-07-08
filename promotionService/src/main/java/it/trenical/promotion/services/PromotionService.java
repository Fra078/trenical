package it.trenical.promotion.services;

import com.google.protobuf.Empty;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import it.trenical.promotion.managers.PromotionManager;
import it.trenical.promotion.proto.*;

public class PromotionService extends PromotionServiceGrpc.PromotionServiceImplBase {

    private final PromotionManager manager;

    public PromotionService(PromotionManager manager) {
        this.manager = manager;
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
            manager.registerPromotion(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (StatusRuntimeException exc){
            responseObserver.onError(exc);
        }
    }

    @Override
    public void updatePromotionState(UpdatePromotionRequest request, StreamObserver<Empty> responseObserver) {
        super.updatePromotionState(request, responseObserver);
    }

    @Override
    public void getAllPromotions(GetAllPromotionsRequest request, StreamObserver<PromotionMessage> responseObserver) {
        manager.findAllPromotions(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    @Override
    public void getPromotionById(GetPromotionRequest request, StreamObserver<PromotionMessage> responseObserver) {
        try {
            responseObserver.onNext(manager.findById(request));
            responseObserver.onCompleted();
        } catch (StatusRuntimeException exc){
            responseObserver.onError(exc);
        }
    }
}
