package com.example.sipsupporterapp.model;

public class BankAccountInfo {
    private int bankAccountID;
    private String bankName;
    private String bankAccountNO;
    private String bankAccountName;
    private String SHABA;
    private long balance;
    private int userID;
    private String userFullName;
    private long addTime;

    public int getBankAccountID() {
        return bankAccountID;
    }

    public void setBankAccountID(int bankAccountID) {
        this.bankAccountID = bankAccountID;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNO() {
        return bankAccountNO;
    }

    public void setBankAccountNO(String bankAccountNO) {
        this.bankAccountNO = bankAccountNO;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getSHABA() {
        return SHABA;
    }

    public void setSHABA(String SHABA) {
        this.SHABA = SHABA;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }
}
