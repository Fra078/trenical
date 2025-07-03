package it.trenical.user.repository;

import it.trenical.user.models.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findByUsername(String username);
}
