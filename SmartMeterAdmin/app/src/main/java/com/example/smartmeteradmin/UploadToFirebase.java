package com.example.smartmeteradmin;

public class UploadToFirebase {
    private String name,mail,userName,Pass;

    public UploadToFirebase(String name, String mail, String userName, String pass) {
        this.name = name;
        this.mail = mail;
        this.userName = userName;
        Pass = pass;
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
}
