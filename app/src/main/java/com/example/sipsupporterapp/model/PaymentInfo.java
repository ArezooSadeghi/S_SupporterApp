package com.example.sipsupporterapp.model;

public class PaymentInfo {
    private int paymentID;
    private int bankAccountID;
    private String bankAccountName;
    private int paymentSubjectID;
    private String paymentSubject;
    private String parentPaymentSubject;
    private int datePayment;
    private long price;
    private String description;
    private int attachCount;
    private int userID;
    private String userFullName;
    private long addTime;
    private boolean ManagerOK;

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public int getBankAccountID() {
        return bankAccountID;
    }

    public void setBankAccountID(int bankAccountID) {
        this.bankAccountID = bankAccountID;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public int getPaymentSubjectID() {
        return paymentSubjectID;
    }

    public void setPaymentSubjectID(int paymentSubjectID) {
        this.paymentSubjectID = paymentSubjectID;
    }

    public String getPaymentSubject() {
        return paymentSubject;
    }

    public void setPaymentSubject(String paymentSubject) {
        this.paymentSubject = paymentSubject;
    }

    public String getParentPaymentSubject() {
        return parentPaymentSubject;
    }

    public void setParentPaymentSubject(String parentPaymentSubject) {
        this.parentPaymentSubject = parentPaymentSubject;
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

    public int getAttachCount() {
        return attachCount;
    }

    public void setAttachCount(int attachCount) {
        this.attachCount = attachCount;
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

    public boolean isManagerOK() {
        return ManagerOK;
    }

    public void setManagerOK(boolean managerOK) {
        ManagerOK = managerOK;
    }
}
