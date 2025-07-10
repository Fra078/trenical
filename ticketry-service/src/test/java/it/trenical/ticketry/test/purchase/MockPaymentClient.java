package it.trenical.ticketry.test.purchase;

import it.trenical.payment.proto.MakePaymentRequest;
import it.trenical.payment.proto.PaymentResponse;
import it.trenical.ticketry.clients.PaymentClient;

import java.util.concurrent.CompletableFuture;

public class MockPaymentClient implements PaymentClient {
        public boolean paymentSuccess = true;
        @Override
        public CompletableFuture<PaymentResponse> makePayment(MakePaymentRequest request) {
            return CompletableFuture.completedFuture(PaymentResponse.newBuilder().setTransactionId(101).setSuccess(paymentSuccess).build());
        }
    }
