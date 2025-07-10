package it.trenical.payment.service;

import io.grpc.stub.StreamObserver;
import it.trenical.payment.models.Transaction;
import it.trenical.payment.proto.MakePaymentRequest;
import it.trenical.payment.proto.PaymentResponse;
import it.trenical.payment.proto.PaymentServiceGrpc;
import it.trenical.payment.repository.TransactionRepository;

import java.time.Instant;

public class PaymentService extends PaymentServiceGrpc.PaymentServiceImplBase {

    private final TransactionRepository transactionRepository;

    public PaymentService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void makePayment(MakePaymentRequest request, StreamObserver<PaymentResponse> responseObserver) {
        Transaction t = new Transaction(
                null,
                request.getUserId(),
                request.getCreditCardInfo(),
                request.getAmount(),
                false,
                Instant.now().getEpochSecond()
        );
        Transaction result = transactionRepository.save(t);
        if (result == null)
            responseObserver.onNext(PaymentResponse.newBuilder().setSuccess(false).build());
        else
            responseObserver.onNext(PaymentResponse.newBuilder()
                        .setSuccess(true)
                        .setTransactionId(t.id()).build());
        responseObserver.onCompleted();
    }
}
