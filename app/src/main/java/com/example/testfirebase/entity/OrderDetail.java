package com.example.testfirebase.entity;

public class OrderDetail {
    private int count;
    private Food id_Food;
    private Bill id_Bill;


    public OrderDetail(int count, Food id_Food, Bill id_Bill) {
        this.count = count;
        this.id_Food = id_Food;
        this.id_Bill = id_Bill;
    }

    public OrderDetail(Food id_Food) {
        this.id_Food = id_Food;
    }

    public OrderDetail(Food id_Food, Bill id_Bill) {
        this.id_Food = id_Food;
        this.id_Bill = id_Bill;
    }

    public OrderDetail() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Food getId_Food() {
        return id_Food;
    }

    public void setId_Food(Food id_Food) {
        this.id_Food = id_Food;
    }

    public Bill getId_Bill() {
        return id_Bill;
    }

    public void setId_Bill(Bill id_Bill) {
        this.id_Bill = id_Bill;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "count=" + count +
                ", id_Food=" + id_Food +
                ", id_Bill=" + id_Bill +
                '}';
    }
}
