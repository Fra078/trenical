package it.trenical.promotion.services;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import it.trenical.proto.railway.GetAllPromotionsRequest;
import it.trenical.proto.railway.Promotion;
import it.trenical.proto.railway.PromotionServiceGrpc;
import it.trenical.proto.railway.UpdatePromotionRequest;

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
