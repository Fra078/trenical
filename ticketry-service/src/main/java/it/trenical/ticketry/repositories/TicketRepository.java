package it.trenical.ticketry.repositories;

import it.trenical.ticketry.models.Ticket;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TicketRepository {
    Optional<Ticket> findById(int trainId, int id);
    List<Ticket> findByCustomerId(int customerId);
    List<Ticket> findByTrainId(int trainId);
    Map<String, Integer> countSeatsForTrain(int trainId);
    void confirmTickets(List<Ticket> tickets);
    void removeTicketsById(List<Integer> id);
    List<Ticket> addTicketIfPossible(Ticket ticket, int count, int maxClassCount);
}