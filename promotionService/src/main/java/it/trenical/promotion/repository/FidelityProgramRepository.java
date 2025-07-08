package it.trenical.promotion.repository;

public interface FidelityProgramRepository {

    boolean isFidelityUser(String username);

    boolean subscribeToProgram(String username);
    boolean unsubscribeFromProgram(String username);

}
