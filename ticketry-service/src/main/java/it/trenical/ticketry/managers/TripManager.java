package it.trenical.ticketry.managers;

import io.grpc.stub.StreamObserver;
import it.trenical.promotion.proto.TravelContextMessage;
import it.trenical.proto.train.ClassSeats;
import it.trenical.proto.train.ServiceClass;
import it.trenical.proto.train.TrainResponse;
import it.trenical.ticketry.clients.PromotionClient;
import it.trenical.ticketry.clients.TrainClient;
import it.trenical.ticketry.mappers.TravelContextBuilder;
import it.trenical.ticketry.mappers.TripMapper;
import it.trenical.ticketry.proto.TripQueryParams;
import it.trenical.ticketry.repositories.TicketRepository;
import it.trenical.travel.proto.TravelSolution;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TripManager {

    private final TrainClient trainClient;
    private final TicketRepository ticketRepository;
    private final PromotionClient promotionClient;

    public TripManager(
            TrainClient trainClient,
            TicketRepository ticketRepository,
            PromotionClient promotionClient
    ) {
        this.trainClient = trainClient;
        this.ticketRepository = ticketRepository;
        this.promotionClient = promotionClient;
    }


    public void getTripSolutions(TripQueryParams request, StreamObserver<TravelSolution> outputObserver) {

        final List<CompletableFuture<Void>> trackingList = new ArrayList<>();

        trainClient.getTrainForPath(TripMapper.mapToTrain(request), new StreamObserver<>() {
            @Override
            public void onNext(TrainResponse train) {
                handleTrainResponse(train, request, outputObserver, trackingList);
            }

            @Override
            public void onError(Throwable t) {
                outputObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                CompletableFuture.allOf(trackingList.toArray(new CompletableFuture[0]))
                        .whenComplete((unused, error) -> {
                            if (error == null)
                                outputObserver.onCompleted();
                        });
            }
        });
    }

    private void handleTrainResponse(
            TrainResponse train,
            TripQueryParams request,
            StreamObserver<TravelSolution> responseObserver,
            List<CompletableFuture<Void>> trackingList
    ) {
        TravelContextMessage ctx = new TravelContextBuilder().build(train, request, getAvailableClasses(train, request));
        CompletableFuture<Void> listenableFuture = promotionClient.applyPromotions(ctx)
                .thenAccept(risposta2 -> responseObserver.onNext(risposta2.getSolution()))
                .exceptionally(error -> {
                    responseObserver.onError(error);
                    return null;
                });

        trackingList.add(listenableFuture);
    }


    private List<ServiceClass> getAvailableClasses(TrainResponse train, TripQueryParams request) {
        Map<String, Integer> occupiedSeats = ticketRepository.countSeatsForTrain(train.getId());
        return train.getSeatsList().stream()
                .filter(sc->!request.hasServiceClass() || request.getServiceClass().equals(sc.getServiceClass().getName()))
                .filter(sc->{
                    int occupied = occupiedSeats.getOrDefault(sc.getServiceClass().getName(), 0);
                    return sc.getCount() - occupied >= request.getTicketCount();
                })
                .map(ClassSeats::getServiceClass).toList();
    }

}
