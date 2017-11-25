package com.esh_tech.aviram.barbershop.JobsHandler;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.data.Appointment;
import com.esh_tech.aviram.barbershop.views.SplashScreenActivity;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Aviram on 19/11/2017.
 */

public class MJobExecuter extends AsyncTask <Void,Void,String>{

    private static final String TAG = "Programmer";

    private Context context;
    private BarbershopDBHandler dbHandler;
    SQLiteDatabase db;

    public MJobExecuter(Context applicationContext) {
        this.context = applicationContext;
    }

    @Override
    protected String doInBackground(Void... voids) {
//        TODO Create an SMS Handler Scheduler for this service.
        dbHandler = dbHandler = new BarbershopDBHandler(context);
        ArrayList<Appointment> appointments = dbHandler.getAllAppointments(Calendar.getInstance());

        Log.d(TAG,"Background Long Running Task Finished...");

        return "Sms db have been tested.";
    }
}
