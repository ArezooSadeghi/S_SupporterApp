package com.example.sipsupporterapp.model;

public class CustomerUserResult {
    private String error;
    private String errorCode;
    private CustomerUsers[] customerUsers;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public CustomerUsers[] getCustomerUsers() {
        return customerUsers;
    }

    public void setCustomerUsers(CustomerUsers[] customerUsers) {
        this.customerUsers = customerUsers;
    }
}
