package com.esh_tech.aviram.barbershop.data;

import android.graphics.Bitmap;

/**
 * Created by AVIRAM on 15/04/2017.
 */

public class Customer {

    private int _id;
    private String name;
    private String phone;
    private String birthday;
    private String email;
    private double bill;


    private int gender;
    private int remainder;
    private Bitmap photo;


    public Customer() {

        this(-1,"Guest","","1/1/1900","",0.0,1,0);
    }

    public Customer(String name, String phone, String birthday, String email, Double bill, Bitmap photo, int gender, int remainder) {
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
        this.email = email;
        this.bill = bill;
        this.photo = photo;
        this.gender = gender;
        this.remainder = remainder;
    }

    public Customer(int _id, String name, String phone, String birthday, String email, double bill, int gender, int remainder) {
        this._id = _id;
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
        this.email = email;
        this.bill = bill;
        this.gender = gender;
        this.remainder = remainder;
    }

    public Customer(String name, String phone, String birthday, String email, double bill, int gender, int remainder) {
        this.name = name;
        this.phone = phone;
        this.birthday = birthday;
        this.email = email;
        this.bill = bill;
        this.gender = gender;
        this.remainder = remainder;
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
        if(name==null || name.equals(""))
            this.name = "Guest";
        else this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {

        if(phone == null || phone.equals(""))
            this.phone = "0505000000";
        else this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        if(birthday == null || birthday == "")
            this.birthday = "0505000000";
        else this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if(email == null || email == "")
            this.email = "0505000000";
        else this.email = email;
    }

    public double getBill() {
        return bill;
    }

    public void setBill(double bill) {
        this.bill = bill;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        if (gender!=1&&gender!=0)
            this.gender = 1;
        else this.gender = gender;
    }

    public int getRemainder() {
        return remainder;
    }

    public void setRemainder(int remainder) {
        if (remainder !=0 && remainder!=1)
            this.remainder = 0;
        else
            this.remainder = remainder;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }
}
