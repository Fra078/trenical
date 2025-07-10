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
        // Eseguiamo l'operazione in modo asincrono
        return CompletableFuture.runAsync(() -> {
            // Confermiamo i biglietti che abbiamo precedentemente riservato nel contesto
            ticketRepository.confirmTickets(context.getReservedTickets());
        }).thenCompose(v -> {
            // Una volta completata la conferma, passiamo al passo successivo (es. SuccessStep)
            if (next != null) {
                return next.execute(context);
            }
            // Se questo è l'ultimo passo, restituiamo semplicemente il contesto
            return CompletableFuture.completedFuture(context);
        });
    }
}