package com.example.sipsupporterapp.model;

public class CustomerPaymentInfo {
    private int customerPaymentID;
    private int customerID;
    private String customerName;
    private int bankAccountID;
    private String bankAccountNO;
    private String bankName;
    private String bankAccountName;
    private int datePayment;
    private long price;
    private String description;
    private int userID;
    private String userFullName;
    private long addTime;
    private boolean managerOk;
    private int managerOkUserID;
    private String managerOkUserFullName;
    private long managerOkTime;
    private int attachCount;

    public int getCustomerPaymentID() {
        return customerPaymentID;
    }

    public void setCustomerPaymentID(int customerPaymentID) {
        this.customerPaymentID = customerPaymentID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getBankAccountID() {
        return bankAccountID;
    }

    public void setBankAccountID(int bankAccountID) {
        this.bankAccountID = bankAccountID;
    }

    public String getBankAccountNO() {
        return bankAccountNO;
    }

    public void setBankAccountNO(String bankAccountNO) {
        this.bankAccountNO = bankAccountNO;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public int getDatePayment() {
        return datePayment;
    }

    public void setDatePayment(int datePayment) {
        this.datePayment = datePayment;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public boolean isManagerOk() {
        return managerOk;
    }

    public void setManagerOk(boolean managerOk) {
        this.managerOk = managerOk;
    }

    public int getManagerOkUserID() {
        return managerOkUserID;
    }

    public void setManagerOkUserID(int managerOkUserID) {
        this.managerOkUserID = managerOkUserID;
    }

    public String getManagerOkUserFullName() {
        return managerOkUserFullName;
    }

    public void setManagerOkUserFullName(String managerOkUserFullName) {
        this.managerOkUserFullName = managerOkUserFullName;
    }

    public long getManagerOkTime() {
        return managerOkTime;
    }

    public void setManagerOkTime(long managerOkTime) {
        this.managerOkTime = managerOkTime;
    }

    public int getAttachCount() {
        return attachCount;
    }

    public void setAttachCount(int attachCount) {
        this.attachCount = attachCount;
    }
}
