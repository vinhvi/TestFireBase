package com.example.testfirebase.entity;

import com.example.testfirebase.entity.User;

public class Bill {
    private String id;
    private User id_user;


    public Bill(String id, User id_user) {
        this.id = id;
        this.id_user = id_user;
    }

    public Bill() {
    }


    public Bill(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getId_user() {
        return id_user;
    }

    public void setId_user(User id_user) {
        this.id_user = id_user;
    }
}
