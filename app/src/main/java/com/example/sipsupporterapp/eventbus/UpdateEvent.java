package com.example.sipsupporterapp.eventbus;

public class UpdateEvent {
    private int attachID;

    public UpdateEvent(int attachID) {
        this.attachID = attachID;
    }

    public int getAttachID() {
        return attachID;
    }

    public void setAttachID(int attachID) {
        this.attachID = attachID;
    }
}
