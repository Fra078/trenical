package it.trenical.ticketry.clients;

import io.grpc.stub.StreamObserver;
import it.trenical.promotion.proto.ApplyPromotionResponse;
import it.trenical.promotion.proto.TravelContextMessage;

public interface PromotionClient {
    void applyPromotions(TravelContextMessage ctx, StreamObserver<ApplyPromotionResponse> observer);
}
