package it.trenical.trainmanager.strategy;

import java.util.Map;

public interface PlatformAssignmentStrategy {
    Map<String, Integer> assignPlatforms(Map<String, Integer> stationPlatformLimits,
                                         Map<String, Integer> requestedChoices);
}

