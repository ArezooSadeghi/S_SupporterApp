package com.example.sipsupporterapp.model;

public class BankAccountResult {
    private String errorCode;
    private String error;
    private BankAccountInfo[] bankAccounts;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public BankAccountInfo[] getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(BankAccountInfo[] bankAccounts) {
        this.bankAccounts = bankAccounts;
    }
}
