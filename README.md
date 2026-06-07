# 🏦 Digital Bank System

A Java Swing-based Banking Management System that allows users to create accounts, login securely, deposit money, withdraw funds, transfer money, view transaction history, and submit customer requests.

## 📌 Features

- User Registration
- User Login Authentication
- Deposit Money
- Withdraw Money
- Transfer Funds
- Check Account Balance
- Transaction History Tracking
- Customer Request Queue
- File-Based Data Persistence

## 🛠 Technologies Used

- Java
- Java Swing (GUI)
- Collections Framework
  - LinkedList
  - HashMap
  - Stack
  - Queue
- File Handling

## 📂 Data Structures

| Data Structure | Purpose |
|--------------|---------|
| LinkedList | Store accounts |
| HashMap | Fast account search |
| Stack | Transaction history |
| Queue | Customer requests |

## 🚀 How to Run

### Compile

```bash
javac project.java
```

### Run

```bash
java project
```

## 📋 Functionalities

### Account Registration
Users can create a new account by providing:
- Name
- Account Number
- Initial Balance
- Password

### Login
Users login using:
- Account Number
- Password

### Deposit
Add money to the account balance.

### Withdraw
Withdraw money if sufficient balance exists.

### Transfer
Transfer money to another registered account.

### Check Balance
View the current account balance.

### Transaction History
View all deposits, withdrawals, and transfers.

### Customer Requests
Submit customer requests that are stored in a queue.

## 💾 Data Storage

Account information is stored in:

```text
accounts.txt
```

Format:

```text
AccountNumber,Name,Password,Balance
```

Example:

```text
1001,Ali,1234,5000.0
1002,Sara,abcd,2500.0
```

## 📸 System Workflow

1. Register Account
2. Login
3. Access Dashboard
4. Perform Banking Operations
5. Data Saved Automatically

## ⚠ Limitations

- Passwords are stored in plain text.
- Transaction history is not permanently saved.
- Customer requests are not persisted after closing the application.
- No database integration.

## 🔮 Future Improvements

- MySQL Database Integration
- Password Encryption
- Admin Panel
- Loan Management
- ATM Simulation
- Interest Calculation
- Account Statement Export

## 👨‍💻 Author

Developed as a Java Data Structures & Algorithms Project.

## 📜 License

This project is for educational purposes only.
