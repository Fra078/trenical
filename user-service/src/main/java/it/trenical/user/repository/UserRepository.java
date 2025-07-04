package it.trenical.user.repository;

import it.trenical.user.exceptions.UserAlreadyExistsException;
import it.trenical.user.models.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user) throws UserAlreadyExistsException;
    Optional<User> findByUsername(String username);
}
