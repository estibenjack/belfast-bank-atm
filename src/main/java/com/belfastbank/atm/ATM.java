package com.belfastbank.atm;

import com.belfastbank.model.Account;
import com.belfastbank.model.Customer;
import com.belfastbank.service.AccountService;
import com.belfastbank.service.CustomerService;

import java.util.List;
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
        List<Account> availableAccounts = loggedInCustomer.getAccounts();
        if (availableAccounts.size()==1) {
            this.selectedAccount=availableAccounts.getFirst();
        } else {
            printHeading("Select Account");
            for (int i=0; i<availableAccounts.size(); i++){
                System.out.printf("%d. %s (****%s)%n", i+1, availableAccounts.get(i).getAccountType(), availableAccounts.get(i).getAccountNo().substring(4));
            }
            System.out.print("\nPlease select: ");

            int choice=-1;
            while (true) {
                try {
                    choice = Integer.parseInt(scanner.nextLine().trim());
                    if (choice >= 1 && choice <= availableAccounts.size()) {
                        this.selectedAccount = availableAccounts.get(choice - 1);
                        break;
                    } else {
                        System.out.print("Invalid selection. Please try again: ");
                    }
                } catch (NumberFormatException e) {
                    System.out.print("Invalid selection. Please try again: ");
                }
            }
        }
        showMenu();
    }

    public void showMenu(){
        int choice=-1;
        while (true) {
            printHeading("Belfast Bank ATM");
            System.out.println("1. Check Balance");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Mini Statement");
            System.out.println("5. Exit");
            printDivider();
            System.out.print("\nPlease select: ");
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= 5) {
                   switch (choice) {
                       case 1:
                           handleBalance();
                           break;
                       case 2:
                           handleWithdraw();
                           break;
                       case 3:
                           handleDeposit();
                           break;
                       case 4:
                           handleMiniStatement();
                           break;
                       case 5:
                           System.out.println("Thank you for using Belfast Bank. Goodbye!");
                           return;
                       default:
                           break;
                   }
                } else {
                    System.out.print("Invalid selection. Please try again: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid selection. Please try again: ");
            }
        }

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
