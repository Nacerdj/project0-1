package com.revature;

import java.util.Scanner;

public class Driver {

    private static Scanner sc = new Scanner(System.in);
    private static BankServices bankService = new BankServices();

    public static void main(String[] args) {

        System.out.println("Please enter userName");
        String userName = sc.next();
        System.out.println("Please enter password");
        String password = sc.next();
        System.out.println("Please enter role");
        String role = sc.next();
        // here validate whether record present in db with given details
        int userId =  bankService.validateUser(userName, password, role);

        //if record is there then continue
        // if logged in user as customer
        if(role.equalsIgnoreCase(Constants.CUSTOMER.toString())) {
            int customerOption;
            do{
                System.out.println("Please enter 1 - for create new account, 2 - for view balance, " +
                        "3 - for withdrawal, 4 - for deposit, 5 - to post money transfer, " +
                        "6- to accept money transfer, 7 to exit");
                customerOption = sc.nextInt();
                customerSelection(customerOption, userId);
            }while (customerOption != 7);

        }else if(role.equalsIgnoreCase(Constants.EMPLOYEE.toString())) {
            // if logged in user is employee
            int employeeOption;
            do {
                System.out.println("Please enter "+
                        " 1 - Approve or Reject new account request \n, "+
                        " 2 - to view customer account details \n, " +
                        " 3 - to view all transactions of a customer \n, "+
                        " 4 - to exit");
                 employeeOption = sc.nextInt();
                 employeeSelection(employeeOption, userId);
            }while (employeeOption != 4);

        }
        System.out.println("Thank you !! Visit Again !!");
    }
    private static void employeeSelection(int employeeOption, int userId){
        switch (employeeOption){
            case 1 :
                bankService.approveOrReject(sc);
                break;
            case 2 :
                bankService.viewCusomerAccount(sc);
                break;
            case 3 :
               bankService.viewAllTransactions(sc);
               break;
            default:
                System.out.println("Invalid Choice");
        }
    }
    private static void customerSelection(int customerOption, int userId) {
        switch(customerOption){
            case 1:
                 bankService.createAccount(sc, userId);
                 break;
            case 2 :
                bankService.viewBalance(sc);
                break;
            case 3:
                bankService.withDrawal(sc);
                break;
            case 4:
                bankService.deposit(sc);
                break;
            case 5 :
                bankService.postMoneyTransfer(sc);
                break;
            case 6:
                bankService.acceptMoneyTransfer(sc);
                break;
            default:
                System.out.println("Invalid CHoice");
        }
    }



}
