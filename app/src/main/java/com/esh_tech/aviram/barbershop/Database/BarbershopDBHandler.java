package com.esh_tech.aviram.barbershop.Database;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.*;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.MessageDBConstants;
import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.R;
import com.esh_tech.aviram.barbershop.Utils.DateUtils;
import com.esh_tech.aviram.barbershop.data.*;
import com.esh_tech.aviram.barbershop.Constants.AppointmentsDBConstants;
import com.esh_tech.aviram.barbershop.Constants.CustomersDBConstants;
import com.esh_tech.aviram.barbershop.Constants.MainDBConstants;
import com.esh_tech.aviram.barbershop.Constants.PicturesDBConstants;
import com.esh_tech.aviram.barbershop.Constants.ProductsDBConstants;
import com.esh_tech.aviram.barbershop.Constants.PurchaseDBConstants;
import com.esh_tech.aviram.barbershop.data.Picture;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by AVIRAM on 15/05/2017.
 */

public class BarbershopDBHandler {


    SharedPreferences settings;
    private MySQLiteHelper dbHelper;
    private Context context;
    private static final String TAG="MyDebug";

    public BarbershopDBHandler(Context context) {
        this.context = context;
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        dbHelper= new MySQLiteHelper(context, MainDBConstants.BARBERSHOP_DB_NAME,null, MainDBConstants.BARBERSHOP_DB_VERSION);
    }

    //    Add Customer ID(String name, String phone, String secondPhone, String email, Double bill, Bitmap photo, boolean gender, boolean remainder)
    public boolean addCustomer(Customer customer){
        settings = PreferenceManager.getDefaultSharedPreferences(context);
        long result = -1;

        if (getCustomerByPhone(customer.getPhone())!=null) {
            Toast.makeText(context, R.string.customerExist, Toast.LENGTH_SHORT).show();
            return false;
        }
        /*if (customer.get_id()==1 && settings.getBoolean(UserDBConstants.USER_IS_REGISTER,false)){
            updateCustomer(customer);
            return updateCustomer(customer);
        }*/

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues columnValues = new ContentValues();

        columnValues.put(CustomersDBConstants.CUSTOMER_NAME,customer.getName());
        columnValues.put(CustomersDBConstants.CUSTOMER_PHONE,customer.getPhone());
        columnValues.put(CustomersDBConstants.CUSTOMER_BIRTHDAY,customer.getBirthday());
        columnValues.put(CustomersDBConstants.CUSTOMER_EMAIL , customer.getEmail());
        columnValues.put(CustomersDBConstants.CUSTOMER_BILL , customer.getBill());
        columnValues.put(CustomersDBConstants.CUSTOMER_GENDER , customer.getGender());
        columnValues.put(CustomersDBConstants.CUSTOMER_REMAINDER , customer.getRemainder());

        if(getCustomerByID(customer.get_id())==null) {
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

        if(customer.getPhoto()!=null){
            Customer p1 = getCustomerByPhone(customer.getPhone());
            if(p1!=null)
                addPicture(new Picture(Calendar.getInstance(),customer.getPhoto(),p1.get_id()));
        }

        Log.d(TAG,"Customer Added "+customer);
        return (result != -1);
    }
    public boolean updateCustomer(Customer customer){
        long result = -1;

//        if(customer.get_id()!=0)
//            if (getCustomerByPhone(customer.getPhone())==null) {
//                Toast.makeText(context, R.string.customerDoesntExist, Toast.LENGTH_SHORT).show();
//                return false;
//            }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues columnValues = new ContentValues();

        columnValues.put(CustomersDBConstants.CUSTOMER_NAME,customer.getName());
        columnValues.put(CustomersDBConstants.CUSTOMER_PHONE,customer.getPhone());
        columnValues.put(CustomersDBConstants.CUSTOMER_BIRTHDAY,customer.getBirthday());
        columnValues.put(CustomersDBConstants.CUSTOMER_EMAIL , customer.getEmail());
        columnValues.put(CustomersDBConstants.CUSTOMER_BILL , customer.getBill());
        columnValues.put(CustomersDBConstants.CUSTOMER_GENDER , customer.getGender());
        columnValues.put(CustomersDBConstants.CUSTOMER_REMAINDER , customer.getRemainder());

        result = db.update(CustomersDBConstants.CUSTOMERS_TABLE_NAME,
                columnValues,
                CustomersDBConstants.CUSTOMER_ID +"= "+customer.get_id(),
                null);

        db.close();
        Log.d(TAG,"Update Customer "+customer);
        return (result != -1);
    }


    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customersList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor customersCursor = db.query(CustomersDBConstants.CUSTOMERS_TABLE_NAME,
                null,null,null,null,null,CustomersDBConstants.CUSTOMER_NAME+" ASC");
//        customersCursor.moveToNext();

        while (customersCursor.moveToNext())

            customersList.add(new Customer(
                    customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_ID)),
                    customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_NAME)),
                    customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_PHONE)),
                    customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_BIRTHDAY)),
                    customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_EMAIL)),
                    customersCursor.getDouble(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_BILL)),
                    customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_GENDER)),
                    customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_REMAINDER))

            ));


        customersCursor.close();
        return customersList;
    }
    public Customer getCustomerByID(int id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor customersCursor = db.query(CustomersDBConstants.CUSTOMERS_TABLE_NAME,null,null,null,null,null,null);

        while (customersCursor.moveToNext())

            if(customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_ID))==id){
                Customer customer =new Customer(
                        customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_ID)),
                        customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_NAME)),
                        customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_PHONE)),
                        customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_BIRTHDAY)),
                        customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_EMAIL)),
                        customersCursor.getDouble(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_BILL)),
                        customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_GENDER)),
                        customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_REMAINDER)));
                customersCursor.close();
            return customer;
            }
            customersCursor.close();
        return null;
    }
    public Customer getCustomerByPhone(String phone) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Customer customer = null;
        Cursor customersCursor = db.query(CustomersDBConstants.CUSTOMERS_TABLE_NAME,null,null,null,null,null,null);

        while (customersCursor.moveToNext())

            if(phone.equals(customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_PHONE)))){
                customer = new Customer(
                        customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_ID)),
                        customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_NAME)),
                        customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_PHONE)),
                        customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_BIRTHDAY)),
                        customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_EMAIL)),
                        customersCursor.getDouble(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_BILL)),
                        customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_GENDER)),
                        customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_REMAINDER))
                );
                break;
            }
        customersCursor.close();
        return customer;
    }
    public Customer getCustomerByName(String name) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor customersCursor = db.query(CustomersDBConstants.CUSTOMERS_TABLE_NAME,null,null,null,null,null,null);


        while (customersCursor.moveToNext()){

        if(name.toLowerCase().equals(
                customersCursor.getString(
                        customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_NAME)).toLowerCase())) {

                Customer customer =  new Customer(
                        customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_ID)),
                        customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_NAME)),
                        customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_PHONE)),
                        customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_BIRTHDAY)),
                        customersCursor.getString(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_EMAIL)),
                        customersCursor.getDouble(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_BILL)),
                        customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_GENDER)),
                        customersCursor.getInt(customersCursor.getColumnIndex(CustomersDBConstants.CUSTOMER_REMAINDER))
                );
            customersCursor.close();
            return customer;
            }

        }
        customersCursor.close();
        return null;
    }
    public boolean deleteCustomerById(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        return db.delete(CustomersDBConstants.CUSTOMERS_TABLE_NAME,
                CustomersDBConstants.CUSTOMER_ID +" = "+id,null)>0;
    }







    public boolean addAppointment(Appointment appointment){
//          TODO Check why NewAppointmentActivity not saving guest appointment
        if(!testIfAppointmentAvailable(appointment) ||
                isCustomerSchedule(appointment)){
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues columnValues = new ContentValues();

//        Set the data
        columnValues.put(AppointmentsDBConstants.APPOINTMENT_DATE,
                DateUtils.setCalendarToDB(appointment.getcDateAndTime()));
        columnValues.put(AppointmentsDBConstants.CUSTOMER_ID,appointment.getCustomerID());
        columnValues.put(AppointmentsDBConstants.APPOINTMENT_PRICE,appointment.getHaircutPrice());
        columnValues.put(AppointmentsDBConstants.APPOINTMENT_TIME,appointment.getHaircutTime());
        columnValues.put(AppointmentsDBConstants.APPOINTMENT_EXECUTED,appointment.getTackAnHaircut());


        long result = db.insertOrThrow(AppointmentsDBConstants.APPOINTMENTS_TABLE_NAME,null,columnValues);
        db.close();

        return (result != -1);
    }

    public ArrayList<Appointment> getAllAppointments() {
        ArrayList<Appointment> AppointmentsList = new ArrayList<Appointment>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor AppointmentsCursor =
                db.query(AppointmentsDBConstants.APPOINTMENTS_TABLE_NAME,
                null, null, null, null, null, AppointmentsDBConstants.APPOINTMENT_DATE+" ASC");

        while (AppointmentsCursor.moveToNext())
            if(getCustomerByID(
                    AppointmentsCursor.getInt(
                            AppointmentsCursor.getColumnIndex(
                                    AppointmentsDBConstants.CUSTOMER_ID)))!=null) {
                AppointmentsList.add(new Appointment(
                        AppointmentsCursor.getInt(AppointmentsCursor.getColumnIndex(AppointmentsDBConstants.APPOINTMENT_ID)),
                        DateUtils.getCalendar(
                                AppointmentsCursor.getString(
                                        AppointmentsCursor.getColumnIndex(AppointmentsDBConstants.APPOINTMENT_DATE))),
                        AppointmentsCursor.getInt(AppointmentsCursor.getColumnIndex(AppointmentsDBConstants.CUSTOMER_ID)),
                        AppointmentsCursor.getInt(AppointmentsCursor.getColumnIndex(AppointmentsDBConstants.APPOINTMENT_EXECUTED)),
                        AppointmentsCursor.getInt(AppointmentsCursor.getColumnIndex(AppointmentsDBConstants.APPOINTMENT_TIME)),
                        AppointmentsCursor.getDouble(AppointmentsCursor.getColumnIndex(AppointmentsDBConstants.APPOINTMENT_PRICE))
                ));
            }
        AppointmentsCursor.close();
        return AppointmentsList;
    }
    public ArrayList<Appointment> getAllAppointments(Calendar receivedDate) {

        String dateToTest = DateUtils.getOnlyDate(receivedDate);

        ArrayList<Appointment> myAppointments = getAllAppointments();
        ArrayList<Appointment> myDateAppointments = new ArrayList<>();

        for (Appointment appointment:
                myAppointments) {

//            Log.d("The Getting Date : ",DateUtils.setCalendarToDB(appointment.getcDateAndTime()));

                if(DateUtils.setCalendarToDB(
                        appointment.getcDateAndTime()).contains(dateToTest)) {
//                    Log.d("found","found");
                    myDateAppointments.add(appointment);
                }
            }

        return myDateAppointments;
    }
    public ArrayList<Appointment> getAllAppointmentsFromTo(Calendar fromDate, Calendar toDate) {

        ArrayList<Appointment> returnAppointments =new ArrayList<>();

        while (fromDate.before(toDate)||fromDate.equals(toDate) ){
            ArrayList<Appointment> appointments = getAllAppointments(fromDate);

            for (Appointment index :
                    appointments) {
                returnAppointments.add(index);
            }


            fromDate.add(Calendar.DAY_OF_MONTH,1);
        }


        return returnAppointments;
    }
    public ArrayList<Appointment> getWaitingListAppointments(Calendar receivedDate) {

        String dateToTest = DateUtils.getOnlyDate(receivedDate);

        ArrayList<Appointment> myAppointments = getAllAppointments();
        ArrayList<Appointment> myDateAppointments = new ArrayList<>();

        for (Appointment appointment:
                myAppointments) {

            Log.d("The Getting Date : ",DateUtils.setCalendarToDB(appointment.getcDateAndTime()));

            if(DateUtils.setCalendarToDB(
                    appointment.getcDateAndTime()).toLowerCase().contains(dateToTest)&&
                    appointment.getTackAnHaircut()!=1) {
//                    Log.d("found","found");
                myDateAppointments.add(appointment);
            }
        }

        return myDateAppointments;
    }
    public boolean updateAppointment(Appointment appointment) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues columnValues = new ContentValues();

//        Set the date
        columnValues.put(AppointmentsDBConstants.APPOINTMENT_DATE,
                DateUtils.setCalendarToDB(appointment.getcDateAndTime()));
        columnValues.put(AppointmentsDBConstants.CUSTOMER_ID,appointment.getCustomerID());
        columnValues.put(AppointmentsDBConstants.APPOINTMENT_EXECUTED,appointment.getTackAnHaircut());
        columnValues.put(AppointmentsDBConstants.APPOINTMENT_TIME,appointment.getHaircutTime());
        columnValues.put(AppointmentsDBConstants.APPOINTMENT_PRICE,appointment.getHaircutPrice());


        long result = db.update(AppointmentsDBConstants.APPOINTMENTS_TABLE_NAME,
                columnValues,
                AppointmentsDBConstants.APPOINTMENT_ID + "= "+appointment.get_id(),
                null);
        db.close();

        return (result != -1);
    }
    public boolean isCustomerSchedule(Appointment newAppointment) {
//        Log.d(TAG,"isCustomerSchedule:Customer ID :"+newAppointment.getCustomerID());

        if (newAppointment.getCustomerID()== 1||newAppointment.getCustomerID()== -1) {
            return false;
        }

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();

        start.setTime(newAppointment.getcDateAndTime().getTime());
        end.setTime(newAppointment.getcDateAndTime().getTime());
        end.add(Calendar.DAY_OF_MONTH,3);

        ArrayList<Appointment> myAppointments = getAllAppointmentsFromTo(start,end);

        for (Appointment testAppointment:
                myAppointments) {

//            Log.d("Testing Date : ",DateUtils.setCalendarToDB(testAppointment.getcDateAndTime()).toLowerCase()+" - "+testDate);

            /*if(testAppointment.getDateAndTimeToDisplay().toLowerCase().contains(testDate)&&*/
            if(testAppointment.getCustomerID() == newAppointment.getCustomerID()) {
//                Toast.makeText(context,
//                        context.getResources().getString( R.string.customer_is_schedule) +" "+DateUtils.setCalendarToDB(testAppointment.getcDateAndTime()),
//                        Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        return false;

    }

    public Appointment getAppointmentById(int appointment_id) {
        ArrayList<Appointment>appointments= getAllAppointments(Calendar.getInstance());

        for (Appointment index:
             appointments) {
            if(index.get_id()==appointment_id)
                return index;
        }

        return null;
    }
    public boolean testIfAppointmentAvailable(Context context,Appointment appointmentToTest) {
        ArrayList<Appointment> appointmentsList = getAllAppointments(appointmentToTest.getcDateAndTime());

        Calendar date1;
        Calendar date2;

        date1 = Calendar.getInstance();
        date1.setTime(appointmentToTest.getcDateAndTime().getTime());
        date1.set(Calendar.SECOND,0);

        if(appointmentsList.isEmpty())return true;
//        Toast.makeText(context, "Date1 : "+ DateUtils.getFullSDF(receivedDate)
//                + "  Date2 : "+ DateUtils.getFullSDF(myAppointments.get(0).getcDateAndTime()), Toast.LENGTH_LONG).show();
        for (Appointment appointmentItem:
                appointmentsList) {

            date2 = Calendar.getInstance();
            date2.setTime(appointmentItem.getcDateAndTime().getTime());
            date2.add(Calendar.MINUTE,1);
            date2.set(Calendar.SECOND,0);

            if(Math.abs(date1.getTimeInMillis() - date2.getTimeInMillis())/60000 < appointmentToTest.getHaircutTime()-1){
                Calendar temp = Calendar.getInstance();
                date2.add(Calendar.MINUTE,-1);
                temp.setTime(date2.getTime());
                temp.add(Calendar.MINUTE,appointmentItem.getHaircutTime());

                Toast.makeText(context,
                        context.getResources().getString(R.string.already_scheduled)+
                                "\n"+DateUtils.setCalendarToDB(date2)+" - "+DateUtils.setCalendarToDB(temp),
                        Toast.LENGTH_LONG).show();

                return false;

            }

//            /*if((date1.get(Calendar.HOUR)+1) < date2.get(Calendar.HOUR)) {
//                Toast.makeText(context, DateUtils.getFullSDF(date1)+" Date+1 is Small "+DateUtils.getFullSDF(date2), Toast.LENGTH_LONG).show();
//            }else if((date1.get(Calendar.HOUR)-1) > date2.get(Calendar.HOUR)){
//                Toast.makeText(context, DateUtils.getFullSDF(date1)+" Date-1 is Big "+DateUtils.getFullSDF(date2), Toast.LENGTH_LONG).show();
//            }else
//                Toast.makeText(context, DateUtils.getFullSDF(date1)+" Error "+DateUtils.getFullSDF(date2), Toast.LENGTH_LONG).show();*/
//
//            /*Toast.makeText(context, "compare date : "+DateUtils.compareDates(date1,date2)+"\nDate1: "+DateUtils.getFullSDF(date1) +
//                    "\n Date2: "+DateUtils.getFullSDF(date2), Toast.LENGTH_LONG).show();*/
////            Toast.makeText(context, "Date1 : "+ DateUtils.getFullSDF(receivedDate)
////            + "  Date2 : "+ DateUtils.getFullSDF(appointment.getcDateAndTime()), Toast.LENGTH_SHORT).show();
//
//            //equals() returns true if both the dates are equal
////            Toast.makeText(context, DateUtils.compareDatesAppointments(context,date1.getTime(),date2.getTime()), Toast.LENGTH_SHORT).show();
//            /*for(int i = 0; i<2;i++) {
//                Toast.makeText(context, DateUtils.getFullSDF(date1)+" == "+DateUtils.getFullSDF(date2), Toast.LENGTH_SHORT).show();
//                switch (DateUtils.compareDatesAppointments(context, date1.getTime(), date2.getTime())) {
//                    case "0":
//                        Toast.makeText(context, "0", Toast.LENGTH_SHORT).show();
//                        break;
//                    case "1":
//                        Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
//                        date2.add(Calendar.MINUTE,35);
//                        Toast.makeText(context, DateUtils.getFullSDF(date1)+" == "+DateUtils.getFullSDF(date2), Toast.LENGTH_LONG).show();
//                        break;
//
//                    case "2":
//                        Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
//                        break;
//
//                    default:
//                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }*/
//
//            /*if (date1.equals(date2)) {
//                Toast.makeText(context, R.string.There_is_a_scheduled_appointment + " \n" +
//                        getCustomerByID(appointment.get_id()).getName() + " " + appointment.getDateAndTimeToDisplay(), Toast.LENGTH_SHORT).show();
//                return false;
////            System.out.println("Date1 is equal Date2");
//            }
//
//            if (date1.after(date2)) {
//                *//*Toast.makeText(context, "Date1: "+DateUtils.getFullSDF(date1) +
//                        "\n>\n Date2: "+DateUtils.getFullSDF(date2)+"", Toast.LENGTH_LONG).show();*//*
//                try {
//                    date2.add(Calendar.MINUTE, -appointment.getHaircutTime());
//                } catch (Exception e) {
//                    date2.add(Calendar.MINUTE, -35);
//                }
//
//                if (!date1.after(date2)) {
//                    Toast.makeText(context, R.string.There_is_a_scheduled_appointment + " \n" +
//                            getCustomerByID(appointment.get_id()).getName() + " " + appointment.getDateAndTimeToDisplay(), Toast.LENGTH_SHORT).show();
//                    return false;
//                }
////                Toast.makeText(context, "Date1 : "+ DateUtils.getFullSDF(receivedDate)
////                        + "  Date2 : "+ DateUtils.getFullSDF(myAppointments.get(0).getcDateAndTime()), Toast.LENGTH_LONG).show();
////            System.out.println("Date1 is after Date2");
//            }
//
//            if (date1.before(date2)) {
//                *//*Toast.makeText(context, "Date1: "+DateUtils.getFullSDF(date1) +
//                        "\n<\n Date2: "+DateUtils.getFullSDF(date2)+"", Toast.LENGTH_LONG).show();*//*
//                try {
//                    date2.add(Calendar.MINUTE, appointment.getHaircutTime());
//                } catch (Exception e) {
//                    date2.add(Calendar.MINUTE, 35);
//                }
//
//                if (!date1.before(date2) || date1.equals(date2)) {
//                    Toast.makeText(context, R.string.There_is_a_scheduled_appointment + " \n" +
//                            getCustomerByID(appointment.get_id()).getName() + " " + appointment.getDateAndTimeToDisplay(), Toast.LENGTH_SHORT).show();
//                    return false;
//                }
////            System.out.println("Date1 is before Date2");
//            }*/

        }

        return true;
    }
    public boolean testIfAppointmentAvailable(Appointment appointmentToTest) {
        ArrayList<Appointment> appointmentsList = getAllAppointments(
                appointmentToTest.getcDateAndTime());

        Calendar date1;
        Calendar date2;

        date1 = Calendar.getInstance();
        date1.setTime(appointmentToTest.getcDateAndTime().getTime());
        date1.set(Calendar.SECOND,0);

        if(appointmentsList.isEmpty())return true;
//        Toast.makeText(context, "Date1 : "+ DateUtils.getFullSDF(receivedDate)
//                + "  Date2 : "+ DateUtils.getFullSDF(myAppointments.get(0).getcDateAndTime()), Toast.LENGTH_LONG).show();
        for (Appointment appointmentItem:
                appointmentsList) {

            date2 = Calendar.getInstance();

            date2.setTime(appointmentItem.getcDateAndTime().getTime());
            date2.add(Calendar.MINUTE,1);
            date2.set(Calendar.SECOND,0);

            if(Math.abs(date1.getTimeInMillis() - date2.getTimeInMillis())/60000 < appointmentToTest.getHaircutTime()-1){
                Calendar temp = Calendar.getInstance();
                date2.add(Calendar.MINUTE,-1);
                temp.setTime(date2.getTime());
                temp.add(Calendar.MINUTE,appointmentItem.getHaircutTime());

//                Toast.makeText(context,
//                        context.getResources().getString(R.string.already_scheduled)+
//                                "\n"+DateUtils.setCalendarToDB(date2)+" - "+DateUtils.setCalendarToDB(temp),
//                        Toast.LENGTH_LONG).show();
                return false;

            }
//            /*if((date1.get(Calendar.HOUR)+1) < date2.get(Calendar.HOUR)) {
//                Toast.makeText(context, DateUtils.getFullSDF(date1)+" Date+1 is Small "+DateUtils.getFullSDF(date2), Toast.LENGTH_LONG).show();
//            }else if((date1.get(Calendar.HOUR)-1) > date2.get(Calendar.HOUR)){
//                Toast.makeText(context, DateUtils.getFullSDF(date1)+" Date-1 is Big "+DateUtils.getFullSDF(date2), Toast.LENGTH_LONG).show();
//            }else
//                Toast.makeText(context, DateUtils.getFullSDF(date1)+" Error "+DateUtils.getFullSDF(date2), Toast.LENGTH_LONG).show();*/
//
//            /*Toast.makeText(context, "compare date : "+DateUtils.compareDates(date1,date2)+"\nDate1: "+DateUtils.getFullSDF(date1) +
//                    "\n Date2: "+DateUtils.getFullSDF(date2), Toast.LENGTH_LONG).show();*/
////            Toast.makeText(context, "Date1 : "+ DateUtils.getFullSDF(receivedDate)
////            + "  Date2 : "+ DateUtils.getFullSDF(appointment.getcDateAndTime()), Toast.LENGTH_SHORT).show();
//
//            //equals() returns true if both the dates are equal
////            Toast.makeText(context, DateUtils.compareDatesAppointments(context,date1.getTime(),date2.getTime()), Toast.LENGTH_SHORT).show();
//            /*for(int i = 0; i<2;i++) {
//                Toast.makeText(context, DateUtils.getFullSDF(date1)+" == "+DateUtils.getFullSDF(date2), Toast.LENGTH_SHORT).show();
//                switch (DateUtils.compareDatesAppointments(context, date1.getTime(), date2.getTime())) {
//                    case "0":
//                        Toast.makeText(context, "0", Toast.LENGTH_SHORT).show();
//                        break;
//                    case "1":
//                        Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
//                        date2.add(Calendar.MINUTE,35);
//                        Toast.makeText(context, DateUtils.getFullSDF(date1)+" == "+DateUtils.getFullSDF(date2), Toast.LENGTH_LONG).show();
//                        break;
//
//                    case "2":
//                        Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
//                        break;
//
//                    default:
//                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }*/
//
//            /*if (date1.equals(date2)) {
//                Toast.makeText(context, R.string.There_is_a_scheduled_appointment + " \n" +
//                        getCustomerByID(appointment.get_id()).getName() + " " + appointment.getDateAndTimeToDisplay(), Toast.LENGTH_SHORT).show();
//                return false;
////            System.out.println("Date1 is equal Date2");
//            }
//
//            if (date1.after(date2)) {
//                *//*Toast.makeText(context, "Date1: "+DateUtils.getFullSDF(date1) +
//                        "\n>\n Date2: "+DateUtils.getFullSDF(date2)+"", Toast.LENGTH_LONG).show();*//*
//                try {
//                    date2.add(Calendar.MINUTE, -appointment.getHaircutTime());
//                } catch (Exception e) {
//                    date2.add(Calendar.MINUTE, -35);
//                }
//
//                if (!date1.after(date2)) {
//                    Toast.makeText(context, R.string.There_is_a_scheduled_appointment + " \n" +
//                            getCustomerByID(appointment.get_id()).getName() + " " + appointment.getDateAndTimeToDisplay(), Toast.LENGTH_SHORT).show();
//                    return false;
//                }
////                Toast.makeText(context, "Date1 : "+ DateUtils.getFullSDF(receivedDate)
////                        + "  Date2 : "+ DateUtils.getFullSDF(myAppointments.get(0).getcDateAndTime()), Toast.LENGTH_LONG).show();
////            System.out.println("Date1 is after Date2");
//            }
//
//            if (date1.before(date2)) {
//                *//*Toast.makeText(context, "Date1: "+DateUtils.getFullSDF(date1) +
//                        "\n<\n Date2: "+DateUtils.getFullSDF(date2)+"", Toast.LENGTH_LONG).show();*//*
//                try {
//                    date2.add(Calendar.MINUTE, appointment.getHaircutTime());
//                } catch (Exception e) {
//                    date2.add(Calendar.MINUTE, 35);
//                }
//
//                if (!date1.before(date2) || date1.equals(date2)) {
//                    Toast.makeText(context, R.string.There_is_a_scheduled_appointment + " \n" +
//                            getCustomerByID(appointment.get_id()).getName() + " " + appointment.getDateAndTimeToDisplay(), Toast.LENGTH_SHORT).show();
//                    return false;
//                }
////            System.out.println("Date1 is before Date2");
//            }*/
        }

        return true;
    }






    public ArrayList<Appointment> getTodayFreeAppointmentsList(Calendar cTodayTest, int startHour, int startMin, int endHour, int endMin) {


        Calendar cStart = Calendar.getInstance();
        cStart.setTime(cTodayTest.getTime());

        Calendar cEnd = Calendar.getInstance();
        cEnd.setTime(cTodayTest.getTime());

        cStart.set(Calendar.HOUR_OF_DAY,startHour);
        cStart.set(Calendar.MINUTE,startMin);

        cEnd.set(Calendar.HOUR_OF_DAY,endHour);
        cEnd.set(Calendar.MINUTE,endMin);


        Log.d("FreeAppointments","Start Date: "+startHour+":"+startMin+" -- "+endHour+":"+endMin);



        int haircutTime = settings.getInt(UserDBConstants.USER_MALE_HAIRCUT_TIME,20);


//        Log.d("FreeAppointments","Start Date: "+appointment.getDateAndTimeToDisplay()+" End Date: "+DateUtils.getFullSDF(cEnd));

        ArrayList<Appointment> freeAppointmentsList = new ArrayList<>();
//        int i=0;

        while (cStart.before(cEnd)){

//            Log.d("FreeAppointments","While: "+appointment.getDateAndTimeToDisplay()+" End Date: "+DateUtils.getFullSDF(cEnd));
//            Log.d("FreeAppointments",++i+")New Date "+appointment.getDateAndTimeToDisplay());

            Appointment appointment = new Appointment();
            appointment.setcDateAndTime(cStart);
            appointment.setHaircutTime(haircutTime);

            if(testIfAppointmentAvailable(appointment)&&
                    (Calendar.getInstance().equals(appointment.getcDateAndTime())|| Calendar.getInstance().before(appointment.getcDateAndTime()))) {
                Log.d("FreeAppointments","Add an'appintment to list : "+
                        DateUtils.setCalendarToDB(appointment.getcDateAndTime()));
                freeAppointmentsList.add(appointment);
            }else{Log.d("FreeAppointments","Not Free Date : "+DateUtils.setCalendarToDB(appointment.getcDateAndTime()));}

            Log.d("FreeAppointments","Testing Date: "+DateUtils.setCalendarToDB(appointment.getcDateAndTime()));


            cStart.add(Calendar.MINUTE, appointment.getHaircutTime());
//
//            Calendar temp = Calendar.getInstance();
//            temp.setTime(cStart.getTime());
//
//            appointment.setcDateAndTime(temp);
        }

        Calendar temp = Calendar.getInstance();
        temp.setTime(cStart.getTime());
        temp.add(Calendar.MINUTE,-(haircutTime/2));

        Appointment appointment =new Appointment();
        appointment.setcDateAndTime(temp);
        appointment.setHaircutTime(haircutTime);

        if(testIfAppointmentAvailable(appointment)) {
            appointment.setcDateAndTime(cStart);
            freeAppointmentsList.add(appointment);
        }

        Log.d("FreeAppointments","List Lang : "+freeAppointmentsList.size());

//        return (!freeAppointmentsList.isEmpty())? freeAppointmentsList : null;
        return freeAppointmentsList;
    }

//    public ArrayList<Appointment> getTodayAppointments(Calendar myDate) {
//
//        ArrayList<Appointment> myDateAppointments = new ArrayList<Appointment>();
//
//        myDateAppointments = getAllAppointments(myDate);
//
//        return myDateAppointments;
//}

//    public ArrayList<String> getScheduledAppointments() {
//
//        ArrayList<Appointment> myAppointments = getAllAppointments();
//        ArrayList<Customer> myCustomers = getAllCustomers();
//        ArrayList<String> myScheduledList = new ArrayList<String>();
//
//        for (Appointment appointment:
//                myAppointments) {
////            myScheduledList.add(myScheduledList(getAppointmentByCustomerId(customer.get_id())));
////
////            String selectQuery = "SELECT * FROM " +
////                    CustomersDBConstants.CUSTOMERS_TABLE_NAME + " WHERE "+
////                    CustomersDBConstants.CUSTOMER_ID +" == "+appointment.getCustomerID()+";";
//            for (Customer customer :
//                    myCustomers) {
//                if (appointment.getCustomerID() == customer.get_id()){
//                    myScheduledList.add(customer.getName()+" "+appointment.getDateAndTimeToDisplay());
//                }
//            }
//        }
//
//        return myScheduledList;
//    }





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
        ArrayList<Product> productsList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor ProductsCursor = db.query(ProductsDBConstants.PRODUCTS_TABLE_NAME,
                null, null, null, null, null, ProductsDBConstants.PRODUCT_NAME +" ASC");

        while (ProductsCursor.moveToNext())
            productsList.add(new Product(
                    ProductsCursor.getInt(ProductsCursor.getColumnIndex(ProductsDBConstants.PRODUCT_ID)),
                    ProductsCursor.getString(ProductsCursor.getColumnIndex(ProductsDBConstants.PRODUCT_NAME)),
                    ProductsCursor.getInt(ProductsCursor.getColumnIndex(ProductsDBConstants.PRODUCT_QUANTITY)),
                    ProductsCursor.getDouble(ProductsCursor.getColumnIndex(ProductsDBConstants.PRODUCT_PRICE))
            ));

        ProductsCursor.close();
        return productsList;
    }
//    public Product getProductByID(int id) {
//
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        Cursor productCursor = db.query(ProductsDBConstants.PRODUCTS_TABLE_NAME,null,null,null,null,null,null);
//
//        while (productCursor.moveToNext())
//
//            if(productCursor.getInt(productCursor.getColumnIndex(ProductsDBConstants.PRODUCT_ID))==id){
//                return new Product(
//                        productCursor.getColumnIndex(ProductsDBConstants.PRODUCT_ID),
//                        productCursor.getString(productCursor.getColumnIndex(ProductsDBConstants.PRODUCT_NAME)),
//                        productCursor.getInt(productCursor.getColumnIndex(ProductsDBConstants.PRODUCT_QUANTITY)),
//                        productCursor.getDouble(productCursor.getColumnIndex(ProductsDBConstants.PRODUCT_PRICE))
//                );
//            }
//        return null;
//
//    }
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
    public Product getProductByName(String name) {

        ArrayList<Product> productsList =getAllProducts();

        for (Product index:
             productsList) {
            if(index.getName().equals(name))
                return index;
        }

        return null;
    }
    public boolean deleteProductById(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        return db.delete(ProductsDBConstants.PRODUCTS_TABLE_NAME,
                ProductsDBConstants.PRODUCT_ID+" = "+id,null)>0;
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
        "dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        dateFormat.format(date);

        columnValues.put(PurchaseDBConstants.PURCHASE_DATE,dateFormat.format(date));

        long result = db.insertOrThrow(PurchaseDBConstants.PURCHASES_TABLE_NAME,null,columnValues);
        db.close();

        return (result != -1);
    }
    public ArrayList<Purchase> getAllPurchase() {
        ArrayList<Purchase> PurchasesList = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor ProductsCursor = db.query(PurchaseDBConstants.PURCHASES_TABLE_NAME,
                null, null, null, null, null, PurchaseDBConstants.PURCHASE_DATE+" ASC");

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
    public ArrayList<Purchase> getPurchaseByDate(Calendar startDate) {

        ArrayList<Purchase> myPurchase = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor purchaseCursor = db.query(PurchaseDBConstants.PURCHASES_TABLE_NAME,null,null,null,null,null,null);

//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());


//        Purchase(int id, int appointmentID, int productID,int customerID,String date, double price)

//        purchaseCursor.moveToFirst();
//        String testFormat = formatter.format(startDate.getTime());

        while (purchaseCursor.moveToNext()){
//            testFormat = formatter.format(startDate.getTime());
            if (purchaseCursor.getString(purchaseCursor.getColumnIndex(PurchaseDBConstants.PURCHASE_DATE)).contains(
                    DateUtils.getOnlyDate(startDate))) {
                myPurchase.add(new Purchase(
                        purchaseCursor.getInt(purchaseCursor.getColumnIndex(PurchaseDBConstants.PURCHASE_ID)),
                        purchaseCursor.getInt(purchaseCursor.getColumnIndex(PurchaseDBConstants.APPOINTMENT_ID)),
                        purchaseCursor.getInt(purchaseCursor.getColumnIndex(PurchaseDBConstants.PRODUCT_ID)),
                        purchaseCursor.getInt(purchaseCursor.getColumnIndex(PurchaseDBConstants.CUSTOMER_ID)),
                        purchaseCursor.getString(purchaseCursor.getColumnIndex(PurchaseDBConstants.PURCHASE_DATE)),
                        purchaseCursor.getDouble(purchaseCursor.getColumnIndex(PurchaseDBConstants.PURCHASE_PRICE))
                ));
            }
        }
        purchaseCursor.close();
        return myPurchase;
    }




//    PICTURE TABLE.

//    PicturesDBConstants.PICTURE_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT  , "+
//    PicturesDBConstants.PICTURE_NAME + " TEXT ,"+
//    PicturesDBConstants.PICTURE_DATA + " BLOB , "+
//    PicturesDBConstants.CUSTOMER_ID + " INTEGER "


    public boolean addPicture(Picture pictureToSave) throws SQLiteException {

        if(pictureToSave.getId() != new Picture().getId()){return updatePicture(pictureToSave);}

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues columnValues = new ContentValues();

        byte[] imageData = BitmapDBUtility.getBytes(pictureToSave.getBitmapImageData());

        if(imageData==null){Log.d(TAG,"AddPicture(): Image data is null");return false;}


        columnValues.put(PicturesDBConstants.PICTURE_NAME,pictureToSave.getName());
        columnValues.put(PicturesDBConstants.PICTURE_DATA,imageData);
        columnValues.put(PicturesDBConstants.CUSTOMER_ID,pictureToSave.getCustomerId());


        long result = db.insertOrThrow(PicturesDBConstants.PICTURES_TABLE_NAME,null,columnValues);
        db.close();
        Log.d(TAG,"addPicture(): Pic was saved"+pictureToSave.getId()+" - "+pictureToSave.getName()+" -- "+pictureToSave.getCustomerId());

        return (result != -1);
    }
    public boolean updatePicture(Picture pictureToUpdate){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues columnValues = new ContentValues();
        byte[] imageData =BitmapDBUtility.getBytes(pictureToUpdate.getBitmapImageData());

        columnValues.put(PicturesDBConstants.PICTURE_NAME,pictureToUpdate.getName());
        columnValues.put(PicturesDBConstants.PICTURE_DATA,imageData);
        columnValues.put(PicturesDBConstants.CUSTOMER_ID,pictureToUpdate.getCustomerId());



        int result = db.update(PicturesDBConstants.PICTURES_TABLE_NAME,
                columnValues,
                PicturesDBConstants.PICTURE_ID +"= "+pictureToUpdate.getId(),
                null);

        db.close();
        Log.d(TAG,"Update Customer ID:"+pictureToUpdate.getId()+" Pic");
        return (result != -1);
    }
    public ArrayList<Picture> getAllPicturesByUserID(int id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Picture> allCustomerPicList=new ArrayList<>();

//        ArrayList<Bitmap> myPhotos = new ArrayList<Bitmap>();
//        BitmapDBUtility picsHandler = new BitmapDBUtility();

//        AppointmentsDBConstants.APPOINTMENT_DATE+" ASC"
        Cursor picturesCursor = db.query(
                PicturesDBConstants.PICTURES_TABLE_NAME,null,null,null,null,null,PicturesDBConstants.PICTURE_NAME+" DESC");

        while (picturesCursor.moveToNext())

            if(picturesCursor.getInt(picturesCursor.getColumnIndex(PicturesDBConstants.CUSTOMER_ID))==id){
//                int id, Calendar name, Bitmap bitmapImageData, int customerId
                allCustomerPicList.add(new Picture(
                        picturesCursor.getInt(picturesCursor.getColumnIndex(PicturesDBConstants.PICTURE_ID)),
                        picturesCursor.getString(picturesCursor.getColumnIndex(PicturesDBConstants.PICTURE_NAME)),
                        BitmapDBUtility.getImage(
                                picturesCursor.getBlob(
                                        picturesCursor.getColumnIndex(PicturesDBConstants.PICTURE_DATA))),
                        picturesCursor.getInt(picturesCursor.getColumnIndex(PicturesDBConstants.CUSTOMER_ID))
                ));


//                myPhotos.add(
//                        BitmapDBUtility.getImage(
//                                picturesCursor.getBlob(
//                                        picturesCursor.getColumnIndex(PicturesDBConstants.PICTURE_DATA))));
            }

            picturesCursor.close();
        return allCustomerPicList;

    }
    public Bitmap getUserPictureByID(int id) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        BitmapDBUtility picsHandler = new BitmapDBUtility();

        Cursor picturesCursor = db.query(PicturesDBConstants.PICTURES_TABLE_NAME,null,null,null,null,null,null);

        while (picturesCursor.moveToNext()) {
            if (picturesCursor.getInt(picturesCursor.getColumnIndex(PicturesDBConstants.CUSTOMER_ID)) == id) {
                return BitmapDBUtility.getImage(
                        picturesCursor.getBlob(
                                picturesCursor.getColumnIndex(PicturesDBConstants.PICTURE_DATA)));
            }
        }
        picturesCursor.close();
        return null;

    }

    public boolean deletePictureById(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        return db.delete(PicturesDBConstants.PICTURES_TABLE_NAME,
                PicturesDBConstants.PICTURE_ID+" = "+id,null)>0;
    }

    public ArrayList<Picture> getAllPictures() {


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ArrayList<Picture> allCustomerPicList=new ArrayList<>();

//        ArrayList<Bitmap> myPhotos = new ArrayList<Bitmap>();
//        BitmapDBUtility picsHandler = new BitmapDBUtility();

//        AppointmentsDBConstants.APPOINTMENT_DATE+" ASC"
        Cursor picturesCursor = db.query(
                PicturesDBConstants.PICTURES_TABLE_NAME,null,null,null,null,null,PicturesDBConstants.PICTURE_NAME+" DESC");

        while (picturesCursor.moveToNext())

                allCustomerPicList.add(new Picture(
                        picturesCursor.getInt(picturesCursor.getColumnIndex(PicturesDBConstants.PICTURE_ID)),
                        picturesCursor.getString(picturesCursor.getColumnIndex(PicturesDBConstants.PICTURE_NAME)),
                        BitmapDBUtility.getImage(
                                picturesCursor.getBlob(
                                        picturesCursor.getColumnIndex(PicturesDBConstants.PICTURE_DATA))),
                        picturesCursor.getInt(picturesCursor.getColumnIndex(PicturesDBConstants.CUSTOMER_ID))
                ));
//                myPhotos.add(
//                        BitmapDBUtility.getImage(
//                                picturesCursor.getBlob(
//                                        picturesCursor.getColumnIndex(PicturesDBConstants.PICTURE_DATA))));
        picturesCursor.close();
        return allCustomerPicList;
    }



    //    Messages table
    public boolean addMessageRemainder(Message message){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues columnValues = new ContentValues();

//        Set the product
        columnValues.put(MessageDBConstants.APPOINTMENT_ID , message.getAppointment_id());
        columnValues.put(MessageDBConstants.CUSTOMER_ID, message.getCustomer_id());
        columnValues.put(MessageDBConstants.MESSAGE_EXECUTE, message.getIsMessageExecute());
        columnValues.put(MessageDBConstants.MESSAGE_TIME, DateUtils.getDateAndTime(message.getExecute_time()));

        long result = db.insertOrThrow(MessageDBConstants.MESSAGES_TABLE_NAME,null,columnValues);
        db.close();

        return (result != -1);
    }

    public ArrayList<Message> getAllMessagesRemainder() {

        ArrayList<Message> messagesList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor messagesCursor = db.query(MessageDBConstants.MESSAGES_TABLE_NAME,
                null,null,null,null,null,MessageDBConstants.MESSAGE_TIME +" ASC");
//        customersCursor.moveToNext();

        while (messagesCursor.moveToNext())

            messagesList.add(new Message(
                    messagesCursor.getInt(messagesCursor.getColumnIndex(MessageDBConstants.MESSAGES_ID)),
                    messagesCursor.getInt(messagesCursor.getColumnIndex(MessageDBConstants.CUSTOMER_ID)),
                    messagesCursor.getInt(messagesCursor.getColumnIndex(MessageDBConstants.APPOINTMENT_ID)),
                    messagesCursor.getInt(messagesCursor.getColumnIndex(MessageDBConstants.MESSAGE_EXECUTE)),
                    DateUtils.getCalendar(
                            messagesCursor.getString(messagesCursor.getColumnIndex(MessageDBConstants.MESSAGE_TIME))
                    )));

        messagesCursor.close();
        return messagesList;
    }

    public boolean deleteRemainderById(int id) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        return db.delete(MessageDBConstants.MESSAGES_TABLE_NAME,
                MessageDBConstants.MESSAGES_ID+" = "+id,null)>0;
    }
}
