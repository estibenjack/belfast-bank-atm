package com.belfastbank.model;

import com.belfastbank.exception.InvalidTransactionException;

import java.math.BigDecimal;

public class CurrentAccount extends Account {

    private BigDecimal overdraftLimit;

    public CurrentAccount(BigDecimal balance, String owner, BigDecimal overdraftLimit) {
        super(balance, owner);
        if (overdraftLimit==null||overdraftLimit.compareTo(BigDecimal.ZERO)<0){
            throw new IllegalArgumentException("Overdraft limit cannot be negative");
        }
        this.overdraftLimit = overdraftLimit;
    }

    public BigDecimal getOverdraftLimit(){
        return overdraftLimit;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Withdrawal must be greater than zero");
        }
        if (getBalance().add(overdraftLimit).compareTo(amount) < 0) {
            throw new InvalidTransactionException("Exceeds overdraft limit");
        }
        setBalance(getBalance().subtract(amount));
        recordTransaction(TransactionType.WITHDRAWAL, amount);
    }

}
