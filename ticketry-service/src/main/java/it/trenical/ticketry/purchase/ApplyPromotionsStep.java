package it.trenical.ticketry.purchase;

import it.trenical.ticketry.clients.PromotionClient;

import java.util.concurrent.CompletableFuture;

public class ApplyPromotionsStep implements PurchaseStep {
    private PurchaseStep next;
    private final PromotionClient promotionClient;

    public ApplyPromotionsStep(PromotionClient promotionClient) {
        this.promotionClient = promotionClient;
    }

    @Override
    public void setNext(PurchaseStep next) {
        this.next = next;
    }

    @Override
    public CompletableFuture<PurchaseContext> execute(PurchaseContext context) {
        return promotionClient.applyPromotions(context.getOriginalSolution())
                .thenCompose(response -> {
                    context.setProcessedSolution(response.getSolution());
                    return next.execute(context);
                })
                .exceptionallyCompose(ex -> {
                    return next.execute(context);
                });
    }
}