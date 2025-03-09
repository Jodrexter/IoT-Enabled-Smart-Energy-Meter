package com.example.smartmeter;

public class UploadToFirebase {
    private String name,mail,userName,Pass;
    private long userNumber;

    private String uName,UEmail,UMobile,UDeviceN,UPass;
    private Boolean flag;

    public UploadToFirebase(String uName, String UEmail, String UMobile,String UPass, String UDeviceN, Boolean flag) {
        this.uName = uName;
        this.UEmail = UEmail;
        this.UMobile = UMobile;
        this.UPass=UPass;
        this.UDeviceN = UDeviceN;
        this.flag = flag;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getUEmail() {
        return UEmail;
    }

    public void setUEmail(String UEmail) {
        this.UEmail = UEmail;
    }

    public String getUMobile() {
        return UMobile;
    }

    public void setUMobile(String UMobile) {
        this.UMobile = UMobile;
    }

    public String getUDeviceN() {
        return UDeviceN;
    }

    public void setUDeviceN(String UDeviceN) {
        this.UDeviceN = UDeviceN;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public UploadToFirebase(String name, String mail, String userName, String pass, long userNumber) {
        this.name = name;
        this.mail = mail;
        this.userName = userName;
        Pass = pass;
        this.userNumber = userNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public long getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(long userNumber) {
        this.userNumber = userNumber;
    }
}
