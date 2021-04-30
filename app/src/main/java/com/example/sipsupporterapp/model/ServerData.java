package com.example.sipsupporterapp.model;

public class ServerData {

    private int primaryKey;
    private String centerName;
    private String ipAddress;
    private String port;

    public ServerData(String centerName, String ipAddress, String port) {
        this.centerName = centerName;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public ServerData() {
    }

    public int getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(int primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
