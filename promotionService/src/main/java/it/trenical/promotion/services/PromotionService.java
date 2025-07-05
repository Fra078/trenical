package it.trenical.promotion.services;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import it.trenical.promotion.proto.GetAllPromotionsRequest;
import it.trenical.promotion.proto.Promotion;
import it.trenical.promotion.proto.PromotionServiceGrpc;
import it.trenical.promotion.proto.UpdatePromotionRequest;

public class PromotionService extends PromotionServiceGrpc.PromotionServiceImplBase {

    public PromotionService(){

    }

    @Override
    public void registerPromotion(Promotion request, StreamObserver<Empty> responseObserver) {
        super.registerPromotion(request, responseObserver);
    }

    @Override
    public void updatePromotionState(UpdatePromotionRequest request, StreamObserver<Empty> responseObserver) {
        super.updatePromotionState(request, responseObserver);
    }

    @Override
    public void getAllPromotions(GetAllPromotionsRequest request, StreamObserver<Promotion> responseObserver) {
        super.getAllPromotions(request, responseObserver);
    }
}
