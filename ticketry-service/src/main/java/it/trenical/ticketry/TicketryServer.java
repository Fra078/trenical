package it.trenical.ticketry;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.trenical.server.database.DatabaseManager;
import it.trenical.ticketry.clients.PaymentClient;
import it.trenical.ticketry.clients.PromotionClient;
import it.trenical.ticketry.clients.TrainClient;
import it.trenical.ticketry.clients.grpc.PaymentGrpcClient;
import it.trenical.ticketry.clients.grpc.PromotionGrpcClient;
import it.trenical.ticketry.clients.grpc.TrainGrpcClient;
import it.trenical.ticketry.managers.PurchaseManager;
import it.trenical.ticketry.managers.TripManager;
import it.trenical.ticketry.managers.TravelSolutionFactory;
import it.trenical.ticketry.repositories.TicketRepository;
import it.trenical.ticketry.repositories.jdbc.TicketJdbcRepository;
import it.trenical.ticketry.services.TicketService;
import it.trenical.ticketry.strategy.LinearPriceCalculationStrategy;
import it.trenical.ticketry.strategy.PriceCalculationStrategy;

import java.io.IOException;

public class TicketryServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        TrainClient trainClient = new TrainGrpcClient();
        PromotionClient promotionClient = PromotionGrpcClient.getInstance();
        TicketRepository ticketRepository = new TicketJdbcRepository(new DatabaseManager("./db/tickets.db"));
        PriceCalculationStrategy priceStrategy = new LinearPriceCalculationStrategy();
        TravelSolutionFactory solutionFactory = new TravelSolutionFactory(priceStrategy);
        PaymentClient paymentClient = PaymentGrpcClient.getInstance();

        TripManager tripManager = new TripManager(trainClient, ticketRepository, promotionClient, solutionFactory);
        PurchaseManager purchaseManager = new PurchaseManager(trainClient, ticketRepository, promotionClient, paymentClient, solutionFactory);

        Server server = ServerBuilder.forPort(8778)
                .addService(new TicketService(tripManager, purchaseManager))
                .build().start();
        System.out.println("Server started on port " + server.getPort());
        server.awaitTermination();
    }
}
