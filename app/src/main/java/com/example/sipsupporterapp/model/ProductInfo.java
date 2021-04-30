package com.example.sipsupporterapp.model;

public class ProductInfo {

    private int productID;
    private String productName;
    private boolean unRepeatable;
    private boolean expirationDate;
    private boolean withInvoice;
    private long cost;
    private int row;
    private int userID;
    private String userFullName;
    private long addTime;
    private long editTime;

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

    public boolean isUnRepeatable() {
        return unRepeatable;
    }

    public void setUnRepeatable(boolean unRepeatable) {
        this.unRepeatable = unRepeatable;
    }

    public boolean isExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(boolean expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isWithInvoice() {
        return withInvoice;
    }

    public void setWithInvoice(boolean withInvoice) {
        this.withInvoice = withInvoice;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
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

    public long getEditTime() {
        return editTime;
    }

    public void setEditTime(long editTime) {
        this.editTime = editTime;
    }
}
