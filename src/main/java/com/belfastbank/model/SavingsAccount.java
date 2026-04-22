package com.belfastbank.model;

import com.belfastbank.exception.InvalidTransactionException;

import java.math.BigDecimal;

public class SavingsAccount extends Account {
    private BigDecimal interestRate;
    private int monthlyWithdrawalCount;
    private static final int MAX_MONTHLY_WITHDRAWALS = 3;

    public SavingsAccount(BigDecimal balance, String owner, BigDecimal interestRate) {
        super(balance, owner);
        if (interestRate==null || interestRate.compareTo(BigDecimal.ZERO) < 0){
           throw new IllegalArgumentException("Interest rate cannot be negative");
        }
        this.interestRate=interestRate;
        this.monthlyWithdrawalCount=0;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        if (amount == null|| amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Withdrawal must be greater than zero");
        }
        if (monthlyWithdrawalCount >= MAX_MONTHLY_WITHDRAWALS) {
            throw new InvalidTransactionException("Monthly withdrawal limit reached");
        }
        if (getBalance().compareTo(amount) <0){
            throw new InvalidTransactionException("Insufficient funds");
        }
        setBalance(getBalance().subtract(amount));
        recordTransaction(TransactionType.WITHDRAWAL, amount);
        monthlyWithdrawalCount++;
    }

    public void applyInterest() {
        BigDecimal interest = getBalance().multiply(interestRate);
        deposit(interest);
    }

    public void resetMonthlyWithdrawals() {
        monthlyWithdrawalCount = 0;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public int getMonthlyWithdrawalCount() {
        return monthlyWithdrawalCount;
    }

}
