package com.esh_tech.aviram.barbershop.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.esh_tech.aviram.barbershop.Constants.AppointmentsDBConstants;
import com.esh_tech.aviram.barbershop.Constants.CustomersDBConstants;
import com.esh_tech.aviram.barbershop.Constants.PicturesDBConstants;
import com.esh_tech.aviram.barbershop.Constants.ProductsDBConstants;
import com.esh_tech.aviram.barbershop.Constants.PurchaseDBConstants;


/**
 * Created by AVIRAM on 20/04/2017.
 * Create Tables Class.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {


//        CREATING USERS TABLE.Customer(String name, String last, String phone, String secondPhone, String email, Double bill, Bitmap photo, boolean gender, boolean remainder)
        db.execSQL("CREATE TABLE " + CustomersDBConstants.CUSTOMERS_TABLE_NAME + "(" +
                CustomersDBConstants.CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT  , "+
                CustomersDBConstants.CUSTOMER_NAME + " TEXT , "+
                CustomersDBConstants.CUSTOMER_PHONE + " TEXT ,"  +
                CustomersDBConstants.CUSTOMER_BIRTHDAY + " TEXT , "+
                CustomersDBConstants.CUSTOMER_EMAIL + " TEXT , "+
                CustomersDBConstants.CUSTOMER_BILL + " DOUBLE , "+
                CustomersDBConstants.CUSTOMER_GENDER + " INTEGER , "+
                CustomersDBConstants.CUSTOMER_REMAINDER + " INTEGER "
                +");");


//        CREATING APPOINTMENTS TABLE. Appointment(Date theDate, Time theTime, int haircutTime, int customerID)
        db.execSQL("CREATE TABLE " + AppointmentsDBConstants.APPOINTMENTS_TABLE_NAME +"("+
                AppointmentsDBConstants.APPOINTMENT_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT  , "+
                AppointmentsDBConstants.APPOINTMENT_DATE + " TEXT ,"+
                CustomersDBConstants.CUSTOMER_ID + " INTEGER , "+
                AppointmentsDBConstants.APPOINTMENT_EXECUTED + " INTEGER "
                +");");

        //        CREATING PRODUCTS TABLE. Products(String name, int quantity, double price)
        db.execSQL("CREATE TABLE " + ProductsDBConstants.PRODUCTS_TABLE_NAME+"("+
                ProductsDBConstants.PRODUCT_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT  , "+
                ProductsDBConstants.PRODUCT_NAME + " TEXT , "+
                ProductsDBConstants.PRODUCT_QUANTITY + " INTEGER , "+
                ProductsDBConstants.PRODUCT_PRICE + " DOUBLE "
                +");");





        //        CREATING PURCHASE TABLE. Products(String name, int quantity, double price)
        db.execSQL("CREATE TABLE " + PurchaseDBConstants.PURCHASES_TABLE_NAME+"("+
                PurchaseDBConstants.PURCHASE_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT  , "+
                PurchaseDBConstants.APPOINTMENT_ID + " INTEGER , "+
                PurchaseDBConstants.PRODUCT_ID + " INTEGER , "+
                PurchaseDBConstants.CUSTOMER_ID + " INTEGER , "+
                PurchaseDBConstants.PURCHASE_DATE + " TEXT ,"+
                PurchaseDBConstants.PURCHASE_PRICE + " DOUBLE "
                +");");

        //        CREATING PICTURE TABLE.
        db.execSQL("CREATE TABLE " + PicturesDBConstants.PICTURES_TABLE_NAME+"("+
                PicturesDBConstants.PICTURE_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT  , "+
                PicturesDBConstants.PICTURE_NAME + " TEXT ,"+
                PicturesDBConstants.PICTURE_DATA + " BLOB , "+
                PicturesDBConstants.CUSTOMER_ID + " INTEGER "
                +");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + CustomersDBConstants.CUSTOMERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + AppointmentsDBConstants.APPOINTMENTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProductsDBConstants.PRODUCTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PurchaseDBConstants.PURCHASES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PicturesDBConstants.PICTURES_TABLE_NAME);

        onCreate(db);
    }
}
