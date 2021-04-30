package com.example.sipsupporterapp.model;

public class AttachInfo {
    private int attachID;
    private int customerID;
    private int customerSupportID;
    private int customerPaymentID;
    private int customerProductID;
    private int paymentID;
    private String fileName;
    private String FileData;
    private String description;
    private int userID;
    private String userFullName;
    private long addTime;

    public int getAttachID() {
        return attachID;
    }

    public void setAttachID(int attachID) {
        this.attachID = attachID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getCustomerSupportID() {
        return customerSupportID;
    }

    public void setCustomerSupportID(int customerSupportID) {
        this.customerSupportID = customerSupportID;
    }

    public int getCustomerPaymentID() {
        return customerPaymentID;
    }

    public void setCustomerPaymentID(int customerPaymentID) {
        this.customerPaymentID = customerPaymentID;
    }

    public int getCustomerProductID() {
        return customerProductID;
    }

    public void setCustomerProductID(int customerProductID) {
        this.customerProductID = customerProductID;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileData() {
        return FileData;
    }

    public void setFileData(String fileData) {
        this.FileData = fileData;
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
}
