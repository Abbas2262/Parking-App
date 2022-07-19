package com.example.smartcarpark;

public class SlotHistory {
    private String userName, userCarNo, slotNo, userEmail, bookTime, cancelTime, recordId;

    public SlotHistory() {
    }

    public SlotHistory(String userName, String userCarNo, String slotNo, String userEmail, String bookTime, String cancelTime, String recordId) {
        this.userName = userName;
        this.userCarNo = userCarNo;
        this.slotNo = slotNo;
        this.userEmail = userEmail;
        this.bookTime = bookTime;
        this.cancelTime = cancelTime;
        this.recordId = recordId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCarNo() {
        return userCarNo;
    }

    public void setUserCarNo(String userCarNo) {
        this.userCarNo = userCarNo;
    }

    public String getSlotNo() {
        return slotNo;
    }

    public void setSlotNo(String slotNo) {
        this.slotNo = slotNo;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}
