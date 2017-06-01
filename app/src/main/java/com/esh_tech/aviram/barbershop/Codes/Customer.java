package com.esh_tech.aviram.barbershop.Codes;

import android.graphics.Bitmap;

/**
 * Created by AVIRAM on 22/03/2017.
 */

public class Customer {


    private int id;
    private String name;
    private String phone;
    private String secondPhone;
    private String email;
    private int bill;
    private boolean gender =true;
    private boolean remainder;
    private Bitmap customerPhoto;


    //pic , date , last time


    //Constructors



    public Customer(String name, String phone, String secondPhone, String email, int bill, boolean gender, boolean remainder, Bitmap customerPhoto) {
        this.name = name;
        this.phone = phone;
        this.secondPhone = secondPhone;
        this.email = email;
        this.bill = bill;
        this.gender = gender;
        this.remainder = remainder;
        this.customerPhoto = customerPhoto;
    }

    public Customer(String name, String phone) {

        this(name,phone,true);
    }

    public Customer(String name, String phone, boolean gender) {
        this.name = name;
        this.phone = phone;
        this.gender = gender;
    }


    public Customer() {
        this("", "", "", "", 0, true, false, null);
    }


    //Getter and Setter


    public int getId() {
        return id;
    }

    public int getBill() {
        return bill;
    }

    public void setBill(int bill) {
        this.bill = bill;
    }

    public boolean isRemainder() {
        return remainder;
    }

    public void setRemainder(boolean remainder) {
        this.remainder = remainder;
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

    public Bitmap getCustomerPhoto() {
        return customerPhoto;
    }

    public void setCustomerPhoto(Bitmap customerPhoto) {
        this.customerPhoto = customerPhoto;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }
}
