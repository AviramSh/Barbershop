package com.esh_tech.aviram.barbershop.Database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Appointment;
import com.esh_tech.aviram.barbershop.AppointmentListActivity;
import com.esh_tech.aviram.barbershop.Constants.AppointmentsDBConstants;
import com.esh_tech.aviram.barbershop.Constants.CustomersDBConstants;
import com.esh_tech.aviram.barbershop.Constants.MainDBConstants;
import com.esh_tech.aviram.barbershop.Constants.ProductsDBConstants;
import com.esh_tech.aviram.barbershop.Customer;
import com.esh_tech.aviram.barbershop.Product;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by AVIRAM on 15/05/2017.
 */

public class BarbershopDBHandler {

    private MySQLiteHelper dbHelper;

    public BarbershopDBHandler(Context context) {
        dbHelper= new MySQLiteHelper(context, MainDBConstants.BARBERSHOP_DB_NAME,null, MainDBConstants.BARBERSHOP_DB_VERSION);
    }

    //    Add Customer ID(String name, String phone, String secondPhone, String email, Double bill, Bitmap photo, boolean gender, boolean remainder)
    public boolean addCustomer(Customer customer){
        long result = -1;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues columnValues = new ContentValues();

        columnValues.put(CustomersDBConstants.CUSTOMER_NAME,customer.getName());
        columnValues.put(CustomersDBConstants.CUSTOMER_PHONE,customer.getPhone());
        columnValues.put(CustomersDBConstants.CUSTOMER_SECOND_PHONE,customer.getSecondPhone());
        columnValues.put(CustomersDBConstants.CUSTOMER_EMAIL , customer.getEmail());
        columnValues.put(CustomersDBConstants.CUSTOMER_BILL , customer.getBill());
        columnValues.put(CustomersDBConstants.CUSTOMER_GENDER , customer.getGender());
        columnValues.put(CustomersDBConstants.CUSTOMER_REMAINDER , customer.getRemainder());

        if(customer.get_id() == -1) {
            result = db.insertOrThrow(CustomersDBConstants.CUSTOMERS_TABLE_NAME,
                    null,
                    columnValues);
        }else{
            result = db.update(CustomersDBConstants.CUSTOMERS_TABLE_NAME,
                    columnValues,
                    CustomersDBConstants.CUSTOMER_ID +"= "+customer.get_id(),
                    null);
        }
        db.close();

        return (result != -1);
    }
    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customersList = new ArrayList<Customer>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor customersCursor = db.query(CustomersDBConstants.CUSTOMERS_TABLE_NAME,null,null,null,null,null,null);

        while (customersCursor.moveToNext())
            customersList.add(new Customer(
                    customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_ID)),
                    customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_NAME)),
                    customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_PHONE)),
                    customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_SECOND_PHONE)),
                    customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_EMAIL)),
                    customersCursor.getDouble(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_BILL)),
                    customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_GENDER)),
                    customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_REMAINDER))

            ));

        return customersList;
    }
    public Customer getCustomerByID(int id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor customersCursor = db.query(CustomersDBConstants.CUSTOMERS_TABLE_NAME,null,null,null,null,null,null);

        while (customersCursor.moveToNext())

            if(customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_ID))==id){
            return new Customer(
                    customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_ID)),
                    customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_NAME)),
                    customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_PHONE)),
                    customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_SECOND_PHONE)),
                    customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_EMAIL)),
                    customersCursor.getDouble(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_BILL)),
                    customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_GENDER)),
                    customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_REMAINDER))
                );
            }
        return null;
    }
    public Customer getCustomerByPhone(String phone) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor customersCursor = db.query(CustomersDBConstants.CUSTOMERS_TABLE_NAME,null,null,null,null,null,null);

        while (customersCursor.moveToNext())

            if(phone.equals(customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_PHONE)))
                    || phone.equals(customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_SECOND_PHONE)))){
                return new Customer(
                        customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_ID)),
                        customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_NAME)),
                        customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_PHONE)),
                        customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_SECOND_PHONE)),
                        customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_EMAIL)),
                        customersCursor.getDouble(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_BILL)),
                        customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_GENDER)),
                        customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_REMAINDER))
                );
            }
        return null;
    }





    //    Add Appointment ID(Date theDate, Time theTime, int haircutTime, int customerID)
    public boolean addAppointment(Appointment appointment){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues columnValues = new ContentValues();

//        Set the date
        columnValues.put(AppointmentsDBConstants.APPOINTMENT_DATE,appointment.getDateAndTimeToDisplay());
        columnValues.put(AppointmentsDBConstants.CUSTOMER_ID,appointment.getCustomerID());


        long result = db.insertOrThrow(AppointmentsDBConstants.APPOINTMENTS_TABLE_NAME,null,columnValues);
        db.close();

        return (result != -1);
    }
    public ArrayList<Appointment> getAllAppointments() {
        ArrayList<Appointment> AppointmentsList = new ArrayList<Appointment>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor AppointmentsCursor = db.query(AppointmentsDBConstants.APPOINTMENTS_TABLE_NAME, null, null, null, null, null, null);

        while (AppointmentsCursor.moveToNext())
            AppointmentsList.add(new Appointment(
                    AppointmentsCursor.getInt(AppointmentsCursor.getColumnIndex(AppointmentsDBConstants.APPOINTMENT_ID)),
                    AppointmentsCursor.getString(AppointmentsCursor.getColumnIndex(AppointmentsDBConstants.APPOINTMENT_DATE)),
                    AppointmentsCursor.getInt(AppointmentsCursor.getColumnIndex(AppointmentsDBConstants.CUSTOMER_ID))
            ));

        return AppointmentsList;
    }

//    Get Appointments by a date.
    public ArrayList<Appointment> getAllAppointments(String receivedDate) {

        ArrayList<Appointment> myAppointments = getAllAppointments();
        ArrayList<Appointment> myDateAppointments = new ArrayList<Appointment>();

        for (Appointment appointment:
                myAppointments) {

            Log.d("The Getting Date : ",appointment.getDateAndTimeToDisplay());

                if(appointment.getDateAndTimeToDisplay().toLowerCase().contains(receivedDate)) {
//                    Log.d("found","found");
                    myDateAppointments.add(appointment);
                }
            }

        return myDateAppointments;
    }
//    Today appointments
    public ArrayList<Appointment> getTodayAppointments(Date myDate) {

        ArrayList<Appointment> myDateAppointments = new ArrayList<Appointment>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String newFormat = formatter.format(myDate.getTime());
        myDateAppointments = getAllAppointments(newFormat);


        return myDateAppointments;
}

    public ArrayList<String> getScheduledAppointments() {

        ArrayList<Appointment> myAppointments = getAllAppointments();
        ArrayList<Customer> myCustomers = getAllCustomers();
        ArrayList<String> myScheduledList = new ArrayList<String>();

        for (Appointment appointment:
                myAppointments) {
//            myScheduledList.add(myScheduledList(getAppointmentByCustomerId(customer.get_id())));
//
//            String selectQuery = "SELECT * FROM " +
//                    CustomersDBConstants.CUSTOMERS_TABLE_NAME + " WHERE "+
//                    CustomersDBConstants.CUSTOMER_ID +" == "+appointment.getCustomerID()+";";
            for (Customer customer :
                    myCustomers) {
                if (appointment.getCustomerID() == customer.get_id()){
                    myScheduledList.add(customer.getName()+" "+appointment.getDateAndTimeToDisplay());
                }
            }
        }

        return myScheduledList;
    }





    //    Add Product ID(String name, int quantity, double price)
    public boolean addProduct(Product product){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues columnValues = new ContentValues();

//        Set the product
        columnValues.put(ProductsDBConstants.PRODUCT_NAME,product.getName());
        columnValues.put(ProductsDBConstants.PRODUCT_QUANTITY,product.getQuantity());
        columnValues.put(ProductsDBConstants.PRODUCT_PRICE,product.getPrice());


        long result = db.insertOrThrow(ProductsDBConstants.PRODUCTS_TABLE_NAME,null,columnValues);
        db.close();

        return (result != -1);
    }
    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> productsList = new ArrayList<Product>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor ProductsCursor = db.query(ProductsDBConstants.PRODUCTS_TABLE_NAME, null, null, null, null, null, null);

        while (ProductsCursor.moveToNext())
            productsList.add(new Product(
                    ProductsCursor.getInt(ProductsCursor.getColumnIndex(ProductsDBConstants.PRODUCT_ID)),
                    ProductsCursor.getString(ProductsCursor.getColumnIndex(ProductsDBConstants.PRODUCT_NAME)),
                    ProductsCursor.getInt(ProductsCursor.getColumnIndex(ProductsDBConstants.PRODUCT_QUANTITY)),
                    ProductsCursor.getInt(ProductsCursor.getColumnIndex(ProductsDBConstants.PRODUCT_PRICE))
            ));

        return productsList;
    }

    public Product getProductByID(int id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor productCursor = db.query(ProductsDBConstants.PRODUCTS_TABLE_NAME,null,null,null,null,null,null);

        while (productCursor.moveToNext())

            if(productCursor.getInt(productCursor.getColumnIndex(ProductsDBConstants.PRODUCT_ID))==id){
                return new Product(
                        productCursor.getColumnIndex(ProductsDBConstants.PRODUCT_ID),
                        productCursor.getString(productCursor.getColumnIndex(ProductsDBConstants.PRODUCT_NAME)),
                        productCursor.getInt(productCursor.getColumnIndex(ProductsDBConstants.PRODUCT_QUANTITY)),
                        productCursor.getDouble(productCursor.getColumnIndex(ProductsDBConstants.PRODUCT_PRICE))
                );
            }
        return null;

    }

    public boolean upDateProduct(Product product) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues columnValues = new ContentValues();
        columnValues.put(ProductsDBConstants.PRODUCT_NAME,product.getName());
        columnValues.put(ProductsDBConstants.PRODUCT_QUANTITY,product.getQuantity());
        columnValues.put(ProductsDBConstants.PRODUCT_PRICE,product.getPrice());


        long result = db.update(ProductsDBConstants.PRODUCTS_TABLE_NAME,
                columnValues,
                ProductsDBConstants.PRODUCT_ID + "= "+product.get_id(),
                null);
        db.close();

        return (result != -1);
    }
}

//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.esh_tech.aviram.barbershop.Appointment;
//
//import java.util.ArrayList;
//
///**
// * Created by AVIRAM on 22/04/2017.
// */
//
//public class BarbershopDBHandler {
//
//    private MySQLiteHelper dbHelper;
//
//
//    public BarbershopDBHandler(Context context) {
//        dbHelper = new MySQLiteHelper(context, BarbershopConstants.BARBERSHOP_DB_NAME,null,BarbershopConstants.BARBERSHOP_VERSION);
//    }
//
//
////    /*Customers Table.*/
//
//
//    //Add customers to Database.
//    public boolean addCustomer(Appointment.Customer newCustomer){
//
////        Opent the connection to database
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
////        Quer
//        ContentValues columnValues = new ContentValues();
//
//        //columnValues.put(BarbershopConstants.CUSTOMER_ID ,"1");
//        columnValues.put(BarbershopConstants.CUSTOMER_NAME ,newCustomer.getName());
//        columnValues.put(BarbershopConstants.CUSTOMER_PHONE ,newCustomer.getPhone());
//
//        long result =db.insert(BarbershopConstants.CUSTOMERS_TABLE_NAME,null,columnValues);
//        db.close();
//
//        return (result != -1);
//    }
//
//    //Import all customers from Database
//    public ArrayList<Appointment.Customer> getAllCustomers(){
//        ArrayList<Appointment.Customer> customersList =new ArrayList<Appointment.Customer>();
////        this open tbe connection to the database
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
////        Select * from customers table
//        Cursor customersCursor = db.query(BarbershopConstants.CUSTOMERS_TABLE_NAME,null,null,null,null,null,null);
//
////        each rund in the loop is a record in the database.
//        while (customersCursor.moveToNext()){
//            customersList.add(new Appointment.Customer(customersCursor.getString(1),customersCursor.getString(2)));
//        }
//
//        return customersList;
//    }
//
//
//
//
//
//
////    /*Appointments Table.*/
//
////    Add an'Appointment to Database..
//    public boolean addAppointment(Appointment newAppointment){
////        Opent the connection to database
//    SQLiteDatabase db = dbHelper.getWritableDatabase();
//
////        Quer
//    ContentValues columnValues = new ContentValues();
//
////    columnValues.put(BarbershopConstants.APPOINTMENT_ID,newAppointment.getAppointmentID());
//    columnValues.put(BarbershopConstants.APPOINTMENT_MINUTE,newAppointment.getMinutes());
//    columnValues.put(BarbershopConstants.APPOINTMENT_HOUR,newAppointment.getHour());
//    columnValues.put(BarbershopConstants.APPOINTMENT_DAY,newAppointment.getDay());
//    columnValues.put(BarbershopConstants.APPOINTMENT_MONTH,newAppointment.getMonth());
//    columnValues.put(BarbershopConstants.APPOINTMENT_YEAR,newAppointment.getYear());
//    columnValues.put(BarbershopConstants.CUSTOMER_ID,1);
//    columnValues.put(BarbershopConstants.CUSTOMER_NAME,newAppointment.getCustomerName());
//
//
//    long result =db.insert(BarbershopConstants.APPOINTMENTS_TABLE_NAME,null,columnValues);
//    db.close();
//
//    return (result != -1);
//}
//
////Import Today appointments from Database
//    public ArrayList<Appointment> getTodayAppointments(int day_x, int month_x, int year_x) {
//
//    ArrayList<Appointment> appointmentsList =new ArrayList<Appointment>();
////        this open tbe connection to the database
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
////        Select * from customers table
//        Cursor appointmentCursor = db.query(BarbershopConstants.APPOINTMENTS_TABLE_NAME,null,null,null,null,null,null);
//
////        each round in the loop is a record in the database.
//        while (appointmentCursor.moveToNext()){
//            appointmentsList.add(new Appointment(appointmentCursor.getInt(appointmentCursor.getColumnIndex(BarbershopConstants.APPOINTMENT_MINUTE)),
//                    appointmentCursor.getInt(appointmentCursor.getColumnIndex(BarbershopConstants.APPOINTMENT_HOUR)),
//                    appointmentCursor.getInt(appointmentCursor.getColumnIndex(BarbershopConstants.APPOINTMENT_DAY)),
//                    appointmentCursor.getInt(appointmentCursor.getColumnIndex(BarbershopConstants.APPOINTMENT_MONTH)),
//                    appointmentCursor.getInt(appointmentCursor.getColumnIndex(BarbershopConstants.APPOINTMENT_YEAR)),
//                    appointmentCursor.getString(appointmentCursor.getColumnIndex(BarbershopConstants.CUSTOMER_NAME)),
//                    appointmentCursor.getInt(appointmentCursor.getColumnIndex(BarbershopConstants.CUSTOMER_ID)
//            )));
////            (int minutes, int hour, int day,int month, int year, String customerName, int customerID)
//        }
//        appointmentsList.add(new Appointment(111,1111,1111,1,1,"dd",23));
//
////        appointmentsList.get(1).toString();
//
//
//        return appointmentsList;
//}
//
//    public ArrayList<Appointment> getNextAppointment() {
//
//        ArrayList<Appointment> appointmentsList =new ArrayList<Appointment>();
//
//        return appointmentsList;
//    }
//
//
//
//
//
//
//
//
//
//
////    /*Products Table.*/
//
//    //    Add an'Appointment to Database..+_+
//    public boolean addProduct(Appointment.Product newProduct){
////        Opent the connection to database
//    SQLiteDatabase db = dbHelper.getWritableDatabase();
//
////        Quer
//    ContentValues columnValues = new ContentValues();
//
//    //columnValues.put(BarbershopConstants.PRODUCT_ID ,"1");
//    columnValues.put(BarbershopConstants.PRODUCT_NAME ,newProduct.getName());
//    columnValues.put(BarbershopConstants.PRODUCT_QUANTITY ,newProduct.getQuantity());
//    columnValues.put(BarbershopConstants.PRODUCT_PRICE ,newProduct.getPrice());
//
//    long result =db.insert(BarbershopConstants.PRODUCT_TABLE_NAME,null,columnValues);
//    db.close();
//
//    return (result != -1);
//    }
//
//    //Import all day appointments from Database
//    public ArrayList<Appointment.Product> getAllProducts(int year_x, int month_x, int day_x){
//
//        ArrayList<Appointment.Product> productsList =new ArrayList<Appointment.Product>();
//
//
//        return productsList;
//    }
//
//}
