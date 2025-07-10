package it.trenical.payment.models;

public record Transaction(Integer id, String userId, String creditCard, double amount, boolean refund, long date) {
    public Transaction setId(int id) {
        if (this.id != null) {
            throw new IllegalStateException("Transaction already has an id");
        }
        return new Transaction(id, userId, creditCard, amount, refund, date);
    }
}
