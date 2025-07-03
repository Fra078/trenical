package it.trenical.trainmanager.strategy.impl;

import it.trenical.trainmanager.strategy.PlatformAssignmentStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DefaultPlatformAssignmentStrategy implements PlatformAssignmentStrategy {
    private final Random random = new Random();

    @Override
    public Map<String, Integer> assignPlatforms(Map<String, Integer> stationPlatformLimits,
                                                Map<String, Integer> requestedChoices) {
        Map<String, Integer> finalChoices = new HashMap<>();
        for (Map.Entry<String, Integer> stationEntry : stationPlatformLimits.entrySet()) {
            String stationName = stationEntry.getKey();
            int maxPlatforms = stationEntry.getValue();
            
            Integer choice = requestedChoices.get(stationName);

            if (choice != null && choice > 0 && choice <= maxPlatforms) {
                finalChoices.put(stationName, choice);
            } else {
                finalChoices.put(stationName, random.nextInt(maxPlatforms) + 1);
            }
        }
        return finalChoices;
    }
}