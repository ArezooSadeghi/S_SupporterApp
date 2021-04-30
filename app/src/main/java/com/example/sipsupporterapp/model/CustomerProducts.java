package com.example.sipsupporterapp.model;

public class CustomerProducts {

    private int customerProductID;
    private int customerID;
    private String customerName;
    private int productID;
    private String productName;
    private long invoicePrice;
    private boolean invoicePayment;
    private String description;
    private long expireDate;
    private int userID;
    private String userFullName;
    private long addTime;
    private boolean finish;
    private int finishUserID;
    private String finishUserFullName;
    private long finishTime;
    private int invoicePaymentUserID;
    private String invoicePaymentUserFullName;
    private long invoicePaymentTime;

    public int getCustomerProductID() {
        return customerProductID;
    }

    public void setCustomerProductID(int customerProductID) {
        this.customerProductID = customerProductID;
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

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getInvoicePrice() {
        return invoicePrice;
    }

    public void setInvoicePrice(long invoicePrice) {
        this.invoicePrice = invoicePrice;
    }

    public boolean isInvoicePayment() {
        return invoicePayment;
    }

    public void setInvoicePayment(boolean invoicePayment) {
        this.invoicePayment = invoicePayment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
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

    public boolean isFinish() {
        return finish;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public int getFinishUserID() {
        return finishUserID;
    }

    public void setFinishUserID(int finishUserID) {
        this.finishUserID = finishUserID;
    }

    public String getFinishUserFullName() {
        return finishUserFullName;
    }

    public void setFinishUserFullName(String finishUserFullName) {
        this.finishUserFullName = finishUserFullName;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(long finishTime) {
        this.finishTime = finishTime;
    }

    public int getInvoicePaymentUserID() {
        return invoicePaymentUserID;
    }

    public void setInvoicePaymentUserID(int invoicePaymentUserID) {
        this.invoicePaymentUserID = invoicePaymentUserID;
    }

    public String getInvoicePaymentUserFullName() {
        return invoicePaymentUserFullName;
    }

    public void setInvoicePaymentUserFullName(String invoicePaymentUserFullName) {
        this.invoicePaymentUserFullName = invoicePaymentUserFullName;
    }

    public long getInvoicePaymentTime() {
        return invoicePaymentTime;
    }

    public void setInvoicePaymentTime(long invoicePaymentTime) {
        this.invoicePaymentTime = invoicePaymentTime;
    }
}

