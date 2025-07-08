package it.trenical.ticketry.services;

import io.grpc.Context;
import io.grpc.stub.StreamObserver;
import it.trenical.server.jwt.JwtServerInterceptor;
import it.trenical.ticketry.managers.TripManager;
import it.trenical.ticketry.proto.TicketryServiceGrpc;
import it.trenical.ticketry.proto.TripQueryParams;
import it.trenical.travel.proto.TravelSolution;

public class TicketService extends TicketryServiceGrpc.TicketryServiceImplBase {

    private final TripManager tripManager;

    public TicketService(TripManager tripManager) {
        this.tripManager = tripManager;
    }

    @Override
    public void getTripSolutions(TripQueryParams request, StreamObserver<TravelSolution> responseObserver) {
        String username = JwtServerInterceptor.USER_ID.get();
        tripManager.getTripSolutions(request, username, responseObserver);
    }
}
