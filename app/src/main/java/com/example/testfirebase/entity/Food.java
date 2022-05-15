package com.example.testfirebase.entity;

import java.io.Serializable;

public class Food implements Serializable {
    private String id;
    private String name;
    private double price;

    public Food(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Food(String id) {
        this.id = id;
    }

    public Food() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
