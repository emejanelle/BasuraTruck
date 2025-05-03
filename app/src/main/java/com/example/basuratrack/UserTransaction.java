package com.example.basuratrack;

public class UserTransaction {
    private String transactionID;
    private int imageRedID;

    public UserTransaction(String transactionID, int imageRedID) {
        this.transactionID = transactionID;
        this.imageRedID = imageRedID;
    }

//   Getters
    public String getTransactionID() {return transactionID;}
    public int getImageRedID() {return imageRedID;}
}
