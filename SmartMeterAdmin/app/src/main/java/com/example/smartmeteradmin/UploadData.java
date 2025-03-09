package com.example.smartmeteradmin;

public class UploadData {
    String name,email,mobile,pass,dNo;
    Boolean flag;

    public UploadData(String name, String email, String mobile, String pass, String dNo, Boolean flag) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.pass = pass;
        this.dNo = dNo;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getdNo() {
        return dNo;
    }

    public void setdNo(String dNo) {
        this.dNo = dNo;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
