package com.example.testfirebase.entity;

import android.net.Uri;

public class User {
    private String id;
    private String name;
    private String email;
    private String sdt;
    private String dc;
    private String pass;
    private String image;

    public User(String id, String name, String email, String sdt, String dc, String pass, String image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.sdt = sdt;
        this.dc = dc;
        this.pass = pass;
        this.image = image;
    }

    public User(String name, String email, String sdt, String dc, String pass, String image) {
        this.name = name;
        this.email = email;
        this.sdt = sdt;
        this.dc = dc;
        this.pass = pass;
        this.image = image;
    }

    public User(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
