package it.trenical.promotion.repository.map;

import it.trenical.promotion.repository.LoyaltyRepository;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

public class LoyaltyMapRepository implements LoyaltyRepository {

    private final Map<String, Long> map;

    public LoyaltyMapRepository(Map<String, Long> map) {
        this.map = map;
    }

    @Override
    public Optional<Long> getSubscriptionDate(String username) {
        return Optional.ofNullable(map.get(username));
    }

    @Override
    public boolean subscribeToProgram(String username) {
        return map.putIfAbsent(username, Instant.now().getEpochSecond()) == null;
    }

    @Override
    public boolean unsubscribeFromProgram(String username) {
        return map.remove(username) != null;
    }
}
