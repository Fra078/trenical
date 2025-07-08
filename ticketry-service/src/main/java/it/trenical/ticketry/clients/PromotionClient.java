package it.trenical.ticketry.clients;

import it.trenical.promotion.proto.ApplyPromotionResponse;
import it.trenical.travel.proto.TravelSolution;

import java.util.concurrent.CompletableFuture;

public interface PromotionClient {
    CompletableFuture<ApplyPromotionResponse> applyPromotions(TravelSolution solution);
}
