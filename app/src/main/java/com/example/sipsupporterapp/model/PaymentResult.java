package com.example.sipsupporterapp.model;

public class PaymentResult {
    private String error;
    private String errorCode;
    private PaymentInfo[] payments;

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

    public PaymentInfo[] getPayments() {
        return payments;
    }

    public void setPayments(PaymentInfo[] payments) {
        this.payments = payments;
    }
}
