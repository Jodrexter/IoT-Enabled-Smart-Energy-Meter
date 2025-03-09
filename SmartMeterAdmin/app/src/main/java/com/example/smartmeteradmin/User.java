package com.example.smartmeteradmin;

public class User {
    String name,mail;
    String mobile,pass;
    String totalUnit;
    Double tUnit;

    public User(String name, String mail, String mobile, String pass) {
        this.name = name;
        this.mail = mail;
        this.mobile = mobile;
        this.pass = pass;
    }

    public User(String name, String totalUnit,Double tUnit) {
        this.name = name;
        this.totalUnit = totalUnit;
        this.tUnit=tUnit;
    }

    public Double gettUnit() {
        return tUnit;
    }

    public void settUnit(Double tUnit) {
        this.tUnit = tUnit;
    }

    public String getTotalUnit() {
        return totalUnit;
    }

    public void setTotalUnit(String totalUnit) {
        this.totalUnit = totalUnit;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public User(String name, String mail) {
        this.name = name;
        this.mail = mail;
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
}
