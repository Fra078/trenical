package it.trenical.ticketry.test.purchase;

import it.trenical.promotion.proto.ApplyPromotionResponse;
import it.trenical.ticketry.clients.PromotionClient;
import it.trenical.travel.proto.TravelSolution;

import java.util.concurrent.CompletableFuture;

public class MockPromotionClient implements PromotionClient {

    public boolean shouldFail = false;

    @Override
    public CompletableFuture<ApplyPromotionResponse> applyPromotions(TravelSolution solution) {
        if (shouldFail)
            return CompletableFuture.failedFuture(new RuntimeException("Not available"));
        return CompletableFuture.completedFuture(ApplyPromotionResponse.newBuilder().setSolution(solution).build());
    }
}
