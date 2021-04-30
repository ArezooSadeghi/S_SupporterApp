package com.example.sipsupporterapp.model;

public class CustomerUsers {

    private int customerUserID;
    private int customerID;
    private int userID;
    private String userName;
    private String lastSeen;

    public int getCustomerUserID() {
        return customerUserID;
    }

    public void setCustomerUserID(int customerUserID) {
        this.customerUserID = customerUserID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }
}
