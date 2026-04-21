package com.belfastbank.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Customer {
    private final UUID customerId;
    private String name;
    private String pin;
    private List<Account> accounts;

    public Customer(String name, String pin) {
        if(name==null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty");
        }
        if (pin==null || !pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("PIN must be 4 digits");
        }
        this.customerId = UUID.randomUUID();
        this.name = name.trim();
        this.pin=pin;
        this.accounts = new ArrayList<>();
    }

    public boolean validatePin(String enteredPin) {
        return this.pin.equals(enteredPin);
    }

    public void addAccount(Account account) {
        if (account==null){
            throw new IllegalArgumentException("Account cannot be null");
        }
        accounts.add(account);
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }
}
