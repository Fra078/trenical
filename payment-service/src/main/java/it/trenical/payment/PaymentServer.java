package it.trenical.payment;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import it.trenical.payment.repository.TransactionRepository;
import it.trenical.payment.repository.jdbc.TransactionJdbcRepository;
import it.trenical.payment.service.PaymentService;
import it.trenical.server.database.DatabaseManager;

import java.io.IOException;

public class PaymentServer {
    public static void main(String[] args) throws IOException, InterruptedException {

        DatabaseManager db = new DatabaseManager("./db/railway-db");
        TransactionRepository transactionRepository = new TransactionJdbcRepository(db);

        Server server = ServerBuilder.forPort(7887)
                .addService(new PaymentService(transactionRepository))
                .build().start();
        System.out.println("Server started on port " + server.getPort());
        server.awaitTermination();
    }
}
