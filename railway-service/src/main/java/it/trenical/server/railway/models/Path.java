package it.trenical.server.railway.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Path implements Iterable<Path.Item> {
    private final int id;
    private final List<Item> stops;

    private Path(int id, List<Item> stops) {
        this.id = id;
        this.stops = List.copyOf(stops);
    }

    public int getId() {
        return id;
    }

    public List<Item> getStops() {
        return stops;
    }

    @Override
    public Iterator<Item> iterator() {
        return stops.iterator();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Item {
        private final Station station;
        private final double distance;

        private Item(Station station, double distance) {
            this.station = station;
            this.distance = distance;
        }
        public Station getStation() {
            return station;
        }
        public double getDistance() {
            return distance;
        }
    }

    public static class Builder {
        private Integer id = null;
        private final List<Item> items = new ArrayList<>();

        private Builder() {}

        public Builder addLast(Station station, double distance) {
            for (Item item : items) {
                if (item.getStation().name().equals(station.name()))
                    throw new IllegalArgumentException("Cannot generate a cyclic path!");
            }
            items.add(new Item(station, distance));
            return this;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Path build() {
            if (id == null)
                throw new IllegalArgumentException("Cannot generate an unregistered path!");
            if (items.size() < 2)
                throw new IllegalArgumentException("Cannot generate an empty path!");
            return new Path(id, items);
        }
    }

}
