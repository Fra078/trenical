package it.trenical.ticketry.managers;

import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import it.trenical.proto.train.ClassSeats;
import it.trenical.proto.train.TrainResponse;
import it.trenical.ticketry.clients.PaymentClient;
import it.trenical.ticketry.clients.PromotionClient;
import it.trenical.ticketry.clients.TrainClient;
import it.trenical.ticketry.proto.PurchaseTicketRequest;
import it.trenical.ticketry.proto.TicketConfirm;
import it.trenical.ticketry.purchase.*;
import it.trenical.ticketry.repositories.TicketRepository;
import it.trenical.travel.proto.TravelSolution;

import java.util.List;
import java.util.NoSuchElementException;

public class PurchaseManager {

    private final TrainClient trainClient;
    private final TravelSolutionFactory solutionFactory;
    private final PurchaseStep purchaseChain;

    public PurchaseManager(
            TrainClient trainClient,
            TicketRepository ticketRepository,
            PromotionClient promotionClient,
            PaymentClient paymentClient,
            TravelSolutionFactory factory
    ) {
        this.trainClient = trainClient;
        this.solutionFactory = factory;
        this.purchaseChain = buildPurchaseChain(ticketRepository, promotionClient, paymentClient);
    }

    private PurchaseStep buildPurchaseChain(TicketRepository ticketRepository, PromotionClient promotionClient, PaymentClient paymentClient) {
        PurchaseStep failureHandler = new CancelReservationStep(ticketRepository);
        PurchaseStep successHandler = new SuccessStep();

        PurchaseStep confirmStep = new ConfirmTicketsStep(ticketRepository);
        confirmStep.setNext(successHandler);

        PurchaseStep paymentStep = new ProcessPaymentStep(paymentClient, failureHandler);
        paymentStep.setNext(confirmStep);

        PurchaseStep reserveStep = new ReserveTicketsStep(ticketRepository);
        reserveStep.setNext(paymentStep);

        PurchaseStep promoStep = new ApplyPromotionsStep(promotionClient);
        promoStep.setNext(reserveStep);

        return promoStep;
    }

    public void buyTickets(PurchaseTicketRequest request, StreamObserver<TicketConfirm> responseObserver) {
        trainClient.getTrain(request.getTrainId(), new StreamObserver<>() {
            @Override
            public void onNext(TrainResponse trainResponse) {
                try {
                    ClassSeats classSeat = trainResponse.getSeatsList().stream()
                            .filter(sc -> sc.getServiceClass().getName().equals(request.getServiceClass()))
                            .findAny()
                            .orElseThrow(() -> new NoSuchElementException("Service class not available for this train"));

                    TravelSolution solution = solutionFactory.buildFrom(
                            trainResponse,
                            request.getDeparture(),
                            request.getArrival(),
                            request.getUsername(),
                            request.getTicketCount(),
                            List.of(classSeat.getServiceClass())
                    );
                    PurchaseContext context = new PurchaseContext(solution, request.getCreditCard(), classSeat.getCount());

                    purchaseChain.execute(context)
                            .thenAccept(finalContext -> {
                                responseObserver.onNext(finalContext.getFinalConfirmation());
                                responseObserver.onCompleted();
                            })
                            .exceptionally(error -> {
                                responseObserver.onError(error);
                                return null;
                            });

                } catch (Exception e) {
                    responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
                }
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {}
        });
    }
}
