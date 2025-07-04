package it.trenical.user.password;

public class PlainPasswordStrategy implements HashPasswordStrategy{
    @Override
    public String hashPassword(String plainPassword) {
        return plainPassword;
    }

    @Override
    public boolean verify(String plainPassword, String hashedPassword) {
        return plainPassword.equals(hashedPassword);
    }

    @Override
    public String[] getSupportedPrefixes() {
        return new String[]{""};
    }
}
