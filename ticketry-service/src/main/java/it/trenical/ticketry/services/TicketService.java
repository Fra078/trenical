package it.trenical.ticketry.services;

import io.grpc.stub.StreamObserver;
import it.trenical.ticketry.proto.TicketryServiceGrpc;
import it.trenical.ticketry.proto.TripQueryParams;
import it.trenical.ticketry.proto.TripSolution;

public class TicketService extends TicketryServiceGrpc.TicketryServiceImplBase {

    public TicketService() {

    }

    @Override
    public void getTripSolutions(TripQueryParams request, StreamObserver<TripSolution> responseObserver) {

    }
}
