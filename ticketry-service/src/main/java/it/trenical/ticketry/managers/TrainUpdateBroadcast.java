package it.trenical.ticketry.managers;

import io.grpc.stub.StreamObserver;
import it.trenical.ticketry.clients.TrainClient;
import it.trenical.ticketry.models.Ticket;
import it.trenical.ticketry.repositories.TicketRepository;
import it.trenical.train.proto.TrainUpdate;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class TrainUpdateBroadcast {

    private final Map<String, Consumer<TrainUpdate>> observers = new ConcurrentHashMap<>();
    private final TicketRepository ticketRepository;

    public TrainUpdateBroadcast(TrainClient trainClient, TicketRepository ticketRepository) {
        trainClient.listenForUpdates(new UpdateListener());
        this.ticketRepository = ticketRepository;
    }

    private void pushToInterestedUsers(TrainUpdate trainUpdate) {
        List<Ticket> tickets = ticketRepository.findByTrainId(trainUpdate.getTrainId());
        for (Ticket ticket : tickets) {
            if (observers.containsKey(ticket.getCustomerId())) {
                try {
                    observers.get(ticket.getCustomerId()).accept(trainUpdate);
                } catch (Exception e) {
                    observers.remove(ticket.getCustomerId());
                }
            }
        }
    }

    public void subscribe(String username, Consumer<TrainUpdate> observer) {
        observers.put(username, observer);
    }


    private class UpdateListener implements StreamObserver<TrainUpdate> {
        @Override
        public void onNext(TrainUpdate trainUpdate) {
            pushToInterestedUsers(trainUpdate);
        }

        @Override
        public void onError(Throwable throwable) {
          System.out.println("Broadcast Error: " + throwable.getMessage());
        }

        @Override
        public void onCompleted() {}
    }

}
