package it.trenical.server.railway.exceptions;

public class LinkNotFoundException extends RuntimeException {
    public LinkNotFoundException(String station1, String station2) {
        super("Link not found: " + station1 + " and " + station2);
    }
}
