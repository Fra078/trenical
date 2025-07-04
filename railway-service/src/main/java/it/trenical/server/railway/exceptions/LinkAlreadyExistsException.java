package it.trenical.server.railway.exceptions;

public class LinkAlreadyExistsException extends RuntimeException {
    public LinkAlreadyExistsException(String station1, String station2) {
        super("Link already exists: " + station1 + " and " + station2);
    }
}
