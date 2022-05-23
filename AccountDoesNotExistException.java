package com.revature;

public class AccountDoesNotExistException extends RuntimeException {

    public AccountDoesNotExistException(){
        System.out.println("Sorry !! No Account existed with given account number");
    }

}
