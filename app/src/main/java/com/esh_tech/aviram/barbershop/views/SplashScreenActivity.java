package com.esh_tech.aviram.barbershop.views;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.JobsHandler.MJobScheduler;
import com.esh_tech.aviram.barbershop.R;
import com.esh_tech.aviram.barbershop.data.Appointment;
import com.esh_tech.aviram.barbershop.data.Customer;
import com.esh_tech.aviram.barbershop.data.Message;
import com.esh_tech.aviram.barbershop.data.Product;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

import static com.esh_tech.aviram.barbershop.Constants.SharedPreferencesConstants.*;
import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.*;


public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    BarbershopDBHandler dbHandler;
    ProgressBar loading;


////    SMS job test
//    private static final int JOB_ID = 101;
//    private JobScheduler jobScheduler;
//    private JobInfo jobInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        loadData();
    }

    private void loadData() {

        this.setTitle(R.string.title_splash_activity);
//
//
////        SMS Tester
//        ComponentName componentName = new ComponentName(this,MJobScheduler.class);
//        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,componentName);
//
//        builder.setPeriodic(5000);
//        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
//        builder.setPersisted(true);
//
//        jobInfo = builder.build();
//        jobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
//
//        jobScheduler.schedule(jobInfo);
        dbHandler = new BarbershopDBHandler(this);
        if(dbHandler.getCustomerByID(1)==null){
            dbHandler.addCustomer(new Customer());
        }

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        loading = (ProgressBar) findViewById(R.id.progressBar);
        loading.computeScroll();


//        if (!settings.getBoolean(USER_IS_REGISTER,false))
                setUpDefaultData();


        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {

                    sleep(3000);

//                    Checking if user are exist and have checked auto login.

                    String username = settings.getString(USER_NAME, "");
                    boolean savePassword = settings.getBoolean(USER_AUTO_LOGIN, false);

                    boolean register = settings.getBoolean(USER_IS_REGISTER, false);

                    if (!username.equals("") && savePassword && register) {
                        //Login with User password
                        //    Database
//                        dbHandler =new BarbershopDBHandler(SplashScreenActivity.this);
//                        dbHandler.addCustomer(new Customer());

                        Intent myIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(myIntent);
                        SplashScreenActivity.this.finish();

                    } else if (!register) {
                        setDayAndHours();
                        Intent myIntent = new Intent(SplashScreenActivity.this, UserRegistrationActivity.class);
                        startActivity(myIntent);
                        SplashScreenActivity.this.finish();
                    } else if (register && !savePassword) {
                        Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(myIntent);
                        SplashScreenActivity.this.finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }

    private void setDayAndHours() {

        editor = settings.edit();

        editor.putString(SUNDAY_TIME_OPEN,"06:00:18:00");

        editor.putString(MONDAY_TIME_OPEN,"06:00:18:00");

        editor.putString(TUESDAY_TIME_OPEN,"06:00:18:00");

        editor.putString(WEDNESDAY_TIME_OPEN,"06:00:18:00");

        editor.putString(THURSDAY_TIME_OPEN,"06:00:18:00");

        editor.putString(FRIDAY_TIME_OPEN,"06:00:13:00");

        editor.putString(SATURDAY_TIME_OPEN,"");

        editor.apply();
    }

    private void setUpDefaultData() {

        editor = settings.edit();

        editor.putString(USER_NAME, "Oren");
        editor.putString(USER_LAST_NAME, "Levi");
        editor.putString(USER_PHONE, "0506792353");
        editor.putString(USER_BUSINESS_NAME, "Oren Design");
        editor.putString(USER_BUSINESS_PHONE, "039806040");
        editor.putString(USER_BUSINESS_ADDRESS, "Marvad Haksamim 1 Rosh Ha'Ayin");

        editor.putString(USER_PASSWORD, "1234");
        editor.putBoolean(USER_AUTO_LOGIN, true);
        editor.putBoolean(USER_IS_REGISTER, true);

        editor.putInt(USER_FEMALE_HAIRCUT_TIME, 45);
        editor.putInt(USER_MALE_HAIRCUT_PRICE, 35);
        editor.putInt(USER_MALE_HAIRCUT_TIME, 35);
        editor.putInt(USER_FEMALE_HAIRCUT_PRICE, 150);
        editor.putInt(USER_FEMALE_HAIRCUT_TIME, 45);

        editor.putString(UserDBConstants.USER_EMAIL, "aviram.note@gmail.com");
        editor.putString(UserDBConstants.USER_EMAIL_PASSWORD, "avi304287");




        editor.putString(SUNDAY_TIME_OPEN,"06:00:18:00");

        editor.putString(MONDAY_TIME_OPEN,"06:00:18:00");

        editor.putString(TUESDAY_TIME_OPEN,"06:00:18:00");

        editor.putString(WEDNESDAY_TIME_OPEN,"06:00:18:00");

        editor.putString(THURSDAY_TIME_OPEN,"06:00:18:00");

        editor.putString(FRIDAY_TIME_OPEN,"06:00:13:00");

        editor.putString(SATURDAY_TIME_OPEN,"");

        dbHandler = new BarbershopDBHandler(SplashScreenActivity.this);


//        Create customers
//        public Customer(String name, String phone, String birthday, String email, Double bill, Bitmap photo, int gender, int remainder)


        dbHandler.addCustomer(
                new Customer(
                        "Sharon","0500000001","1/1/1900","AAA@MMM.com",0.0,null,0,1));

        dbHandler.addCustomer(
                new Customer(
                        "Alon Moshe","0500000002","1/1/1900","AAA@MMM.com",0.0,null,1,1));
        dbHandler.addCustomer(
                new Customer(
                        "Meir Eli","0500000003","1/1/1900","AAA@MMM.com",0.0,null,1,1));
        dbHandler.addCustomer(
                new Customer(
                        "Elad Bear","0500000004","1/1/1900","AAA@MMM.com",0.0,null,1,1));
        dbHandler.addCustomer(
                new Customer(
                        "Liat Natan","0500000005","1/1/1900","AAA@MMM.com",0.0,null,0,1));
        dbHandler.addCustomer(
                new Customer(
                        "Nir Sher","0500000006","1/1/1900","AAA@MMM.com",0.0,null,1,1));
        dbHandler.addCustomer(
                new Customer(
                        "Moran Shamesh","0500000007","1/1/1900","AAA@MMM.com",0.0,null,0,1));

        dbHandler.addCustomer(
                new Customer(
                        "Adi Kohen","0500000008","1/1/1900","AAA@MMM.com",0.0,null,0,1));

        dbHandler.addCustomer(
                new Customer(
                        "Arik Nagar","0500000009","1/1/1900","AAA@MMM.com",0.0,null,1,1));
        dbHandler.addCustomer(
                new Customer(
                        "Erez Mor","0500000003","1/1/1900","AAA@MMM.com",0.0,null,1,1));
        dbHandler.addCustomer(
                new Customer(
                        "Itay Yhoda","0500000004","1/1/1900","AAA@MMM.com",0.0,null,1,1));
        dbHandler.addCustomer(
                new Customer(
                        "Shiran Moshe","0500000005","1/1/1900","AAA@MMM.com",0.0,null,0,1));
        dbHandler.addCustomer(
                new Customer(
                        "Michael Mor","0500000006","1/1/1900","AAA@MMM.com",0.0,null,1,1));
        dbHandler.addCustomer(
                new Customer(
                        "Reut Levin","0500000007","1/1/1900","AAA@MMM.com",0.0,null,0,1));



//        /////////////////////////////////////////////////////////////////////////////////////////////


//        Create Products
//        Product(String name, int quantity, double price)
        dbHandler.addProduct(new Product("Shampoo",19,12.90));
        dbHandler.addProduct(new Product("Scissors",7,15.00));
        dbHandler.addProduct(new Product("Wax",14,25.00));
        dbHandler.addProduct(new Product("Hair Gel",32,20.00));
        dbHandler.addProduct(new Product("Razor",150,5.00));
        dbHandler.addProduct(new Product("Haircut Machine",5,399.90));
        dbHandler.addProduct(new Product("Black Color",16,65.00));
        dbHandler.addProduct(new Product("Brown Color",11,85.00));




//        Appointment appointment = new Appointment();
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MINUTE,45);
//        appointment.setcDateAndTime(calendar);
//        appointment.setCustomerID(1);
//        dbHandler.addAppointment(appointment);
//
//
//        Appointment appointment1 = new Appointment();
//        calendar.add(Calendar.MINUTE,45);
//        appointment1.setcDateAndTime(calendar);
//        appointment1.setCustomerID(1);
//        dbHandler.addAppointment(appointment1);
//
//        Appointment appointment2 = new Appointment();
//        calendar.add(Calendar.MINUTE,45);
//        appointment2.setcDateAndTime(calendar);
//        appointment2.setCustomerID(1);
//        dbHandler.addAppointment(appointment2);
        editor.apply();



//        Appointments


        ArrayList<Customer> customerList = dbHandler.getAllCustomers();


        Calendar myCalendar = Calendar.getInstance();

        myCalendar.set(Calendar.DAY_OF_MONTH,1);
        myCalendar.set(Calendar.HOUR_OF_DAY,6);
        myCalendar.set(Calendar.MINUTE,0);


        for (Customer customerProfile :
                customerList) {

//            Appointment(int _id, Calendar cDateAndTime, int customerID, int tackAnHaircut, int haircutTime , double haircutPrice)
            Appointment appointment;



            if(customerProfile.getGender()==1) {
                appointment = new Appointment(
                        -1,
                        myCalendar,
                        customerProfile.get_id(),
                        1,
                        settings.getInt(UserDBConstants.USER_MALE_HAIRCUT_TIME,25),
                        settings.getInt(UserDBConstants.USER_MALE_HAIRCUT_PRICE,35)
                        );
                myCalendar.add(Calendar.MINUTE,settings.getInt(UserDBConstants.USER_MALE_HAIRCUT_TIME,45));

            }else{
                appointment = new Appointment(
                        -1,
                        myCalendar,
                        customerProfile.get_id(),
                        1,
                        settings.getInt(UserDBConstants.USER_FEMALE_HAIRCUT_TIME,45),
                        settings.getInt(UserDBConstants.USER_FEMALE_HAIRCUT_PRICE,150)
                );
                myCalendar.add(Calendar.MINUTE,settings.getInt(UserDBConstants.USER_FEMALE_HAIRCUT_TIME,45));
            }


            if(dbHandler.addAppointment(appointment)) {
                Log.d("Defalut Date", "New Appointment");

//                NewAppointmentActivity.this.finish();
            }

//            if(customerProfile.getRemainder()==1){
//                dbHandler.addMessageRemainder(
//                        new Message(
//                                0,customerProfile.get_id(),newAppointment.get_id(),0,newAppointment.getcDateAndTime()));
//            }
//

        }
    }

}

