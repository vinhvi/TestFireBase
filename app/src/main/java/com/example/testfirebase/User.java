package com.example.testfirebase;

public class User {
    private String name;
    private String email;
    private String sdt;
    private String dc;
    private String pass;

    public User(String name, String email, String sdt, String dc, String pass) {
        this.name = name;
        this.email = email;
        this.sdt = sdt;
        this.dc = dc;
        this.pass = pass;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDc() {
        return dc;
    }

    public void setDc(String dc) {
        this.dc = dc;
    }
}
