package it.trenical.ticketry.purchase;

import it.trenical.ticketry.purchase.PurchaseContext;
import it.trenical.ticketry.purchase.PurchaseStep;
import it.trenical.ticketry.repositories.TicketRepository;
import java.util.concurrent.CompletableFuture;

public class ConfirmTicketsStep implements PurchaseStep {
    private PurchaseStep next;
    private final TicketRepository ticketRepository;

    public ConfirmTicketsStep(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void setNext(PurchaseStep next) {
        this.next = next;
    }

    @Override
    public CompletableFuture<PurchaseContext> execute(PurchaseContext context) {
        return CompletableFuture.runAsync(() -> {
            ticketRepository.confirmTickets(context.getReservedTickets());
        }).thenCompose(v -> {
            if (next != null) {
                return next.execute(context);
            }
            return CompletableFuture.completedFuture(context);
        });
    }
}