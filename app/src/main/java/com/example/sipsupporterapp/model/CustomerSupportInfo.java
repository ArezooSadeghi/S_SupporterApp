package com.example.sipsupporterapp.model;

public class CustomerSupportInfo {

    private int customerSupportID;
    private int supportEventID;
    private String supportEvent;
    private int customerID;
    private String customerName;
    private int customerUserID;
    private String customerUserName;
    private String question;
    private String regTime;
    private int userID;
    private String userFullName;
    private String answer;
    private String answerRegTime;

    public CustomerSupportInfo(int supportEventID, int customerID, int customerUserID, String question, String answer) {
        this.supportEventID = supportEventID;
        this.customerID = customerID;
        this.customerUserID = customerUserID;
        this.question = question;
        this.answer = answer;
    }

    public int getCustomerSupportID() {
        return customerSupportID;
    }

    public void setCustomerSupportID(int customerSupportID) {
        this.customerSupportID = customerSupportID;
    }

    public int getSupportEventID() {
        return supportEventID;
    }

    public void setSupportEventID(int supportEventID) {
        this.supportEventID = supportEventID;
    }

    public String getSupportEvent() {
        return supportEvent;
    }

    public void setSupportEvent(String supportEvent) {
        this.supportEvent = supportEvent;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCustomerUserID() {
        return customerUserID;
    }

    public void setCustomerUserID(int customerUserID) {
        this.customerUserID = customerUserID;
    }

    public String getCustomerUserName() {
        return customerUserName;
    }

    public void setCustomerUserName(String customerUserName) {
        this.customerUserName = customerUserName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRegTime() {
        return regTime;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerRegTime() {
        return answerRegTime;
    }

    public void setAnswerRegTime(String answerRegTime) {
        this.answerRegTime = answerRegTime;
    }
}
