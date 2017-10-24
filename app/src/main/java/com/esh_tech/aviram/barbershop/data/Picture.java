package com.esh_tech.aviram.barbershop.data;

import android.graphics.Bitmap;

import com.esh_tech.aviram.barbershop.Utils.DateUtils;

import java.util.Calendar;

/**
 * Created by Aviram on 24/10/2017.
 */

public class Picture {

    private int id;
    private String name;
    private Bitmap bitmapImageData;
    private int customerId;

    public Picture(int id, Calendar name, Bitmap bitmapImageData, int customerId) {
        this.id = id;
        this.name = DateUtils.getPicName(name);
        this.bitmapImageData = bitmapImageData;
        this.customerId = customerId;
    }
    public Picture(int id, String name, Bitmap bitmapImageData, int customerId) {
        this.id = id;
        this.name = name;
        this.bitmapImageData = bitmapImageData;
        this.customerId = customerId;
    }

    public Picture(Calendar name, Bitmap bitmapImageData, int customerId) {

        this.name = DateUtils.getPicName(name);
        this.bitmapImageData = bitmapImageData;
        this.customerId = customerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmapImageData() {
        return bitmapImageData;
    }

    public void setBitmapImageData(Bitmap bitmapImageData) {
        this.bitmapImageData = bitmapImageData;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
}
