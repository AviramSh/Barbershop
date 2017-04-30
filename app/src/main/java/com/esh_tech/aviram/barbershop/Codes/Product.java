package com.esh_tech.aviram.barbershop.Codes;

/**
 * Created by AVIRAM on 30/04/2017.
 */

public class Product {

    private int productID;

    private String name;
    private int quantity;
    private float price;


    public Product(int productID, String name, int quantity, float price) {
        this.productID = productID;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }





    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}

