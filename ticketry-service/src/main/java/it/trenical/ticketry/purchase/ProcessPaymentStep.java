package it.trenical.ticketry.purchase;

import it.trenical.payment.proto.MakePaymentRequest;
import it.trenical.ticketry.clients.PaymentClient;
import it.trenical.travel.proto.TravelSolution;

import java.util.concurrent.CompletableFuture;

public class ProcessPaymentStep implements PurchaseStep {
    private PurchaseStep next;
    private final PaymentClient paymentClient;
    private final PurchaseStep errorHandler; // In caso di fallimento

    public ProcessPaymentStep(PaymentClient paymentClient, PurchaseStep errorHandler) {
        this.paymentClient = paymentClient;
        this.errorHandler = errorHandler;
    }

    @Override
    public void setNext(PurchaseStep next) {
        this.next = next;
    }

    @Override
    public CompletableFuture<PurchaseContext> execute(PurchaseContext context) {
        TravelSolution.Mode mode = context.getProcessedSolution().getModes(0);
        double price = mode.hasPromo() ? mode.getPromo().getFinalPrice() : mode.getPrice();
        MakePaymentRequest request = MakePaymentRequest.newBuilder()
                .setAmount(price)
                .setCreditCardInfo(context.getCreditCard())
                .build();

        return paymentClient.makePayment(request)
                .thenCompose(response -> {
                    if (response.getSuccess()) {
                        return next.execute(context);
                    } else {
                        return errorHandler.execute(context);
                    }
                })
                .exceptionallyCompose(ex -> errorHandler.execute(context));
    }
}