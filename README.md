# Belfast Bank ATM Simulator

A Java-based ATM simulator built as a portfolio project to demonstrate OOP principles.
Simulates a real-world ATM experience via the command line interface (CLI).

## Features
- Login with card number and PIN
- PIN blocking after 3 failed attempts
- Check account balance
- Withdraw funds
- Deposit funds
- Mini statement (last 5 transactions)

## Account Types
- **Current Account** - configurable overdraft limit
- **Savings Account** - interest rate, max 3 withdrawals per month
- **Student Account** - fixed £500 overdraft, max £200 single withdrawal

## Tech Stack
- Java
- Maven

## Project Structure
```
src/
├── model/         # Core data classes (Account, Customer, Transaction)
├── service/       # Business logic (AccountService, CustomerService)
├── atm/           # CLI session and menu handling
├── exception/     # Custom exceptions
└── Main.java      # Entry point
```

## How to Run
1. Clone the repository
2. Open in IntelliJ IDEA
3. Run `Main.java`

Test credentials are printed at the start of the program.

## Design Decisions
- `BigDecimal` used for all monetary values to avoid floating point errors
- Abstract `Account` class with account-specific withdrawal rules in each subclass
- Service layer handles orchestration, models handle their own rules
- PIN never exposed outside of `Customer` class
- Transactions are immutable once created
- `recordTransaction()` protected helper in `Account` keeps transaction recording consistent across all subclasses

## Known Limitations
- Data does not persist between sessions (JSON persistence planned)
- PINs stored as plain text — in production would use BCrypt hashing
- Card numbers are 8 digits for simplicity — real card numbers are 16 digits and follow the Luhn algorithm
- Account numbers randomly generated and could theoretically produce duplicates

## Status
- Core features complete ✅
- JSON persistence coming soon 🚧
