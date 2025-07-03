package it.trenical.trainmanager.strategy.impl;

import it.trenical.trainmanager.strategy.PlatformAssignmentStrategy;

import java.util.Map;

public class StrictPlatformAssignmentStrategy implements PlatformAssignmentStrategy {

    @Override
    public Map<String, Integer> assignPlatforms(Map<String, Integer> stationPlatformLimits,
                                                Map<String, Integer> requestedChoices) {
        if (!stationPlatformLimits.keySet().equals(requestedChoices.keySet()))
            throw new IllegalArgumentException("Le preferenze devono riguardare tutte e solo le stazioni coinvolte!");
        for (String station : requestedChoices.keySet()) {
            int value = requestedChoices.get(station);
            int limit = stationPlatformLimits.get(station);
            if (value > limit || value <= 0)
                throw new IllegalArgumentException("La stazione " + station + " non ha il binario " + value);
        }
        return requestedChoices;
    }
}