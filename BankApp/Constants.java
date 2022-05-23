package com.revature;

public enum Constants {

    EMPLOYEE("employee"),
    CUSTOMER("customer"),
    SUCCESS("success"),
    FAIL("fail"),
    WITHDRAW("withdrawal"),
    DEPOSIT("deposit"),
    PENDINGTOAPPROVE("pending to approve"),
    APPROVED("approved"),
    REJECTED("rejected");

    private String message;
    Constants(String message){
          this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
