package it.trenical.user.test;

import it.trenical.user.exceptions.UserAlreadyExistsException;
import it.trenical.user.models.User;
import it.trenical.user.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository {

    private Map<String, User> users = new HashMap<>();

    @Override
    public void save(User user) throws UserAlreadyExistsException {
        if (users.containsKey(user.username())) {
            throw new UserAlreadyExistsException(user.username());
        }
        users.put(user.username(), user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (!users.containsKey(username))
            return Optional.empty();
        return Optional.of(users.get(username));
    }
}
