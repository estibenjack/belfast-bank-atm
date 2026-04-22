package com.belfastbank.service;

import com.belfastbank.exception.InvalidTransactionException;
import com.belfastbank.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    public void withdraw(Account account, BigDecimal amount) {
        // 1. check account isn't null, isn't inactive
        validateAccount(account);

        // 2. call the specific withdraw method for the account type (savingsaccount, studentaccount etc)
       account.withdraw(amount);
    }

    public BigDecimal getBalance(Account account) {
        // 1. check account isn't null, isn't inactive
        validateAccount(account);

        // 2. return the balance
        return account.getBalance();
    }

    public void deposit(Account account, BigDecimal amount) {
        validateAccount(account);
        account.deposit(amount);
    }

    public List<Transaction> getMiniStatement(Account account) {
        validateAccount(account);
        List<Transaction> history = account.getTransactionHistory();
        List<Transaction> lastFive = new ArrayList<>();

        int startIndex = history.size() - 5;
        if (startIndex < 0) {
            startIndex = 0;
        }

        for (int i = startIndex; i < history.size(); i++) {
            lastFive.add(history.get(i));
        }

        return lastFive;
    }

    private void validateAccount(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null");
        }
        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidTransactionException("Account is not active");
        }
    }

}
