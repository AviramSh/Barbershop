package com.esh_tech.aviram.barbershop.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.session.PlaybackState;

import com.esh_tech.aviram.barbershop.Appointment;
import com.esh_tech.aviram.barbershop.Codes.Customer;

import java.util.ArrayList;

/**
 * Created by AVIRAM on 22/04/2017.
 */

public class BarbershopDBHandler {

    private MySQLiteHelper dbHelper;



    public BarbershopDBHandler(Context context) {
        dbHelper = new MySQLiteHelper(context, BarbershopConstants.BARBERSHOP_DB_NAME,null,BarbershopConstants.BARBERSHOP_VERSION);
    }


//    /*Customers Table.*/


    //Add customers to Database.
    public boolean addCustomer(Customer newCustomer){
//        Opent the connection to database
        SQLiteDatabase db = dbHelper.getWritableDatabase();

//        Quer
        ContentValues columnValues = new ContentValues();

        //columnValues.put(BarbershopConstants.CUSTOMER_ID ,"1");
        columnValues.put(BarbershopConstants.CUSTOMER_NAME ,newCustomer.getName());
        columnValues.put(BarbershopConstants.CUSTOMER_PHONE ,newCustomer.getPhone());

        long result =db.insert(BarbershopConstants.CUSTOMERS_TABLE_NAME,null,columnValues);
        db.close();

        return (result != -1);
    }

    //Import all customers from Database
    public ArrayList<Customer> getAllCustomers(){
        ArrayList<Customer> customersList =new ArrayList<Customer>();
//        this open tbe connection to the database
        SQLiteDatabase db = dbHelper.getReadableDatabase();

//        Select * from customers table
        Cursor customersCursor = db.query(BarbershopConstants.CUSTOMERS_TABLE_NAME,null,null,null,null,null,null);

//        each rund in the loop is a record in the database.
        while (customersCursor.moveToNext()){
            customersList.add(new Customer(customersCursor.getString(1),customersCursor.getString(2)));
        }

        return customersList;
    }






//    /*Appointments Table.*/

//    Add an'Appointment to Database..
public boolean addApointment(Appointment newAppointment){
//        Opent the connection to database
    SQLiteDatabase db = dbHelper.getWritableDatabase();

//        Quer
    ContentValues columnValues = new ContentValues();

    /*columnValues.put(BarbershopConstants.CUSTOMER_ID ,"1");
    columnValues.put(BarbershopConstants.CUSTOMER_NAME ,newAppointment.getName());
    columnValues.put(BarbershopConstants.CUSTOMER_PHONE ,newAppointment.getPhone());

    long result =db.insert(BarbershopConstants.CUSTOMERS_TABLE_NAME,null,columnValues);
    db.close();

    return (result != -1);*/return true;
}

//Import all day appointments from Database
public ArrayList<Appointment> getDayAppointments(int year_x, int month_x, int day_x){

    ArrayList<Appointment> appointmentsList =new ArrayList<Appointment>();


    return appointmentsList;
}

}
