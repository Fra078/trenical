package it.trenical.ticketry.purchase;

import it.trenical.ticketry.proto.TicketConfirm;

import java.util.concurrent.CompletableFuture;

public class SuccessStep implements PurchaseStep {
    @Override
    public void setNext(PurchaseStep next) {}

    @Override
    public CompletableFuture<PurchaseContext> execute(PurchaseContext context) {
        context.setFinalConfirmation(TicketConfirm.newBuilder().build());
        return CompletableFuture.completedFuture(context);
    }
}