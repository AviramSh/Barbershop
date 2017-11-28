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
import android.widget.ProgressBar;

import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.JobsHandler.MJobScheduler;
import com.esh_tech.aviram.barbershop.R;
import com.esh_tech.aviram.barbershop.data.Customer;

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


        settings = PreferenceManager.getDefaultSharedPreferences(this);
        loading = (ProgressBar) findViewById(R.id.progressBar);
        loading.computeScroll();
        if (!settings.getBoolean(USER_IS_REGISTER,false))
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

    private void setUpDefaultData() {

        editor = settings.edit();

        editor.putString(USER_NAME, "אורן");
        editor.putString(USER_LAST_NAME, "לוי");
        editor.putString(USER_PHONE, "0506792353");
        editor.putString(USER_BUSINESS_NAME, "אורן עיצוב");
        editor.putString(USER_BUSINESS_PHONE, "039000000");
        editor.putString(USER_BUSINESS_ADDRESS, "מרבד הקסמים 1 ראש העין");

        editor.putString(USER_PASSWORD, "1234");
        editor.putBoolean(USER_AUTO_LOGIN, true);
        editor.putBoolean(USER_IS_REGISTER, true);

        editor.putInt(USER_FEMALE_HAIRCUT_TIME, 45);
        editor.putInt(USER_MALE_HAIRCUT_PRICE, 35);
        editor.putInt(USER_MALE_HAIRCUT_TIME, 35);
        editor.putInt(USER_FEMALE_HAIRCUT_PRICE, 45);
        editor.putInt(USER_FEMALE_HAIRCUT_TIME, 45);

        editor.putString(UserDBConstants.USER_EMAIL, "aviram.note@gmail.com");
        editor.putString(UserDBConstants.USER_EMAIL_PASSWORD, "avi304287");




        editor.putString(SUNDAY_TIME_OPEN,"06:00:18:00");

        editor.putString(MONDAY_TIME_OPEN,"06:00:18:00");

        editor.putString(TUESDAY_TIME_OPEN,"06:00:18:00");

        editor.putString(WEDNESDAY_TIME_OPEN,"06:00:18:00");

        editor.putString(THURSDAY_TIME_OPEN,"06:00:18:00");

        editor.putString(FRIDAY_TIME_OPEN,"06:00:18:00");

        editor.putString(SATURDAY_TIME_OPEN,"");

        dbHandler = new BarbershopDBHandler(SplashScreenActivity.this);

        Customer p1 =new Customer();
        p1.set_id(1);
        p1.setName(getResources().getString(R.string.guest));
        dbHandler.addCustomer(p1);

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
    }
}

