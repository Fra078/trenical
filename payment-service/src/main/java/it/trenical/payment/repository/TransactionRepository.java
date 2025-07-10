package it.trenical.payment.repository;

import it.trenical.payment.models.Transaction;

import java.util.Optional;

public interface TransactionRepository {
    Transaction save(Transaction transaction);

    Optional<Transaction> findById(int id);
}
