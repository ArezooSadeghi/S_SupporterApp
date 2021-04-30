package com.example.sipsupporterapp.model;

public class CustomerProductResult {

    private String error;
    private String errorCode;
    private CustomerProducts[] customerProducts;

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

    public CustomerProducts[] getCustomerProducts() {
        return customerProducts;
    }

    public void setCustomerProducts(CustomerProducts[] customerProducts) {
        this.customerProducts = customerProducts;
    }
}
