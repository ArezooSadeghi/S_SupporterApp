package com.example.sipsupporterapp.model;

public class CustomerResult {

    private String error;
    private String errorCode;
    private CustomerInfo[] customers;

    public CustomerInfo[] getCustomers() {
        return customers;
    }

    public void setCustomers(CustomerInfo[] customers) {
        this.customers = customers;
    }

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
}
