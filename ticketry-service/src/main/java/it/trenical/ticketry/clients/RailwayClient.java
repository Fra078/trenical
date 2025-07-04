package it.trenical.ticketry.clients;

import java.util.List;

public interface RailwayClient {
    List<Integer> findPaths(String start, String end);
}
