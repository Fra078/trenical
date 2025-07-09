package it.trenical.server.gateway.services;

import io.grpc.stub.StreamObserver;
import it.trenical.server.gateway.client.GrpcClientManager;
import it.trenical.server.gateway.interceptors.JwtServerInterceptor;
import it.trenical.server.gateway.proto.TrenicalGatewayGrpc;
import it.trenical.ticketry.proto.TicketryServiceGrpc;
import it.trenical.ticketry.proto.TripQueryParams;
import it.trenical.travel.proto.TravelSolution;

public class TrenicalGateway extends TrenicalGatewayGrpc.TrenicalGatewayImplBase {

    private final TicketryServiceGrpc.TicketryServiceStub ticketryService;

    public TrenicalGateway(GrpcClientManager clientManager) {
        this.ticketryService = clientManager.getTicketryStub();
    }

    @Override
    public void queryTravelSolutions(TripQueryParams request, StreamObserver<TravelSolution> responseObserver) {
        TripQueryParams.Builder builder = TripQueryParams.newBuilder(request)
                .setUsername(JwtServerInterceptor.USER_ID.get());
        ticketryService.getTripSolutions(builder.build(), responseObserver);
    }
}
