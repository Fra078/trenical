package it.trenical.user.models;

public record User(
        String username,
        String passwordHash,
        String firstName,
        String lastName,
        Type type
) {

    public enum Type {
        STANDARD,
        FIDELITY
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(User user) {
        return new Builder(user);
    }

    public static class Builder {
        private String username;
        private String passwordHash;
        private String firstName;
        private String lastName;
        private Type type;


        public String getUsername() {
            return username;
        }

        public String getPasswordHash() {
            return passwordHash;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public Type getType() {
            return type;
        }

        private Builder() {}

        private Builder(User user) {
            this.username = user.username;
            this.passwordHash = user.passwordHash;
            this.firstName = user.firstName;
            this.lastName = user.lastName;
            this.type = user.type;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPasswordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public Builder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public User build() {
            return new User(username, passwordHash, firstName, lastName, type);
        }


    }

}
