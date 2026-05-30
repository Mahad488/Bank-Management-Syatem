import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;

class Account {
    private int accountNumber;
    private String name;
    private String password;
    private double balance;

    Stack<String> transactions = new Stack<>();

    public Account(int accountNumber, String name, String password, double balance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.password = password;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.push("Deposited: " + amount);
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transactions.push("Withdrawn: " + amount);
            return true;
        }
        return false;
    }

    public void transfer(Account receiver, double amount) {
        balance -= amount;
        receiver.balance += amount;

        transactions.push("Transferred " + amount + " to Account " + receiver.accountNumber);
        receiver.transactions.push("Received " + amount + " from Account " + accountNumber);
    }

    public Stack<String> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return accountNumber + "," + name + "," + password + "," + balance;
    }
}

class Bank {

    LinkedList<Account> accounts = new LinkedList<>();
    HashMap<Integer, Account> accountMap = new HashMap<>();
    Queue<String> customerRequests = new LinkedList<>();

    public void addAccount(Account account) {
        accounts.add(account);
        accountMap.put(account.getAccountNumber(), account);
        saveAccounts();
    }

    public Account searchAccount(int accNo) {
        return accountMap.get(accNo);
    }

    public boolean login(int accNo, String password) {
        Account acc = accountMap.get(accNo);

        return acc != null && acc.getPassword().equals(password);
    }

    public void addRequest(String request) {
        customerRequests.add(request);
    }

    public void saveAccounts() {

        try {

            FileWriter writer = new FileWriter("accounts.txt");

            for (Account acc : accounts) {
                writer.write(acc.toString() + "\n");
            }

            writer.close();

        } catch (Exception e) {
            System.out.println("File Error");
        }
    }

    public void loadAccounts() {

        try {

            File file = new File("accounts.txt");

            if (!file.exists()) {
                return;
            }

            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {

                String line = sc.nextLine();
                String[] data = line.split(",");

                int accNo = Integer.parseInt(data[0]);
                String name = data[1];
                String password = data[2];
                double balance = Double.parseDouble(data[3]);

                Account acc = new Account(accNo, name, password, balance);

                accounts.add(acc);
                accountMap.put(accNo, acc);
            }

            sc.close();

        } catch (Exception e) {
            System.out.println("Load Error");
        }
    }
}

public class project extends JFrame {

    Bank bank = new Bank();

    JTextField accField;
    JPasswordField passField;

    public project() {

        bank.loadAccounts();

        setTitle("Bank Management System");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(20, 40, 90));

        JLabel title = new JLabel("Digital Bank System");
        title.setBounds(130, 20, 300, 40);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(title);

        JLabel accLabel = new JLabel("Account Number:");
        accLabel.setBounds(70, 100, 150, 30);
        accLabel.setForeground(Color.WHITE);
        panel.add(accLabel);

        accField = new JTextField();
        accField.setBounds(220, 100, 180, 30);
        panel.add(accField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(70, 150, 150, 30);
        passLabel.setForeground(Color.WHITE);
        panel.add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(220, 150, 180, 30);
        panel.add(passField);

        JButton loginBtn = new JButton("Login");
        loginBtn.setBounds(90, 240, 120, 40);
        panel.add(loginBtn);

        JButton registerBtn = new JButton("Register");
        registerBtn.setBounds(260, 240, 120, 40);
        panel.add(registerBtn);

        add(panel);

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> register());
    }

    public void login() {

        try {

            int accNo = Integer.parseInt(accField.getText());
            String password = String.valueOf(passField.getPassword());

            if (bank.login(accNo, password)) {

                JOptionPane.showMessageDialog(this, "Login Successful");

                Account acc = bank.searchAccount(accNo);

                new Dashboard(bank, acc);

                dispose();

            } else {

                JOptionPane.showMessageDialog(this, "Invalid Credentials");
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Invalid Input");
        }
    }

    public void register() {

        JTextField nameField = new JTextField();
        JTextField accNoField = new JTextField();
        JTextField balanceField = new JTextField();
        JTextField pass = new JTextField();

        Object[] fields = {
                "Name:", nameField,
                "Account Number:", accNoField,
                "Initial Balance:", balanceField,
                "Password:", pass
        };

        int option = JOptionPane.showConfirmDialog(
                null,
                fields,
                "Create Account",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {

            try {

                String name = nameField.getText();
                int accNo = Integer.parseInt(accNoField.getText());
                double balance = Double.parseDouble(balanceField.getText());
                String password = pass.getText();

                if (bank.searchAccount(accNo) != null) {

                    JOptionPane.showMessageDialog(this, "Account Already Exists");
                    return;
                }

                Account account = new Account(accNo, name, password, balance);

                bank.addAccount(account);

                JOptionPane.showMessageDialog(this, "Account Created Successfully");

            } catch (Exception e) {

                JOptionPane.showMessageDialog(this, "Invalid Input");
            }
        }
    }

    public static void main(String[] args) {

        new project().setVisible(true);
    }
}

class Dashboard extends JFrame {

    Bank bank;
    Account currentAccount;

    public Dashboard(Bank bank, Account account) {

        this.bank = bank;
        this.currentAccount = account;

        setTitle("Dashboard");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel welcome = new JLabel("Welcome " + account.getName());
        welcome.setBounds(250, 20, 300, 30);
        welcome.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(welcome);

        JButton depositBtn = new JButton("Deposit");
        depositBtn.setBounds(70, 100, 150, 50);
        panel.add(depositBtn);

        JButton withdrawBtn = new JButton("Withdraw");
        withdrawBtn.setBounds(250, 100, 150, 50);
        panel.add(withdrawBtn);

        JButton transferBtn = new JButton("Transfer");
        transferBtn.setBounds(430, 100, 150, 50);
        panel.add(transferBtn);

        JButton balanceBtn = new JButton("Check Balance");
        balanceBtn.setBounds(70, 200, 150, 50);
        panel.add(balanceBtn);

        JButton historyBtn = new JButton("Transactions");
        historyBtn.setBounds(250, 200, 150, 50);
        panel.add(historyBtn);

        JButton requestBtn = new JButton("Customer Request");
        requestBtn.setBounds(430, 200, 150, 50);
        panel.add(requestBtn);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBounds(250, 320, 150, 50);
        panel.add(logoutBtn);

        add(panel);

        depositBtn.addActionListener(e -> deposit());
        withdrawBtn.addActionListener(e -> withdraw());
        transferBtn.addActionListener(e -> transfer());
        balanceBtn.addActionListener(e -> balance());
        historyBtn.addActionListener(e -> showTransactions());
        requestBtn.addActionListener(e -> request());

        logoutBtn.addActionListener(e -> {

            new project().setVisible(true);

            dispose();
        });

        setVisible(true);
    }

    public void deposit() {

        String input = JOptionPane.showInputDialog(this, "Enter Amount:");

        try {

            double amount = Double.parseDouble(input);

            if (amount <= 0) {
                throw new Exception();
            }

            currentAccount.deposit(amount);

            bank.saveAccounts();

            JOptionPane.showMessageDialog(this, "Deposit Successful");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Invalid Amount");
        }
    }

    public void withdraw() {

        String input = JOptionPane.showInputDialog(this, "Enter Amount:");

        try {

            double amount = Double.parseDouble(input);

            if (amount <= 0) {
                throw new Exception();
            }

            if (currentAccount.withdraw(amount)) {

                bank.saveAccounts();

                JOptionPane.showMessageDialog(this, "Withdrawal Successful");

            } else {

                JOptionPane.showMessageDialog(this, "Insufficient Balance");
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this, "Invalid Amount");
        }
    }

    public void transfer() {

        JTextField accField = new JTextField();
        JTextField amountField = new JTextField();

        Object[] fields = {
                "Receiver Account Number:", accField,
                "Amount:", amountField
        };

        int option = JOptionPane.showConfirmDialog(
                this,
                fields,
                "Transfer Money",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (option == JOptionPane.OK_OPTION) {

            try {

                int receiverAcc = Integer.parseInt(accField.getText());
                double amount = Double.parseDouble(amountField.getText());

                Account receiver = bank.searchAccount(receiverAcc);

                if (receiver == null) {

                    JOptionPane.showMessageDialog(this, "Receiver Account Not Found");
                    return;
                }

                if (amount > currentAccount.getBalance()) {

                    JOptionPane.showMessageDialog(this, "Insufficient Balance");
                    return;
                }

                currentAccount.transfer(receiver, amount);

                bank.saveAccounts();

                JOptionPane.showMessageDialog(this, "Transfer Successful");

            } catch (Exception e) {

                JOptionPane.showMessageDialog(this, "Invalid Input");
            }
        }
    }

    public void balance() {

        JOptionPane.showMessageDialog(
                this,
                "Current Balance: " + currentAccount.getBalance()
        );
    }

    public void showTransactions() {

        JFrame frame = new JFrame("Transaction History");
        frame.setSize(500, 400);

        String[] columns = {"Recent Transactions"};

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        Stack<String> stack = currentAccount.getTransactions();

        for (int i = stack.size() - 1; i >= 0; i--) {

            model.addRow(new Object[]{stack.get(i)});
        }

        JTable table = new JTable(model);

        JScrollPane pane = new JScrollPane(table);

        frame.add(pane);

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);
    }

    public void request() {

        String req = JOptionPane.showInputDialog(
                this,
                "Enter Customer Request:"
        );

        if (req != null && !req.isEmpty()) {

            bank.addRequest(req);

            JOptionPane.showMessageDialog(
                    this,
                    "Request Added To Queue"
            );
        }
    }
}