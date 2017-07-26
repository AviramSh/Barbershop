package com.esh_tech.aviram.barbershop.data;
/**
 * Created by AVIRAM on 01/06/2017.
 */

public class Product {

    private int _id;
    private String name;
    private int quantity;
    private double price;

    public Product() {
        this._id = -1;
        this.name = "";
        this.quantity = 1;
        this.price = 1;
    }

    public Product(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public Product(int _id, String name, int quantity, double price) {
        this._id = _id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
