package it.trenical.server.railway.exceptions;

public class StationExistsException extends Exception {
    public StationExistsException(String stationName) {
        super(String.format("Station %s already exists", stationName));
    }
}
