package com.belfastbank.atm;

import com.belfastbank.exception.InvalidTransactionException;
import com.belfastbank.model.Account;
import com.belfastbank.model.Customer;
import com.belfastbank.model.Transaction;
import com.belfastbank.service.AccountService;
import com.belfastbank.service.CustomerService;

import java.math.BigDecimal;
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
            System.out.println();
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
            System.out.println();
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
                           if (loggedInCustomer == null) return;
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

    private void handleBalance(){
        System.out.println();
        printHeading("Account Balance");
        System.out.printf("%s (****%s)", selectedAccount.getAccountType(), selectedAccount.getAccountNo().substring(4));
        System.out.printf("%nAvailable Balance: £%.2f%n", accountService.getBalance(selectedAccount));
        printDivider();
        System.out.println("Press Enter to return to menu...");
        scanner.nextLine();
    }

    private void handleWithdraw(){
        System.out.println();
        printHeading("Withdraw");

        BigDecimal withdrawalAmount = null;
        while (true) {
            System.out.print("Enter amount to withdraw: £");
            try {
                withdrawalAmount = new BigDecimal(scanner.nextLine().trim());
                if (withdrawalAmount.compareTo(BigDecimal.ZERO)>0) {
                    break;
                }
                System.out.println("Amount must be greater than zero.");
            } catch (NumberFormatException e) {
                System.out.print("Invalid selection. Please try again: ");
            }
        }
        printProcessing();
        try {
            accountService.withdraw(selectedAccount, withdrawalAmount);
            System.out.println("Your card has been returned.");
            System.out.println("Please take your cash.");
            System.out.printf("%nGoodbye, %s!%n", loggedInCustomer.getName());
            printDivider();
            loggedInCustomer=null;
            selectedAccount=null;
        } catch (InvalidTransactionException e) {
            System.out.println("Transaction failed: " + e.getMessage());
            System.out.println("Press Enter to return to menu...");
            scanner.nextLine();
            return;
        }
    }

    private void handleDeposit(){
        System.out.println();
        printHeading("Deposit");

        BigDecimal depositAmount = null;
        while (true) {
            System.out.print("Enter amount to deposit: £");
            try {
                depositAmount = new BigDecimal(scanner.nextLine().trim());
                if (depositAmount.compareTo(BigDecimal.ZERO)>0) {
                    break;
                }
                System.out.println("Amount must be greater than zero.");
            } catch (NumberFormatException e) {
                System.out.print("Invalid selection. Please try again: ");
            }
        }
        printProcessing();
        accountService.deposit(selectedAccount, depositAmount);
        System.out.printf("£%.2f deposited successfully.", depositAmount);
        System.out.printf("%nNew balance: £%.2f%n", accountService.getBalance(selectedAccount));
        printDivider();
        System.out.print("Press Enter to return to menu...");
        scanner.nextLine();
    }

    private void handleMiniStatement(){
        System.out.println();
        printHeading("Mini Statement");
        System.out.printf("%s (****%s)", selectedAccount.getAccountType(), selectedAccount.getAccountNo().substring(4));
        System.out.println();
        System.out.printf("%s\t\t%s\t%s%n", "DATE", "TYPE", "AMOUNT");
        for (Transaction t : accountService.getMiniStatement(selectedAccount)) {
            System.out.printf("%s\t\t%s\t£%.2f%n", t.getTimestamp().toLocalDate(), t.getTransactionTypeStr(), t.getAmount());
        }
        System.out.println();
        printDivider();
        System.out.println("Press Enter to return to menu...");
        scanner.nextLine();
    }

    private void printHeading(String title) {
        printDivider();
        System.out.println("         "+title);
        printDivider();
    }

    private void printDivider() {
        System.out.println("=========================================");
    }

    private void printProcessing() {
        try {
            System.out.print("\nProcessing");
            Thread.sleep(800);
            System.out.print(".");
            Thread.sleep(800);
            System.out.print(".");
            Thread.sleep(800);
            System.out.println(".\n");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
