package it.trenical.ticketry.managers;

import com.google.protobuf.Any;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import it.trenical.payment.proto.MakePaymentRequest;
import it.trenical.proto.railway.StopResponse;
import it.trenical.proto.train.ClassSeats;
import it.trenical.proto.train.TrainResponse;
import it.trenical.ticketry.clients.PaymentClient;
import it.trenical.ticketry.clients.PromotionClient;
import it.trenical.ticketry.clients.TrainClient;
import it.trenical.ticketry.models.Ticket;
import it.trenical.ticketry.proto.PurchaseTicketRequest;
import it.trenical.ticketry.proto.TicketConfirm;
import it.trenical.ticketry.repositories.TicketRepository;
import it.trenical.ticketry.services.TicketService;
import it.trenical.travel.proto.TravelSolution;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PurchaseManager {

    private final TravelSolutionFactory solutionFactory;
    private final TrainClient trainClient;
    private final PromotionClient promotionClient;
    private final TicketRepository ticketRepository;
    private final PaymentClient paymentClient;

    public PurchaseManager(
            TravelSolutionFactory solutionFactory,
            TrainClient trainClient,
            PromotionClient promotionClient,
            TicketRepository ticketRepository,
            PaymentClient paymentClient
    ) {
        this.trainClient = trainClient;
        this.solutionFactory = solutionFactory;
        this.promotionClient = promotionClient;
        this.ticketRepository = ticketRepository;
        this.paymentClient = paymentClient;
    }

    public void buyTickets(PurchaseTicketRequest request, StreamObserver<TicketConfirm> responseObserver) {
        trainClient.getTrain(request.getTrainId(), new StreamObserver<>() {
            @Override
            public void onNext(TrainResponse trainResponse) {
                try {
                    handleTrainResponse(request, trainResponse, response->{
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    });
                } catch (StatusRuntimeException e) {
                    responseObserver.onError(e);
                }
            }
            @Override
            public void onError(Throwable throwable) {responseObserver.onError(throwable);}
            @Override
            public void onCompleted() {}
        });
    }

    private void handleTrainResponse(PurchaseTicketRequest request, TrainResponse trainResponse, Consumer<TicketConfirm> consumer) {
        List<StopResponse> pr = trainResponse.getPath().getStopsList();

        boolean foundDeparture = false, foundArrival = false;
        for (StopResponse stop : pr) {
            if (stop.getStation().getName().equals(request.getDeparture())) {
                foundDeparture = true;
            } else if (stop.getStation().getName().equals(request.getArrival())) {
                foundArrival = true;
                break;
            }
        }
        if (!foundDeparture || !foundArrival) {
            throw Status.INVALID_ARGUMENT.withDescription("No departure or arrival found").asRuntimeException();
        }

        ClassSeats classSeat = trainResponse.getSeatsList().stream()
                .filter(sc-> sc.getServiceClass().getName().equals(request.getServiceClass()))
                .findAny()
                .orElseThrow(() -> Status.INVALID_ARGUMENT.withDescription("No service class found for this train").asRuntimeException());


        TravelSolution solution = solutionFactory.buildFrom(
                trainResponse, request.getDeparture(), request.getArrival(),
                request.getUsername(), request.getTicketCount(), List.of(classSeat.getServiceClass()));

        promotionClient.applyPromotions(solution)
                .thenAccept(response -> purchaseSolution(response.getSolution(), classSeat.getCount(), request.getCreditCard()))
                .exceptionally((t)->{purchaseSolution(solution, classSeat.getCount(), request.getCreditCard());return null;});
    }

    private void purchaseSolution(TravelSolution solution, int maxClassCount, String creditCard) {

        TravelSolution.Mode mode = solution.getModes(0);
        double price = mode.hasPromo() ? mode.getPromo().getFinalPrice() : mode.getPrice();

        Ticket prototype = Ticket.newBuilder()
                .trainId(solution.getTrainId())
                .customerId(solution.getUserId())
                .className(solution.getModes(0).getServiceClass().getName())
                .departure(solution.getRouteInfo().getDepartureStation())
                .status(Ticket.Status.WAITING)
                .arrival(solution.getRouteInfo().getArrivalStation())
                .build();

        List<Ticket> tickets = ticketRepository.addTicketIfPossible(prototype, solution.getTicketCount(), maxClassCount);
        MakePaymentRequest paymentRequest = MakePaymentRequest.newBuilder()
                .setAmount(price)
                .setCreditCardInfo(creditCard)
                .build();
        paymentClient.makePayment(paymentRequest)
                .thenAccept(response -> {
                    ticketRepository.removeTicketsById(tickets.stream().map(Ticket::getId).collect(Collectors.toList()));

                })
                .exceptionally(t-> {
                        ticketRepository.removeTicketsById(tickets.stream().map(Ticket::getId).toList());
                        return null;
                });

    }
}
