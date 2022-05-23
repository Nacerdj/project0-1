package com.revature;

// Account class for storing records as objects.
public class Account {

    private int accountNumber;
    private int amount;
    private int userId;
    private String status;
     // default constructor
    public Account(){

    }
    public Account(int accountNumber, int amount, int userId, String status) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", amount=" + amount +
                ", userId=" + userId +
                ", status=" + status +
                '}';
    }
}
