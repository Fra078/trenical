package it.trenical.promotion.managers;

import it.trenical.promotion.repository.FidelityProgramRepository;

import java.util.Optional;

public class FidelityProgramManager {

    private final FidelityProgramRepository repository;

    public FidelityProgramManager(FidelityProgramRepository repository) {
        this.repository = repository;
    }

    public boolean subscribeToProgram(String username){
        return repository.subscribeToProgram(username);
    }

    public boolean unsubscribeToProgram(String username){
        return repository.unsubscribeFromProgram(username);
    }

    public Optional<Long> getSubscriptionInfo(String username) {
        return repository.getSubscriptionDate(username);
    }
}
