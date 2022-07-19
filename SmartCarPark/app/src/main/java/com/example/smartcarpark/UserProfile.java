package com.example.smartcarpark;

public class UserProfile {

    public String userName;
    public String userEmail;
    public String userCar;
    public String userImage;
    public String uId;

    public UserProfile(){

    }

    public UserProfile(String userName, String userEmail, String userCar, String userImage) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userCar = userCar;
        this.userImage = userImage;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserCar() {
        return userCar;
    }

    public void setUserCar(String userCar) {
        this.userCar = userCar;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
