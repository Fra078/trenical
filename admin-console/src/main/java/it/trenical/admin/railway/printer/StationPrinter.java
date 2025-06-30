package it.trenical.admin.railway.printer;

import it.trenical.proto.railway.StationResponse;

import java.util.List;

public class StationPrinter {
    private StationPrinter() {}

    public static void printStations(List<StationResponse> list) {
        System.out.printf("%-20s %-20s %-2s\n", "Nome", "Citta", "Binari");
        list.forEach(station -> {
            System.out.printf("%-20s %-20s %-2d\n", station.getName(), station.getCity(), station.getPlatformCount());
        });
    }
}
