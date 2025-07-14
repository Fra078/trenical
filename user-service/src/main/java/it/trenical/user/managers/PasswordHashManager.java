package it.trenical.user.managers;

import it.trenical.user.password.HashPasswordStrategy;

import java.util.List;

public class PasswordHashManager {


    private final HashPasswordStrategy preferredHastStrategy;
    private final List<HashPasswordStrategy> supportedHastStrategies;

    public PasswordHashManager(HashPasswordStrategy preferredHastStrategy, List<HashPasswordStrategy> supportedHastStrategies) {
        this.preferredHastStrategy = preferredHastStrategy;
        this.supportedHastStrategies = supportedHastStrategies;
    }

    public String getHash(String plainPassword){
        return preferredHastStrategy.hashPassword(plainPassword);
    }
    public boolean verify(String plainPassword, String hashedPassword){
        for (HashPasswordStrategy strategy : supportedHastStrategies) {
            for (String prefix : strategy.getSupportedPrefixes()) {
                if (hashedPassword.startsWith(prefix)) {
                    return strategy.verify(plainPassword, hashedPassword);
                }
            }
        }
        throw new RuntimeException("Cannot find an algorithm to verify this password");
    }


}
