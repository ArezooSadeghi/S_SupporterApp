package com.example.sipsupporterapp.model;

public class CustomerPaymentResult {
    private String error;
    private String errorCode;
    private CustomerPaymentInfo[] customerPayments;

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

    public CustomerPaymentInfo[] getCustomerPayments() {
        return customerPayments;
    }

    public void setCustomerPayments(CustomerPaymentInfo[] customerPayments) {
        this.customerPayments = customerPayments;
    }
}
