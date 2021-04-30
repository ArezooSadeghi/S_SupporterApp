package com.example.sipsupporterapp.model;

public class PaymentSubjectResult {
    private String error;
    private String errorCode;
    private PaymentSubjectInfo[] paymentSubjects;

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

    public PaymentSubjectInfo[] getPaymentSubjects() {
        return paymentSubjects;
    }

    public void setPaymentSubjects(PaymentSubjectInfo[] paymentSubjects) {
        this.paymentSubjects = paymentSubjects;
    }
}

