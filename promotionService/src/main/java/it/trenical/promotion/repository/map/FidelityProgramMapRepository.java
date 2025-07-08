package it.trenical.promotion.repository.map;

import it.trenical.promotion.repository.FidelityProgramRepository;
import java.util.Map;

public class FidelityProgramMapRepository implements FidelityProgramRepository {

    private final Map<String, Boolean> map;

    public FidelityProgramMapRepository(Map<String, Boolean> map) {
        this.map = map;
    }

    @Override
    public boolean isFidelityUser(String username) {
        return map.get(username) != null;
    }

    @Override
    public boolean subscribeToProgram(String username) {
        return map.putIfAbsent(username, true) != null;
    }

    @Override
    public boolean unsubscribeFromProgram(String username) {
        return map.remove(username);
    }
}
