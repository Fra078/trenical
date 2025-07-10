package it.trenical.ticketry.purchase;

import it.trenical.ticketry.models.Ticket;
import it.trenical.ticketry.proto.TicketConfirm;
import it.trenical.travel.proto.TravelSolution;

import java.util.List;

public class PurchaseContext {
    private final TravelSolution originalSolution;
    private TravelSolution processedSolution;
    private List<Ticket> reservedTickets;
    private final String creditCard;
    private final int maxClassCount;
    private TicketConfirm finalConfirmation;

    public PurchaseContext(TravelSolution solution, String creditCard, int maxClassCount) {
        this.originalSolution = solution;
        this.processedSolution = solution;
        this.creditCard = creditCard;
        this.maxClassCount = maxClassCount;
    }

    public TravelSolution getOriginalSolution() {
        return originalSolution;
    }

    public void setProcessedSolution(TravelSolution processedSolution) {
        this.processedSolution = processedSolution;
    }

    public TravelSolution getProcessedSolution() {
        return processedSolution;
    }

    public int getMaxClassCount() {
        return maxClassCount;
    }

    public void setReservedTickets(List<Ticket> reservedTickets) {
        this.reservedTickets = reservedTickets;
    }

    public List<Ticket> getReservedTickets() {
        return reservedTickets;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setFinalConfirmation(TicketConfirm finalConfirmation) {
        this.finalConfirmation = finalConfirmation;
    }

    public TicketConfirm getFinalConfirmation() {
        return finalConfirmation;
    }
}