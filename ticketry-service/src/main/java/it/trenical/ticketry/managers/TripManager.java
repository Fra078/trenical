package it.trenical.ticketry.managers;

import io.grpc.stub.StreamObserver;
import it.trenical.promotion.proto.ApplyPromotionResponse;
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

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

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


    public void getTripSolutions(TripQueryParams request, StreamObserver<TravelSolution> observer) {

        final AtomicInteger pendingCalls = new AtomicInteger(0);
        final AtomicBoolean trainStreamCompleted = new AtomicBoolean(false);

        Runnable checkCompletion = () -> {
            if (trainStreamCompleted.get() && pendingCalls.get() == 0) {
                observer.onCompleted();
            }
        };

        StreamObserver<ApplyPromotionResponse> solutionObserver = new StreamObserver<>() {

            @Override
            public void onNext(ApplyPromotionResponse response) {
                observer.onNext(response.getSolution());
            }

            @Override
            public void onError(Throwable throwable) {
                observer.onError(throwable);
                pendingCalls.decrementAndGet();
                checkCompletion.run();
            }

            @Override
            public void onCompleted() {
                pendingCalls.decrementAndGet();
                checkCompletion.run();
            }
        };

        StreamObserver<TrainResponse> trainObserver = new StreamObserver<>() {

            @Override
            public void onNext(TrainResponse train) {
                pendingCalls.incrementAndGet();

                try {
                    List<ServiceClass> availableClasses = getAvailableClasses(train, request);
                    TravelContextMessage msg = new TravelContextBuilder().build(train, request, availableClasses);
                    promotionClient.applyPromotions(msg, solutionObserver);
                } catch (Exception e) {
                    pendingCalls.decrementAndGet();
                    observer.onError(e);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                observer.onError(throwable);
            }

            @Override
            public void onCompleted() {
                trainStreamCompleted.set(true);
                checkCompletion.run();
            }
        };

        trainClient.getTrainForPath(
                TripMapper.mapToTrain(request),
                trainObserver
        );
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
