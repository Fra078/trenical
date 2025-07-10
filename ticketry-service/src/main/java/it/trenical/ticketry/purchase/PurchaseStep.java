package it.trenical.ticketry.purchase;

import java.util.concurrent.CompletableFuture;

public interface PurchaseStep {
    void setNext(PurchaseStep next);
    CompletableFuture<PurchaseContext> execute(PurchaseContext context);
}