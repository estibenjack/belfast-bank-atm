package com.belfastbank.service;

import com.belfastbank.model.Customer;

import java.util.HashMap;
import java.util.Map;

public class CustomerService {

    private Map<String, Customer> customers = new HashMap<>();
    private Map<String, Integer> failedAttempts = new HashMap<>();

    // registerCustomer
    public void registerCustomer(Customer customer) {
        if (customer==null) {
            throw new IllegalArgumentException("Customer cannot be null");
        }
        if (!customers.containsKey(customer.getCardNo())) {
            customers.put(customer.getCardNo(), customer);
        }
    }

    // findByCardNumber
    public Customer findByCardNumber(String cardNo) {
        if (customers.containsKey(cardNo)) {
         return customers.get(cardNo);
        } else {
            throw new IllegalArgumentException("Customer doesn't exist");
        }
    }

    // validatePin
    // call customer.isCardBlocked() to check before pin validation
    // then call customer.blockCard() when failed attempts hits 3
    public boolean validatePin(String cardNo, String pin) {
        Customer customer = findByCardNumber(cardNo);
        if (customer.isCardBlocked()) {
            return false;
        }
        boolean pinVal = customer.validatePin(pin);
        if (!pinVal) {
            if (!failedAttempts.containsKey(cardNo)) {
                failedAttempts.put(cardNo, 1);
                return false;
            } else {
                if (failedAttempts.get(cardNo) == 3) {
                    customer.blockCard();
                    return false;
                } else {
                    failedAttempts.put(cardNo, failedAttempts.get(cardNo) + 1);
                    return false;
                }
            }
        }
        failedAttempts.remove(cardNo);
        return true;
    }
}
