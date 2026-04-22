package com.belfastbank.atm;

import com.belfastbank.model.Account;
import com.belfastbank.model.Customer;
import com.belfastbank.service.AccountService;
import com.belfastbank.service.CustomerService;

import java.util.Scanner;

public class ATM {
    private AccountService accountService;
    private CustomerService customerService;
    private Scanner scanner = new Scanner(System.in);
    private Customer loggedInCustomer;
    private Account selectedAccount;

    public ATM(AccountService accountService, CustomerService customerService) {
        this.accountService = accountService;
        this.customerService = customerService;
    }

    public void start(){
        while(true) {
            login();
        }
    }

    public void login() {
        printHeading("Welcome to Belfast Bank");

        String cardInput;
        while (true) {
            System.out.print("Please enter your card number: ");
            cardInput = scanner.nextLine().trim();
            if (cardInput.length() == 8 && cardInput.matches("\\d+")) {
                break;
            }
            System.out.println("Invalid card number. Please enter 8 digits.");
        }

        int attempts=0;
        String pinInput;
        while (attempts<3) {
            System.out.print("Please enter your PIN: ");
            pinInput = scanner.nextLine().trim();
            if (!pinInput.matches("\\d{4}")) {
                System.out.println("Invalid PIN format. Please enter 4 digits.");
                continue;
            }
            try {
                boolean valid = customerService.validatePin(cardInput, pinInput);
                if (valid) {
                    loggedInCustomer = customerService.findByCardNumber(cardInput);
                    selectAccount();
                    return;
                } else {
                    Customer customer = customerService.findByCardNumber(cardInput);
                    if (customer.isCardBlocked()) {
                        System.out.println("Your card has been blocked. Please contact Belfast Bank.");
                        return;
                    }
                    attempts++;
                    System.out.println("Incorrect PIN. Attempts remaining: " + (3 - attempts));
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Card not recognised.");
                return;
            }
        }
    }

    public void selectAccount() {

    }

    public void showMenu(){

    }

    private void handleBalance(){}
    private void handleWithdraw(){}
    private void handleDeposit(){}
    private void handleMiniStatement(){}

    private void printHeading(String title) {
        printDivider();
        System.out.println("         "+title);
        printDivider();
    }

    private void printDivider() {
        System.out.println("=========================================");
    }

}
