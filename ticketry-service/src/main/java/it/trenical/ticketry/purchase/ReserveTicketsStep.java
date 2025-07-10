package it.trenical.ticketry.purchase;

import io.grpc.Status;
import it.trenical.ticketry.models.Ticket;
import it.trenical.ticketry.repositories.TicketRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ReserveTicketsStep implements PurchaseStep {
    private PurchaseStep next;
    private final TicketRepository ticketRepository;

    public ReserveTicketsStep(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void setNext(PurchaseStep next) {
        this.next = next;
    }

    @Override
    public CompletableFuture<PurchaseContext> execute(PurchaseContext context) {
        Ticket prototype = Ticket.newBuilder()
                .trainId(context.getOriginalSolution().getTrainId())
                .customerId(context.getOriginalSolution().getUserId())
                .className(context.getOriginalSolution().getModes(0).getServiceClass().getName())
                .departure(context.getOriginalSolution().getRouteInfo().getDepartureStation())
                .status(Ticket.Status.WAITING)
                .arrival(context.getOriginalSolution().getRouteInfo().getArrivalStation())
                .build();
        
        List <Ticket> tickets = ticketRepository.addTicketIfPossible(prototype, context.getProcessedSolution().getTicketCount(), context.getMaxClassCount());
        System.out.println("RESERVED: " + tickets.stream().map(Ticket::getId).toList());
        if (tickets.isEmpty()) {
            return CompletableFuture.failedFuture(Status.UNAVAILABLE.withDescription("There aren't enough seats available").asRuntimeException());
        }
        context.setReservedTickets(tickets);
        return next.execute(context);
    }
}