package com.esh_tech.aviram.barbershop.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.esh_tech.aviram.barbershop.Appointment;
import com.esh_tech.aviram.barbershop.Codes.Customer;
import com.esh_tech.aviram.barbershop.Codes.Product;

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
            customersList.add(new Customer(customersCursor.getString(1),customersCursor.getLong(2)));
        }

        return customersList;
    }






//    /*Appointments Table.*/

//    Add an'Appointment to Database..
    public boolean addAppointment(Appointment newAppointment){
//        Opent the connection to database
    SQLiteDatabase db = dbHelper.getWritableDatabase();

//        Quer
    ContentValues columnValues = new ContentValues();

//    columnValues.put(BarbershopConstants.APPOINTMENT_ID,newAppointment.getAppointmentID());
    columnValues.put(BarbershopConstants.APPOINTMENT_MINUTE,newAppointment.getMinutes());
    columnValues.put(BarbershopConstants.APPOINTMENT_HOUR,newAppointment.getHour());
    columnValues.put(BarbershopConstants.APPOINTMENT_DAY,newAppointment.getDay());
    columnValues.put(BarbershopConstants.APPOINTMENT_MONTH,newAppointment.getMonth());
    columnValues.put(BarbershopConstants.APPOINTMENT_YEAR,newAppointment.getYear());
    columnValues.put(BarbershopConstants.CUSTOMER_ID,1);
    columnValues.put(BarbershopConstants.CUSTOMER_NAME,newAppointment.getCustomerName());


    long result =db.insert(BarbershopConstants.APPOINTMENTS_TABLE_NAME,null,columnValues);
    db.close();

    return (result != -1);
}

//Import Today appointments from Database
    public ArrayList<Appointment> getTodayAppointments(int day_x, int month_x, int year_x) {

    ArrayList<Appointment> appointmentsList =new ArrayList<Appointment>();
//        this open tbe connection to the database
        SQLiteDatabase db = dbHelper.getReadableDatabase();

//        Select * from customers table
        Cursor appointmentCursor = db.query(BarbershopConstants.APPOINTMENTS_TABLE_NAME,null,null,null,null,null,null);

//        each round in the loop is a record in the database.
        while (appointmentCursor.moveToNext()){
            appointmentsList.add(new Appointment(Integer.parseInt(appointmentCursor.getString(1)),
                    Integer.parseInt(appointmentCursor.getString(1)),
                    Integer.parseInt(appointmentCursor.getString(2)),
                    Integer.parseInt(appointmentCursor.getString(3)),
                    Integer.parseInt(appointmentCursor.getString(4)),
                    appointmentCursor.getString(6),
                    Integer.parseInt(appointmentCursor.getString((5)))
            ));
//            (int minutes, int hour, int day,int month, int year, String customerName, int customerID)
        }
        appointmentsList.add(new Appointment(111,1111,1111,1,1,"dd",23));

//        appointmentsList.get(1).toString();


        return appointmentsList;
}

    public ArrayList<Appointment> getNextAppointment() {

        ArrayList<Appointment> appointmentsList =new ArrayList<Appointment>();

        return appointmentsList;
    }










//    /*Products Table.*/

    //    Add an'Appointment to Database..+_+
    public boolean addProduct(Product newProduct){
//        Opent the connection to database
    SQLiteDatabase db = dbHelper.getWritableDatabase();

//        Quer
    ContentValues columnValues = new ContentValues();

    //columnValues.put(BarbershopConstants.PRODUCT_ID ,"1");
    columnValues.put(BarbershopConstants.PRODUCT_NAME ,newProduct.getName());
    columnValues.put(BarbershopConstants.PRODUCT_QUANTITY ,newProduct.getQuantity());
    columnValues.put(BarbershopConstants.PRODUCT_PRICE ,newProduct.getPrice());

    long result =db.insert(BarbershopConstants.PRODUCT_TABLE_NAME,null,columnValues);
    db.close();

    return (result != -1);
    }

    //Import all day appointments from Database
    public ArrayList<Product> getAllProducts(int year_x, int month_x, int day_x){

        ArrayList<Product> productsList =new ArrayList<Product>();


        return productsList;
    }

}
