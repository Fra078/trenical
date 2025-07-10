package it.trenical.ticketry.clients;

import it.trenical.payment.proto.MakePaymentRequest;
import it.trenical.payment.proto.PaymentResponse;

import java.util.concurrent.CompletableFuture;

public interface PaymentClient {
    CompletableFuture<PaymentResponse> makePayment(MakePaymentRequest request);
}
