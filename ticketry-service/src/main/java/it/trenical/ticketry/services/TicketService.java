package it.trenical.ticketry.services;

import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import it.trenical.ticketry.managers.PurchaseManager;
import it.trenical.ticketry.managers.TrainUpdateBroadcast;
import it.trenical.ticketry.managers.TripManager;
import it.trenical.ticketry.proto.*;
import it.trenical.ticketry.validator.GrpcInputValidator;
import it.trenical.train.proto.TrainUpdate;
import it.trenical.travel.proto.TravelSolution;

public class TicketService extends TicketryServiceGrpc.TicketryServiceImplBase {

    private final TripManager tripManager;
    private final PurchaseManager purchaseManager;
    private final TrainUpdateBroadcast updateBroadcast;

    public TicketService(
            TripManager tripManager,
            PurchaseManager purchaseManager,
            TrainUpdateBroadcast updateBroadcast
    ) {
        this.tripManager = tripManager;
        this.purchaseManager = purchaseManager;
        this.updateBroadcast = updateBroadcast;
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

    @Override
    public void listenToMyTrains(ListenToMyTrainsRequest request, StreamObserver<TrainUpdate> responseObserver) {
        updateBroadcast.subscribe(request.getUsername(), responseObserver::onNext);
    }
}
