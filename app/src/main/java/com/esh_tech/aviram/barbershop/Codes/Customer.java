package com.esh_tech.aviram.barbershop.Codes;

/**
 * Created by AVIRAM on 22/03/2017.
 */

public class Customer {

    private String name;
    private String lastname;
    private String phone;
    private boolean gender;

    //pic , date , last time


    //Constructors

    public Customer(String name, String lastname, String phone, boolean gender) {
        this.name = name;
        this.lastname = lastname;
        this.phone = phone;
        this.gender = gender;
    }


    //Getter and Setter

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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
