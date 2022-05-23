package com.revature;

public class Transaction {
    private String status;
    private int accountNumber;
    private int amount;
    private String operation;

    public Transaction(){

    }
    public Transaction(int accountNumber, int amount, String operation, String status) {
        this.status = status;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.operation = operation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "status='" + status + '\'' +
                ", accountNumber=" + accountNumber +
                ", amount=" + amount +
                ", operation='" + operation + '\'' +
                '}';
    }
}
