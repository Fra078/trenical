package it.trenical.ticketry.purchase;

import it.trenical.ticketry.models.Ticket;
import it.trenical.ticketry.proto.TicketConfirm;

import java.util.concurrent.CompletableFuture;

public class SuccessStep implements PurchaseStep {
    @Override
    public void setNext(PurchaseStep next) {}

    @Override
    public CompletableFuture<PurchaseContext> execute(PurchaseContext context) {
        context.setFinalConfirmation(TicketConfirm.newBuilder()
                        .addAllTicketId(context.getReservedTickets().stream().map(Ticket::getId).toList())
                .setTrainId(context.getOriginalSolution().getTrainId())
                .build());
        return CompletableFuture.completedFuture(context);
    }
}