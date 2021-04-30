package com.example.sipsupporterapp.model;

public class ProductResult {

    private String error;
    private String errorCode;
    private ProductInfo[] products;

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

    public ProductInfo[] getProducts() {
        return products;
    }

    public void setProducts(ProductInfo[] products) {
        this.products = products;
    }
}
