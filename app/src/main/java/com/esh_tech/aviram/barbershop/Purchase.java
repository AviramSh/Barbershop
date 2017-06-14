package com.esh_tech.aviram.barbershop;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by AVIRAM on 14/06/2017.
 */

public class Purchase {


    private int id;
    private int appointmentID;
    private int productID;
    private int customerID;
    private String date;
    private double price;

//    Constructors

    public Purchase() {
        this(-1,0,0,-1,"",0.0);
    }

    public Purchase(int id, int appointmentID, int productID,int customerID,String date, double price) {
        this.id = id;
        this.appointmentID = appointmentID;
        this.productID = productID;
        this.customerID = customerID;
        this.date = date;
        this.price = price;
    }



//    Getter And Setter


    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
//    PurchaseDBConstants.APPOINTMENT_ID + " INTEGER , "+
//    PurchaseDBConstants.PRODUCT_ID + " INTEGER , "+
//    PurchaseDBConstants.PURCHASE_PRICE + " DOUBLE "
}
