package com.esh_tech.aviram.barbershop.Database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.util.Log;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Appointment;
import com.esh_tech.aviram.barbershop.AppointmentListActivity;
import com.esh_tech.aviram.barbershop.Constants.AppointmentsDBConstants;
import com.esh_tech.aviram.barbershop.Constants.CustomersDBConstants;
import com.esh_tech.aviram.barbershop.Constants.MainDBConstants;
import com.esh_tech.aviram.barbershop.Constants.ProductsDBConstants;
import com.esh_tech.aviram.barbershop.Constants.PurchaseDBConstants;
import com.esh_tech.aviram.barbershop.Customer;
import com.esh_tech.aviram.barbershop.Product;
import com.esh_tech.aviram.barbershop.Purchase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
        columnValues.put(AppointmentsDBConstants.APPOINTMENT_EXECUTED,appointment.getTackAnHaircut());


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
                    AppointmentsCursor.getInt(AppointmentsCursor.getColumnIndex(AppointmentsDBConstants.CUSTOMER_ID)),
                    AppointmentsCursor.getInt(AppointmentsCursor.getColumnIndex(AppointmentsDBConstants.APPOINTMENT_EXECUTED))
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



//    PURCHASE

    //    Add Purchase ID((int id, int appointmentID, int productID, double price)
    public boolean addPurchase(Purchase purchase){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues columnValues = new ContentValues();

//        Set the product
        columnValues.put(PurchaseDBConstants.APPOINTMENT_ID , purchase.getAppointmentID());
        columnValues.put(PurchaseDBConstants.PRODUCT_ID , purchase.getProductID());
        columnValues.put(PurchaseDBConstants.PURCHASE_PRICE, purchase.getPrice());

        SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        dateFormat.format(date);

        columnValues.put(PurchaseDBConstants.PURCHASE_DATE,dateFormat.format(date));



        long result = db.insertOrThrow(ProductsDBConstants.PRODUCTS_TABLE_NAME,null,columnValues);
        db.close();

        return (result != -1);
    }
    public ArrayList<Purchase> getAllPurchase() {
        ArrayList<Purchase> PurchasesList = new ArrayList<Purchase>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor ProductsCursor = db.query(PurchaseDBConstants.PURCHASES_TABLE_NAME, null, null, null, null, null, null);

        while (ProductsCursor.moveToNext())
            PurchasesList.add(new Purchase(
                    ProductsCursor.getInt(ProductsCursor.getColumnIndex(PurchaseDBConstants.PURCHASE_ID)),
                    ProductsCursor.getInt(ProductsCursor.getColumnIndex(PurchaseDBConstants.APPOINTMENT_ID)),
                    ProductsCursor.getInt(ProductsCursor.getColumnIndex(PurchaseDBConstants.PRODUCT_ID)),
                    ProductsCursor.getInt(ProductsCursor.getColumnIndex(PurchaseDBConstants.CUSTOMER_ID)),
                    ProductsCursor.getString(ProductsCursor.getColumnIndex(PurchaseDBConstants.PURCHASE_DATE)),
                    ProductsCursor.getDouble(ProductsCursor.getColumnIndex(PurchaseDBConstants.PURCHASE_PRICE))
            ));

        return PurchasesList;
    }

    public Product getPurchaseByAppointmentID(int id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor purchaseCursor = db.query(PurchaseDBConstants.PURCHASES_TABLE_NAME,null,null,null,null,null,null);

        while (purchaseCursor.moveToNext())

            if(purchaseCursor.getInt(purchaseCursor.getColumnIndex(PurchaseDBConstants.APPOINTMENT_ID))==id){
                return new Product(
                        purchaseCursor.getColumnIndex(ProductsDBConstants.PRODUCT_ID),
                        purchaseCursor.getString(purchaseCursor.getColumnIndex(ProductsDBConstants.PRODUCT_NAME)),
                        purchaseCursor.getInt(purchaseCursor.getColumnIndex(ProductsDBConstants.PRODUCT_QUANTITY)),
                        purchaseCursor.getDouble(purchaseCursor.getColumnIndex(ProductsDBConstants.PRODUCT_PRICE))
                );
            }
        return null;

    }
    public Product getPurchaseByProductID(int id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor purchaseCursor = db.query(PurchaseDBConstants.PURCHASES_TABLE_NAME,null,null,null,null,null,null);

        while (purchaseCursor.moveToNext())

            if(purchaseCursor.getInt(purchaseCursor.getColumnIndex(PurchaseDBConstants.PRODUCT_ID))==id){
                return new Product(
                        purchaseCursor.getColumnIndex(ProductsDBConstants.PRODUCT_ID),
                        purchaseCursor.getString(purchaseCursor.getColumnIndex(ProductsDBConstants.PRODUCT_NAME)),
                        purchaseCursor.getInt(purchaseCursor.getColumnIndex(ProductsDBConstants.PRODUCT_QUANTITY)),
                        purchaseCursor.getDouble(purchaseCursor.getColumnIndex(ProductsDBConstants.PRODUCT_PRICE))
                );
            }
        return null;

    }

//    Need to sort the dates
    public ArrayList<Purchase> getPurchaseByDate(Calendar startDate , Calendar endDate) {

        ArrayList<Purchase> myPurchase = new ArrayList<Purchase>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor purchaseCursor = db.query(PurchaseDBConstants.PURCHASES_TABLE_NAME,null,null,null,null,null,null);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());


//        Purchase(int id, int appointmentID, int productID,int customerID,String date, double price)
        while (startDate.getTimeInMillis() <= endDate.getTimeInMillis()) {
            purchaseCursor.moveToFirst();
            String testFormat = formatter.format(startDate.getTime());
            while (purchaseCursor.moveToNext())

                if (purchaseCursor.getString(purchaseCursor.getColumnIndex(PurchaseDBConstants.PURCHASE_DATE)).contains(testFormat)) {
                    myPurchase.add(new Purchase(
                            purchaseCursor.getInt(purchaseCursor.getColumnIndex(PurchaseDBConstants.PURCHASE_ID)) ,
                            purchaseCursor.getInt(purchaseCursor.getColumnIndex(PurchaseDBConstants.APPOINTMENT_ID)) ,
                            purchaseCursor.getInt(purchaseCursor.getColumnIndex(PurchaseDBConstants.PRODUCT_ID)) ,
                            purchaseCursor.getInt(purchaseCursor.getColumnIndex(PurchaseDBConstants.CUSTOMER_ID)) ,
                            purchaseCursor.getString(purchaseCursor.getColumnIndex(PurchaseDBConstants.PURCHASE_DATE)),
                            purchaseCursor.getDouble(purchaseCursor.getColumnIndex(PurchaseDBConstants.PURCHASE_PRICE))
                    ));
                }
            startDate.add(Calendar.DAY_OF_MONTH,1);
        }
        return myPurchase;
    }
}
