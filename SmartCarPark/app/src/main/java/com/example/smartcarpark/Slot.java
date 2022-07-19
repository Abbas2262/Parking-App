package com.example.smartcarpark;

public class Slot {


    private String slotNumber;
    private String carNumber;
    private String slotTime;
    private String slotStatus;
    private String userName;

    public Slot()
    {

    }

    public Slot(String slotNumber, String carNumber, String slotTime, String slotStatus, String userName) {
        this.slotNumber = slotNumber;
        this.carNumber = carNumber;
        this.slotTime = slotTime;
        this.slotStatus = slotStatus;
        this.userName = userName;
    }

    public String getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(String slotNumber) {
        this.slotNumber = slotNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public String getSlotStatus() {
        return slotStatus;
    }

    public void setSlotStatus(String slotStatus) {
        this.slotStatus = slotStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
