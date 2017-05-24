package com.esh_tech.aviram.barbershop;

import android.widget.Toast;

/**
 * Created by AVIRAM on 25/04/2017.
 */

public class Appointment {

    private int appointmentID;

    private int year;
    private int month;
    private int  day;

    private int  hour;
    private int minutes;

    private int haircutTime;

    private String customerName;
    private int customerID;

//    Constructor
    public Appointment(int appointmentID, int year, int month, int day, int hour, int minutes, int haircutTime, String customerName, int customerID) {
        this.appointmentID = appointmentID;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minutes = minutes;
        this.haircutTime = haircutTime;
        this.customerName = customerName;
        this.customerID = customerID;
    }

    public Appointment() {

    }

    public Appointment(int minutes, int hour, int day,int month, int year, String customerName, int customerID) {
        this.minutes = minutes;
        this.hour = hour;
        this.day = day;
        this.month = month;
        this.year = year;
        this.customerName = customerName;
        this.customerID = customerID;
    }


    //    Getter and Setter
    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHaircutTime() {
        return haircutTime;
    }

    public void setHaircutTime(int haircutTime) {
        this.haircutTime = haircutTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }


}
