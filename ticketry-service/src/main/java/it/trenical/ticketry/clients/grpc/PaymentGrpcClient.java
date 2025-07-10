package it.trenical.ticketry.clients.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import it.trenical.payment.proto.MakePaymentRequest;
import it.trenical.payment.proto.PaymentResponse;
import it.trenical.payment.proto.PaymentServiceGrpc;
import it.trenical.server.future.FutureMapper;
import it.trenical.ticketry.clients.PaymentClient;

import java.util.concurrent.CompletableFuture;

public class PaymentGrpcClient implements PaymentClient {

    private static PaymentGrpcClient instance;
    private final ManagedChannel channel = ManagedChannelBuilder
            .forAddress("localhost", 5606)
            .usePlaintext()
            .build();
    private final PaymentServiceGrpc.PaymentServiceFutureStub stub
            = PaymentServiceGrpc.newFutureStub(channel);

    private PaymentGrpcClient() {}

    public synchronized static PaymentGrpcClient getInstance() {
        if (instance == null) {
            instance = new PaymentGrpcClient();
        }
        return instance;
    }

    @Override
    public CompletableFuture<PaymentResponse> makePayment(MakePaymentRequest request) {
        return FutureMapper.toCompletableFuture(stub.makePayment(request));
    }


}
