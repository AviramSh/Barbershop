package com.esh_tech.aviram.barbershop.Codes;

import android.graphics.Bitmap;

/**
 * Created by AVIRAM on 22/03/2017.
 */

public class Customer {


    private int id;
    private String name;
    private long phone;
    private long secondPhone;
    private String email;
    private int bill;
    private boolean gender;
    private boolean remainder;
    private Bitmap CustomerPhoto;


    //pic , date , last time


    //Constructors


    public Customer(String name, long phone, long secondPhone, String email, int bill, boolean gender, boolean remainder) {
        this.name = name;
        this.phone = phone;
        this.secondPhone = secondPhone;
        this.email = email;
        this.bill = bill;
        this.gender = gender;
        this.remainder = remainder;
    }

    public Customer(String name, long phone, long secondPhone, String email, int bill, boolean gender, boolean remainder, Bitmap customerPhoto) {
        this.name = name;
        this.phone = phone;
        this.secondPhone = secondPhone;
        this.email = email;
        this.bill = bill;
        this.gender = gender;
        this.remainder = remainder;
        CustomerPhoto = customerPhoto;
    }

    public Customer(String name, long phone, int bill) {
        this.name = name;
        this.phone = phone;
        this.bill = bill;
    }

    public Customer(String name, long phone, String email, boolean gender, Bitmap customerPhoto) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        CustomerPhoto = customerPhoto;
    }

    public Customer(String name, long phone) {

        this(name,phone,true);
    }

    public Customer(String name, long phone, String email, boolean gender) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
    }

    public Customer(String name, long phone, boolean gender) {
        this.name = name;
        this.phone = phone;
        this.gender = gender;
    }

    public Customer(String name, String lastname, long phone) {
        this(name,phone,true);
    }

    public Customer(String name, long phone, String email, boolean gender, boolean remainder, Bitmap customerPhoto) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.gender = gender;
        this.remainder = remainder;
        CustomerPhoto = customerPhoto;
    }

    public Customer() {

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
        return CustomerPhoto;
    }

    public void setCustomerPhoto(Bitmap customerPhoto) {
        CustomerPhoto = customerPhoto;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }
}
