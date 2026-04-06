package com.belfastbank.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.belfastbank.exception.InvalidTransactionException;

/**
 * Represents an immutable record of a single financial transaction.
 * Once created, a transaction can't be modified.
 */
public class Transaction {
    private final TransactionType transactionType;
    private final BigDecimal amount;
    private final LocalDateTime timestamp;
    private final UUID transactionId;


    /**
     * Creates a new Transaction. Timestamp and ID are generated automatically.
     *
     * @param transactionType - the type of transaction
     * @param amount          - the monetary value, must be positive
     */
    public Transaction(TransactionType transactionType, BigDecimal amount) {
        if (transactionType == null) {
            throw new InvalidTransactionException("Transaction type cannot be null");
        }
        this.transactionType = transactionType;
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Transaction amount must be greater than zero");
        }
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.transactionId = UUID.randomUUID();
    }

    // getters
    public TransactionType getTransactionType(){
        return transactionType;
    }

    public String getTransactionTypeStr() {
        return transactionType.getDisplayName();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionType=" + transactionType +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                ", transactionId=" + transactionId +
                '}';
    }
}
