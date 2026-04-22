package com.belfastbank;

import com.belfastbank.atm.ATM;
import com.belfastbank.model.CurrentAccount;
import com.belfastbank.model.Customer;
import com.belfastbank.model.SavingsAccount;
import com.belfastbank.model.StudentAccount;
import com.belfastbank.service.AccountService;
import com.belfastbank.service.CustomerService;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        AccountService accountService = new AccountService();
        CustomerService customerService = new CustomerService();

        // test customer 1 - current and savings account
        Customer john = new Customer("John Smith", "1234");
        john.addAccount(new CurrentAccount(new BigDecimal("1500.00"), "John Smith", new BigDecimal("500.00")));
        john.addAccount(new SavingsAccount(new BigDecimal("3000.00"), "John Smith", new BigDecimal("0.03")));
        customerService.registerCustomer(john);

        // test customer 2 - only has student account
        Customer jane = new Customer("Jane Doe", "5678");
        jane.addAccount(new StudentAccount(new BigDecimal("250.00"), "Jane Doe"));
        customerService.registerCustomer(jane);

        // printing card numbers so I can test login
        System.out.println("TEST DATA:");
        System.out.println("John's card number: " + john.getCardNo());
        System.out.println("Jane's card number: " + jane.getCardNo());
        System.out.println();

        ATM atm = new ATM(accountService, customerService);
        atm.start();
    }
}
