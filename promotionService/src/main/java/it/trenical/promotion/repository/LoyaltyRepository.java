package it.trenical.promotion.repository;

import java.util.Optional;

public interface LoyaltyRepository {
    default boolean isFidelityUser(String username){
        return getSubscriptionDate(username).isPresent();
    }

    Optional<Long> getSubscriptionDate(String username);

    boolean subscribeToProgram(String username);
    boolean unsubscribeFromProgram(String username);
}
