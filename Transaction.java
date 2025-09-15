import java.util.*;


class Transaction {
    private String transactionId;
    private Date date;
    private double amount;
    private String type;  

    public Transaction(String transactionId, double amount, String type) {
        this.transactionId = transactionId;
        this.date = new Date(); // current date
        this.amount = amount;
        this.type = type;
    }


    public String getTransactionId() { return transactionId; }
    public Date getDate() { return date; }
    public double getAmount() { return amount; }
    public String getType() { return type; }

    @Override
    public String toString() {
        return "TxnID: " + transactionId + ", Date: " + date +
               ", Amount: " + amount + ", Type: " + type;
    }
}


abstract class Account {
    protected String accountNumber;
    protected String holderName;
    protected double balance;
    protected List<Transaction> transactions = new ArrayList<>();

    public Account(String accountNumber, String holderName, double balance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    // Add transaction
    public void addTransaction(Transaction txn) {
        transactions.add(txn);
    }

    public void printTransactions() {
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

    public abstract void withdraw(double amount);
    public abstract void deposit(double amount);
}


class SavingsAccount extends Account {
    private static final double MIN_BALANCE = 1000;

    public SavingsAccount(String accountNumber, String holderName, double balance) {
        super(accountNumber, holderName, balance);
    }

    @Override
    public void withdraw(double amount) {
        if (balance - amount < MIN_BALANCE) {
            System.out.println("❌ Withdrawal denied! Minimum balance of " + MIN_BALANCE + " must be maintained.");
        } else {
            balance -= amount;
            Transaction txn = new Transaction(UUID.randomUUID().toString(), amount, "Withdraw");
            addTransaction(txn);
            System.out.println("✅ Withdrawn: " + amount + ", Balance: " + balance);
        }
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        Transaction txn = new Transaction(UUID.randomUUID().toString(), amount, "Deposit");
        addTransaction(txn);
        System.out.println("✅ Deposited: " + amount + ", Balance: " + balance);
    }
}


class CurrentAccount extends Account {
    private static final double OVERDRAFT_LIMIT = -5000;

    public CurrentAccount(String accountNumber, String holderName, double balance) {
        super(accountNumber, holderName, balance);
    }

    @Override
    public void withdraw(double amount) {
        if (balance - amount < OVERDRAFT_LIMIT) {
            System.out.println("❌ Withdrawal denied! Overdraft limit reached.");
        } else {
            balance -= amount;
            Transaction txn = new Transaction(UUID.randomUUID().toString(), amount, "Withdraw");
            addTransaction(txn);
            System.out.println("✅ Withdrawn: " + amount + ", Balance: " + balance);
        }
    }

    @Override
    public void deposit(double amount) {
        balance += amount;
        Transaction txn = new Transaction(UUID.randomUUID().toString(), amount, "Deposit");
        addTransaction(txn);
        System.out.println("✅ Deposited: " + amount + ", Balance: " + balance);
    }
}

interface TransactionLogger {
    void log(Transaction txn);
}


abstract class ATM {
    protected String location;
    protected String machineId;

    public ATM(String location, String machineId) {
        this.location = location;
        this.machineId = machineId;
    }

    public abstract void withdraw(Account account, double amount);
    public abstract void deposit(Account account, double amount);
}

class BankATM extends ATM implements TransactionLogger {
    public BankATM(String location, String machineId) {
        super(location, machineId);
    }

    @Override
    public void withdraw(Account account, double amount) {
        account.withdraw(amount);
    }

    @Override
    public void deposit(Account account, double amount) {
        account.deposit(amount);
    }

    @Override
    public void log(Transaction txn) {
        System.out.println("📜 Logging Transaction: " + txn);
    }
}

public class ATMSystem {
    public static void main(String[] args) {
        Account savings = new SavingsAccount("SA123", "Amit", 5000);
        Account current = new CurrentAccount("CA456", "Rahul", 2000);

        BankATM atm = new BankATM("Delhi", "ATM001");

        // Savings Account operations
        atm.deposit(savings, 2000);
        atm.withdraw(savings, 3000);
        atm.withdraw(savings, 4000); 

        System.out.println("\nSavings Transactions:");
        savings.printTransactions();

        // Current Account operations
        atm.deposit(current, 1000);
        atm.withdraw(current, 7000); // overdraft allowed
        atm.withdraw(current, 1000); // should fail (limit exceeded)

        System.out.println("\nCurrent Transactions:");
        current.printTransactions();
    }
}
