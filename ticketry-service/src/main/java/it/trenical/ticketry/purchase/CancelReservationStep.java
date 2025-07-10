package it.trenical.ticketry.purchase;

import io.grpc.Status;
import it.trenical.ticketry.models.Ticket;
import it.trenical.ticketry.repositories.TicketRepository;
import java.util.concurrent.CompletableFuture;

public class CancelReservationStep implements PurchaseStep {
    private final TicketRepository ticketRepository;
    private PurchaseStep next;

    public CancelReservationStep(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void setNext(PurchaseStep next) {
        this.next = next;
    }

    @Override
    public CompletableFuture<PurchaseContext> execute(PurchaseContext context) {
        return CompletableFuture.runAsync(() -> {
            if (context.getReservedTickets() != null && !context.getReservedTickets().isEmpty()) {
                ticketRepository.removeTicketsById(context.getOriginalSolution().getTrainId(), context.getReservedTickets().stream().map(Ticket::getId).toList());
            }
        }).thenCompose(v -> CompletableFuture.failedFuture(
            Status.ABORTED.withDescription("Processo di acquisto annullato a causa di un errore.").asRuntimeException()
        ));
    }
}