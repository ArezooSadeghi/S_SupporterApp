package com.example.sipsupporterapp.model;

public class UserInfo {

    private int userID;
    private String userFullName;
    private String userLoginKey;
    private boolean disable;

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

    public String getUserLoginKey() {
        return userLoginKey;
    }

    public void setUserLoginKey(String userLoginKey) {
        this.userLoginKey = userLoginKey;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
}
