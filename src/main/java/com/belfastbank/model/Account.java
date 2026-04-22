package com.belfastbank.model;

import com.belfastbank.exception.InvalidTransactionException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Abstract class representing a belfast bank account.
 * Provides common functionality for all account types including:
 * deposit, balance management and transaction history
 * Subclasses must implement {@link #withdraw(BigDecimal)} according
 * to their own withdrawal rules
 */
public abstract class Account {
    private String accountNo;
    private BigDecimal balance;
    // TODO: change to Customer once Customer class implemented
    private String owner;
    private List<Transaction> transactions;
    private AccountStatus accountStatus;

    // constructor

    /**
     * Creates a new Account with ACTIVE status
     * Account number is automatically generated
     *
     * @param balance - the opening balance, must be zero or greater
     * @param owner   - the name of the account holder
     * @throws IllegalArgumentException if balance is negative or owner is null.empty
     */
    public Account(BigDecimal balance, String owner) {
        this.accountNo = String.format("%08d", new Random().nextInt(100000000));
        if (balance == null || balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        } else {
            this.balance = balance;
        }
        if (owner == null || owner.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner name cannot be empty");
        } else {
            this.owner = owner.trim();
        }
        this.transactions = new ArrayList<>();
        this.accountStatus = AccountStatus.ACTIVE;
    }

    // deposit

    /**
     * Deposits funds into the account and records transaction
     *
     * @param amount - the amount to deposit, must be greater than zero
     * @throws InvalidTransactionException - if amount is null or negative
     */
    public void deposit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Deposit amount must be greater than zero");
        } else {
            balance = balance.add(amount);
            transactions.add(new Transaction(TransactionType.DEPOSIT, amount));
        }
    }

    // withdraw

    /**
     * Withdraws funds from the account
     * Implentation varies by account type
     *
     * @param amount - the amount to withdraw, must be greater than zero
     */
    public abstract void withdraw(BigDecimal amount);
    public abstract String getAccountType();

    // get account no.
    public String getAccountNo() {
        return accountNo;
    }

    // getBalance
    public BigDecimal getBalance() {
        return balance;
    }

    // get owner
    public String getOwner() {
        return owner;
    }

    // get account status
    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    // get transaction history
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }

    // set balance (protected method)

    /**
     * Used by subclasses to update the balance after a withdrawal.
     */
    protected void setBalance(BigDecimal newBalance) {
        this.balance = newBalance;
    }

    protected void recordTransaction(TransactionType type, BigDecimal amount) {
        transactions.add(new Transaction(type, amount));
    }
}
