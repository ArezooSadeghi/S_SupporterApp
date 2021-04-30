package com.example.sipsupporterapp.model;

public class CustomerSupportResult {
    private String error;
    private String errorCode;
    private CustomerSupportInfo[] customerSupports;

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

    public CustomerSupportInfo[] getCustomerSupports() {
        return customerSupports;
    }

    public void setCustomerSupports(CustomerSupportInfo[] customerSupports) {
        this.customerSupports = customerSupports;
    }
}
