package com.belfastbank.model;

import com.belfastbank.exception.InvalidTransactionException;

import java.math.BigDecimal;

public class StudentAccount extends Account {

    private static final BigDecimal OVERDRAFT_LIMIT = new BigDecimal("500.00");
    private static final BigDecimal MAX_SINGLE_WITHDRAWAL = new BigDecimal("200.00");

    public StudentAccount (BigDecimal balance, String owner) {
        super(balance, owner);

    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Withdrawal must be greater than zero");
        }
        if (amount.compareTo(MAX_SINGLE_WITHDRAWAL) > 0) {
            throw new InvalidTransactionException("Can't withdraw more than £200 at a time");
        }
        if (getBalance().subtract(amount).compareTo(OVERDRAFT_LIMIT.negate()) < 0) {
            throw new InvalidTransactionException("Can't exceed £500 overdraft");
        }
        setBalance(getBalance().subtract(amount));
    }
}
