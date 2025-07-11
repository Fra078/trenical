package it.trenical.ticketry.services;

import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import it.trenical.ticketry.managers.PurchaseManager;
import it.trenical.ticketry.managers.TripManager;
import it.trenical.ticketry.proto.PurchaseTicketRequest;
import it.trenical.ticketry.proto.TicketConfirm;
import it.trenical.ticketry.proto.TicketryServiceGrpc;
import it.trenical.ticketry.proto.TripQueryParams;
import it.trenical.ticketry.validator.GrpcInputValidator;
import it.trenical.travel.proto.TravelSolution;

public class TicketService extends TicketryServiceGrpc.TicketryServiceImplBase {

    private final TripManager tripManager;
    private final PurchaseManager purchaseManager;

    public TicketService(TripManager tripManager, PurchaseManager purchaseManager) {
        this.tripManager = tripManager;
        this.purchaseManager = purchaseManager;
    }

    @Override
    public void getTripSolutions(TripQueryParams request, StreamObserver<TravelSolution> responseObserver) {
        tripManager.getTripSolutions(request, responseObserver);
    }

    @Override
    public void buyTicket(PurchaseTicketRequest request, StreamObserver<TicketConfirm> responseObserver) {
        try {
            GrpcInputValidator.validate(request);
            purchaseManager.buyTickets(request, responseObserver);
        } catch (StatusRuntimeException e) {
            responseObserver.onError(e);
        }
    }
}
