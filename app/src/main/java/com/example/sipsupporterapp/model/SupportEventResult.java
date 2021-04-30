package com.example.sipsupporterapp.model;

public class SupportEventResult {

    private String error;
    private String errorCode;
    private SupportEvents[] supportEvents;

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

    public SupportEvents[] getSupportEvents() {
        return supportEvents;
    }

    public void setSupportEvents(SupportEvents[] supportEvents) {
        this.supportEvents = supportEvents;
    }
}
