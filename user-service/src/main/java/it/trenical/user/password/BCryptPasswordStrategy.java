package it.trenical.user.password;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordStrategy implements HashPasswordStrategy {

    private final int logRounds = 12; // Fattore di lavoro per bCrypt

    @Override
    public String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(logRounds));
    }

    @Override
    public boolean verify(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    @Override
    public String[] getSupportedPrefixes() {
        return new String[]{"$2a$", "$2y$", "$2b$"};
    }
}