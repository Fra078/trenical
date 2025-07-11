package it.trenical.ticketry.test;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import it.trenical.proto.train.ClassSeats;
import it.trenical.proto.train.ServiceClass;
import it.trenical.proto.train.TrainResponse;
import it.trenical.server.database.DatabaseManager;
import it.trenical.ticketry.managers.PurchaseManager;
import it.trenical.ticketry.managers.TravelSolutionFactory;
import it.trenical.ticketry.models.Ticket;
import it.trenical.ticketry.proto.PurchaseTicketRequest;
import it.trenical.ticketry.proto.TicketConfirm;
import it.trenical.ticketry.repositories.TicketRepository;
import it.trenical.ticketry.repositories.jdbc.TicketJdbcRepository;
import it.trenical.ticketry.strategy.LinearPriceCalculationStrategy;
import it.trenical.ticketry.test.purchase.MockPaymentClient;
import it.trenical.ticketry.test.purchase.MockPromotionClient;
import it.trenical.ticketry.test.purchase.MockTrainClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseManagerTest {

    private DatabaseManager dbManager;
    private TicketRepository ticketRepository;

    private MockTrainClient fakeTrainClient;
    private MockPromotionClient fakePromotionClient;
    private MockPaymentClient fakePaymentClient;
    private TravelSolutionFactory travelSolutionFactory;
    private FakeResponseObserver fakeResponseObserver;

    private PurchaseManager purchaseManager;

    @BeforeEach
    void setUp() {
        dbManager = new DatabaseManager("mem:testdb;DB_CLOSE_DELAY=-1");
        ticketRepository = new TicketJdbcRepository(dbManager);

        fakeTrainClient = new MockTrainClient();
        fakePromotionClient = new MockPromotionClient();
        fakePaymentClient = new MockPaymentClient();
        travelSolutionFactory = new TravelSolutionFactory(new LinearPriceCalculationStrategy());
        fakeResponseObserver = new FakeResponseObserver();

        purchaseManager = new PurchaseManager(
                fakeTrainClient,
                ticketRepository,
                fakePromotionClient,
                fakePaymentClient,
                travelSolutionFactory
        );
    }

    @AfterEach
    void clearDb() throws SQLException {
        try (Connection conn = dbManager.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM Ticket");
        List<Ticket> tickets = ticketRepository.findAll();
        System.out.println(tickets);
        }
    }

    @Test
    void buyTicketsSuccessful() throws InterruptedException {
        PurchaseTicketRequest request = createPurchaseRequest();
        fakeTrainClient.setTrainToReturn(createFakeTrainResponse());
        fakePaymentClient.paymentSuccess = true;
        fakePromotionClient.shouldFail = false;

        purchaseManager.buyTickets(request, fakeResponseObserver);

        Thread.sleep(1000);

        TicketConfirm finalConfirmation = fakeResponseObserver.confirmation;
        assertNotNull(finalConfirmation);
        assertFalse(finalConfirmation.getTicketIdList().isEmpty());

        for (int id: finalConfirmation.getTicketIdList()){
            Optional<Ticket> savedTicket = ticketRepository.findById(finalConfirmation.getTrainId(), id);
            assertTrue(savedTicket.isPresent(), "Il biglietto dovrebbe essere salvato nel database");
            assertEquals(Ticket.Status.CONFIRMED, savedTicket.get().getStatus(), "Lo stato del biglietto dovrebbe essere CONFIRMED");
        }
    }

    @Test
    void buyTicketsPaymentFails() throws InterruptedException {
        PurchaseTicketRequest request = createPurchaseRequest();
        fakeTrainClient.setTrainToReturn(createFakeTrainResponse());
        fakePaymentClient.paymentSuccess = false;
        fakePromotionClient.shouldFail = false;
        purchaseManager.buyTickets(request, fakeResponseObserver);

        Thread.sleep(1000);
        Throwable error = fakeResponseObserver.error;
        assertNotNull(error);
        Optional<Ticket> savedTicket = ticketRepository.findById(1, 1);
        assertFalse(savedTicket.isPresent(), "Nessun biglietto dovrebbe essere presente nel database dopo un pagamento fallito.");
    }

    private PurchaseTicketRequest createPurchaseRequest() {
        return PurchaseTicketRequest.newBuilder()
                .setTrainId(1)
                .setUsername("testUser")
                .setDeparture("Milano")
                .setArrival("Roma")
                .setServiceClass("Standard")
                .setTicketCount(1)
                .setCreditCard("1234-5678-9012-3456")
                .build();
    }

    private TrainResponse createFakeTrainResponse() {
        ServiceClass sc = ServiceClass.newBuilder().setName("Standard").setIncrementFactor(1.0).build();
        ClassSeats cs = ClassSeats.newBuilder().setServiceClass(sc).setCount(100).build();
        return TrainResponse.newBuilder().setId(1).setName("FakeTrain").addSeats(cs).build();
    }

    static class FakeResponseObserver implements StreamObserver<TicketConfirm> {
        TicketConfirm confirmation = null;
        Throwable error = null;

        @Override public void onNext(TicketConfirm value) { confirmation = value; }
        @Override public void onError(Throwable t) { error=t; }
        @Override public void onCompleted() {  }
    }

}
