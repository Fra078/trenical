package it.trenical.user.password;

public interface HashPasswordStrategy {
    String hashPassword(String plainPassword);
    boolean verify(String plainPassword, String hashedPassword);
    String[] getSupportedPrefixes();
}
