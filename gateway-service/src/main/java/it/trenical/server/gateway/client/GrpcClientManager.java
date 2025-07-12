package it.trenical.server.gateway.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.promotion.proto.PromotionServiceGrpc;
import it.trenical.proto.railway.RailwayServiceGrpc;
import it.trenical.proto.train.TrainManagerGrpc;
import it.trenical.ticketry.proto.TicketryServiceGrpc;
import it.trenical.user.proto.UserServiceGrpc;

public class GrpcClientManager {

    private final ManagedChannel userChannel;
    private final UserServiceGrpc.UserServiceStub userStub;

    private final ManagedChannel ticketryChannel;
    private final TicketryServiceGrpc.TicketryServiceStub ticketryStub;

    private final ManagedChannel trainChannel;
    private final TrainManagerGrpc.TrainManagerStub trainStub;

    private final ManagedChannel promotionChannel;
    private final PromotionServiceGrpc.PromotionServiceStub promotionStub;

    private final ManagedChannel railwayChannel;
    private final RailwayServiceGrpc.RailwayServiceStub railwayStub;

    public GrpcClientManager() {
        userChannel = createLocalChannel(5060);
        userStub = UserServiceGrpc.newStub(userChannel);

        ticketryChannel = createLocalChannel(8778);
        ticketryStub = TicketryServiceGrpc.newStub(ticketryChannel);

        trainChannel = createLocalChannel(5051);
        trainStub = TrainManagerGrpc.newStub(trainChannel);

        promotionChannel = createLocalChannel(5606);
        promotionStub = PromotionServiceGrpc.newStub(promotionChannel);

        railwayChannel = createLocalChannel(5050);
        railwayStub = RailwayServiceGrpc.newStub(railwayChannel);
    }

    private ManagedChannel createLocalChannel(int port) {
        return ManagedChannelBuilder.forAddress("localhost", port).usePlaintext().build();
    }

    public UserServiceGrpc.UserServiceStub getUserStub() {
        return userStub;
    }

    public PromotionServiceGrpc.PromotionServiceStub getPromotionStub() {
        return promotionStub;
    }

    public TicketryServiceGrpc.TicketryServiceStub getTicketryStub() {
        return ticketryStub;
    }

    public TrainManagerGrpc.TrainManagerStub getTrainStub() {
        return trainStub;
    }

    public RailwayServiceGrpc.RailwayServiceStub getRailwayStub() {return railwayStub;}
}
