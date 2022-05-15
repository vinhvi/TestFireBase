package com.example.testfirebase.entity;

public class Ram {
    private User id_user;
    private Bill id_bill;

    public Ram(User id_user) {
        this.id_user = id_user;
    }

    public Ram() {
    }

    public Ram(User id_user, Bill id_bill) {
        this.id_user = id_user;
        this.id_bill = id_bill;
    }

    public User getId_user() {
        return id_user;
    }

    public void setId_user(User id_user) {
        this.id_user = id_user;
    }

    public Bill getId_bill() {
        return id_bill;
    }

    public void setId_bill(Bill id_bill) {
        this.id_bill = id_bill;
    }

    @Override
    public String toString() {
        return "Ram{" +
                "id_user=" + id_user +
                ", id_bill=" + id_bill +
                '}';
    }
}
