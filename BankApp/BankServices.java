package com.revature;

import java.util.Random;
import java.util.Scanner;

public class BankServices {
    private BankDao bankDao;
    public BankServices(){
        bankDao = new BankDao();
    }

    public int validateUser(String userName, String password, String role){
          int userId = bankDao.validateUser( userName,  password,  role);
          if(userId == 0){
              throw new UserNotExistException();
          }
          return userId;
    }

    public void createAccount(Scanner sc, int userId) {
        System.out.println("Welcome to create a new Account");
            int newAccountNumber = generateAccountNumber();
            Account newAccount = new Account(newAccountNumber, 100, userId, Constants.PENDINGTOAPPROVE.toString());
            int result = bankDao.createNewAccount(newAccount);
            if(result != 0) {
                System.out.println("Congratulations! Your account details are "+newAccount);
            }else{
                System.out.println("Sorry some trouble in creating the account");
            }
    }

    public void viewBalance(Scanner sc) {
        System.out.println("Welcome to view balance");
        System.out.println("Please enter your account number");
        int accountNumber = sc.nextInt();
        // if account does not exist throw user defined exception -- AccountDoesNotExistException
        Account account = bankDao.isAccountExisted(accountNumber);
        if(account == null){
            throw new AccountDoesNotExistException();
        }else {
            // pull balance from db with given account number
            System.out.println("Your Balance is "+account.getAmount());
        }
    }

    public void withDrawal(Scanner sc) {
        System.out.println("Welcome to withDrawal");
        System.out.println("Please enter your account number");
        int accountNumber = sc.nextInt();
        // if account does not exist throw user defined exception -- AccountDoesNotExistException
        Account account = bankDao.isAccountExisted(accountNumber);
        if(account == null){
            throw new AccountDoesNotExistException();
        }else {
            System.out.println("Please enter the amount to withdraw");
            int withdrawalRequestedAmount = sc.nextInt();
            // check the validity of amount
            if (withdrawalRequestedAmount > 0 && withdrawalRequestedAmount < account.getAmount()) {
                int result = bankDao.withDrawOrDeposit(account, withdrawalRequestedAmount, Constants.WITHDRAW.toString());
                if(result != 0) {
                    System.out.println("WithDrawal success and current balance is " + account.getAmount());
                }else{
                    System.out.println("some trouble in doing the operation");
                }
            } else {
                System.out.println("Invalid amount to withdraw");
            }
        }
    }

    public void deposit(Scanner sc) {
        System.out.println("Welcome to deposit");
        System.out.println("Please enter your account number");
        int accountNumber = sc.nextInt();
        // if account does not exist throw user defined exception -- AccountDoesNotExistException
            Account account = bankDao.isAccountExisted(accountNumber);
            if(account == null){
                throw new AccountDoesNotExistException();
            }else {
                System.out.println("Please enter the amount to deposit");
                int depositAmount = sc.nextInt();
                // check the validity of amount
                if (depositAmount > 0) {
                    int result = bankDao.withDrawOrDeposit(account, depositAmount, Constants.DEPOSIT.toString());
                    if (result != 0) {
                        System.out.println("Deposit success and current balance is " + account.getAmount());
                    } else {
                        System.out.println("some trouble in doing the operation");
                    }
                } else {
                    System.out.println("Invalid amount to deposit");
                }
            }
    }

    public boolean postMoneyTransfer(Scanner sc) {
        System.out.println("Welcome to post money transfer to another account");
        boolean flag = false;
        System.out.println("Please enter your account number");
        int senderAccountNumber = sc.nextInt();
        Account senderAccount = validateAccountNumber(senderAccountNumber);
        if(senderAccount!=null){
            System.out.println("Please enter receiver account number");
            int receiverAccountNumber = sc.nextInt();
            Account receiverAccount = validateAccountNumber(receiverAccountNumber);
            if (receiverAccount != null) {
                System.out.println("Please enter amount");
                int amountToTransfer = sc.nextInt();
                if(amountToTransfer > 0 && amountToTransfer < senderAccount.getAmount()){
                    int result = bankDao.postTransfer(senderAccount, receiverAccount, amountToTransfer);
                    if(result == 1){
                        System.out.println("Successfully posted money transfer from "+senderAccount.getAccountNumber()+" to "+receiverAccount.getAccountNumber());
                        flag = true;
                    }else{
                        System.out.println("some trouble while doing post transfer");
                    }
                }else{
                    System.out.println("Sorry invalid amount");
                }
            }else{
                System.out.println("Sorry ! Receiver account does not exist");
            }
        }else{
            System.out.println("Sorry !! Sender account does not exist");
        }
        return flag;
    }

    public boolean acceptMoneyTransfer(Scanner sc) {
        System.out.println("Welcome to accept money transfer from another account");
        boolean flag = false;
        System.out.println("Please enter sender account number");
        int senderAccountNumber = sc.nextInt();
        Account senderAccount = validateAccountNumber(senderAccountNumber);
        if(senderAccount!=null){
            System.out.println("Please enter your account number");
            int receiverAccountNumber = sc.nextInt();
            Account receiverAccount = validateAccountNumber(receiverAccountNumber);
            if (receiverAccount != null) {
                System.out.println("Please enter amount");
                int amountToTransfer = sc.nextInt();
                //validate entry in pendingTransactions
                int pendingTransactionResult = bankDao.validatePendingTransaction(senderAccount, receiverAccount, amountToTransfer);
                if(pendingTransactionResult!=0){
                    int result = bankDao.completePendingTransaction(senderAccount, receiverAccount, amountToTransfer);
                    if(result == 1){
                        System.out.println("Successfully accepted money transfer from "+senderAccount.getAccountNumber()+" to "+receiverAccount.getAccountNumber());
                        flag = true;
                    }else{
                        System.out.println("some trouble while doing post transfer");
                    }
                }else{
                    System.out.println("Sorry could not found any pending transaction with given details");
                }
            }else{
                System.out.println("Sorry ! Receiver account does not exist");
            }
        }else{
            System.out.println("Sorry !! Sender account does not exist");
        }
        return flag;
    }

    private Account validateAccountNumber(int accountNumber){
        Account account = bankDao.isAccountExisted(accountNumber);
        if(account == null){
            throw new AccountDoesNotExistException();
        }
        return account;
    }

    private int generateAccountNumber() {
        Random r = new Random();
        return (Math.abs(r.nextInt(99999999)));
    }

    public void viewAllTransactions(Scanner sc){
        System.out.println("Welcome to view all transactions of a customer");
        System.out.println("Please enter customer account Number");
        int customerAccountNumber = sc.nextInt();
        validateAccountNumber(customerAccountNumber);
        bankDao.displayTransactions(customerAccountNumber);
    }

    public void viewCusomerAccount(Scanner sc){
        System.out.println("Welcome to view customer details");
        System.out.println("Please enter customer account number");
        int accountNumber = sc.nextInt();
        Account account = validateAccountNumber(accountNumber);
        System.out.println(account);
    }

    public void approveOrReject(Scanner sc){
        System.out.println("Welcome to approve or reject menu");
        System.out.println("Please enter account number");
        int accountNumber = sc.nextInt();
        Account account = bankDao.isAccountExistedForApproval(accountNumber);
        if(account != null) {
            System.out.println("Press 101 to approve or 102 to reject");
            int approveOrRejectChoice = sc.nextInt();
            if (approveOrRejectChoice == 101) {
                account.setStatus(Constants.APPROVED.toString());
                bankDao.approveOrRejectAccount(account);
            } else if (approveOrRejectChoice == 102)
                account.setStatus(Constants.REJECTED.toString());
                bankDao.approveOrRejectAccount(account);
            } else{
                System.out.println("Invalid choice");
            }
        }
    }

