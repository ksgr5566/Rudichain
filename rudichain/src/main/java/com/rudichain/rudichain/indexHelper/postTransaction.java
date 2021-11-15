package com.rudichain.rudichain.indexHelper;

public class postTransaction {
    private double amount;
    private String recipient;

    public double getAmount(){
        return amount;
    }

    public String getRecipient(){
        return recipient;
    }

    public postTransaction(double amount, String recipient){
        this.amount = amount;
        this.recipient = recipient;
    }

}