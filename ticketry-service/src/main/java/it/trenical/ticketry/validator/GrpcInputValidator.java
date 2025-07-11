package it.trenical.ticketry.validator;

import io.grpc.Status;
import it.trenical.ticketry.proto.PurchaseTicketRequest;

public final class GrpcInputValidator {
    private GrpcInputValidator() {
        throw new IllegalStateException("Utility class");
    }

    public static void validate(PurchaseTicketRequest request) {
        if (request.getDeparture().isEmpty() || request.getArrival().isEmpty()) {
            throw Status.INVALID_ARGUMENT.withDescription("Departure and arrival are required").asRuntimeException();
        }
        if (request.getDeparture().equals(request.getArrival()))
            throw Status.INVALID_ARGUMENT.withDescription("Departure is equal to arrival").asRuntimeException();
        if (request.getServiceClass().isBlank())
            throw Status.INVALID_ARGUMENT.withDescription("Service class is required").asRuntimeException();
        if (request.getTicketCount() <= 0)
            throw Status.INVALID_ARGUMENT.withDescription("Ticket count must be positive").asRuntimeException();
    }
}
