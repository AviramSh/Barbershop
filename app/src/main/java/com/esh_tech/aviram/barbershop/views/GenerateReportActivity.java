package com.esh_tech.aviram.barbershop.views;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.R;
import com.esh_tech.aviram.barbershop.Utils.DateUtils;
import com.esh_tech.aviram.barbershop.Utils.MailUtils;
import com.esh_tech.aviram.barbershop.data.Appointment;
import com.esh_tech.aviram.barbershop.data.Config;
import com.esh_tech.aviram.barbershop.data.Customer;
import com.esh_tech.aviram.barbershop.data.MyGlobalUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//import com.mobilit.wastetrack.R;
//import com.mobilit.wastetrack.WasteTrackApp;
//import com.mobilit.wastetrack.data.Ride;
//import com.mobilit.wastetrack.utils.DateUtils;
//import com.mobilit.wastetrack.utils.MailUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GenerateReportActivity extends AppCompatActivity {

    //    Database
    BarbershopDBHandler dbHandler;

    EditText targetMailEdit, plateNumber;
    EditText fromDateEdit, toDateEdit;
    MyGlobalUser myApp;
    String reportEmail;
    SharedPreferences settings;
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST = 1;
    Calendar fromDate, toDate;
    Spinner rideStatusSpinner;
    DatePickerDialog startDateDialog, endDateDialog;
    SimpleDateFormat dateFormatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_report);
//
//        myApp = (WasteTrackApp) getApplication();
//        siteManager = myApp.getUsername();
//        siteName = myApp.getSiteName();
        initComponents();

        handleDates();

    }

    public void initComponents() {
        dbHandler = new BarbershopDBHandler(this);
        this.settings = PreferenceManager.getDefaultSharedPreferences(this);

        targetMailEdit = (EditText) findViewById(R.id.targetMailEdit);
//        targetMailEdit.setText(myApp.getEmail());
        targetMailEdit.setText(settings.getString(UserDBConstants.USER_EMAIL,""));

        /*rideStatusSpinner = (Spinner) findViewById(R.id.ridesStatusSpinner);*/
//        rideStatusSpinner.setSelection(2);
//        plateNumber = (EditText) findViewById(R.id.licensePlateEdit);

        fromDateEdit = (EditText) findViewById(R.id.fromDateEdit);
        toDateEdit = (EditText) findViewById(R.id.endingDateEdit);

        fromDateEdit.setText(getString(R.string.allAppointments));
        toDateEdit.setText(getString(R.string.allAppointments));
    }

    public void resetTime(Calendar c, boolean startDay) {
        if (startDay) {
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
        } else {
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MILLISECOND, 0);
        }
    }

    public void handleDates() {

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        fromDate = Calendar.getInstance();
        resetTime(fromDate, true);
        toDate = Calendar.getInstance();
        resetTime(toDate, false);

        Calendar newCalendar = Calendar.getInstance();
        startDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                resetTime(newDate, true);
                fromDate = newDate;
                fromDateEdit.setText(dateFormatter.format(fromDate.getTime()));

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        endDateDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                resetTime(newDate, false);
                toDate = newDate;
                toDateEdit.setText(dateFormatter.format(toDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        fromDateEdit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                startDateDialog.updateDate(fromDate.getTime().getYear() + 1900, fromDate.getTime().getMonth(), fromDate.getTime().getDate());
                startDateDialog.show();
                return true;
            }
        });

        toDateEdit.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                endDateDialog.updateDate(toDate.getTime().getYear() + 1900, toDate.getTime().getMonth(), toDate.getTime().getDate());
                endDateDialog.show();
                return true;
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.try_again, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void generateReport(View view) {
//
//        Calendar rideStartDate = Calendar.getInstance();
//        rideStartDate.setTime(DateUtils.getDateFromString(ride.getStartDate()));
//
//        fromDateEdit.getText().toString();
//        toDateEdit.getText().toString();
        String myTest ="";
        myTest +=fromDateEdit.getText().toString()+"-"+toDateEdit.getText().toString();
        Toast.makeText(this,myTest, Toast.LENGTH_SHORT).show();


        if(fromDate.after(toDate)){
            Calendar replaceDate = fromDate;
            fromDate = toDate;
            toDate= replaceDate;
        }


        reportEmail = targetMailEdit.getText().toString();

        if(!reportEmail.equals("")) {
            ArrayList<Appointment> appointments = dbHandler.getAllAppointmentsFromTo(fromDate,toDate);

            if (appointments.size() > 0) {
                new GenerateReportTask(appointments).execute();
            } else {
                Toast.makeText(GenerateReportActivity.this, R.string.no_haircuts_for_this_dates, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(GenerateReportActivity.this, R.string.report_filed, Toast.LENGTH_LONG).show();
        }

    }

    class GenerateReportTask extends AsyncTask<Void, Void, Boolean> {

        ArrayList<Appointment> appointmentList;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(GenerateReportActivity.this);
            pd.show();
        }

        public GenerateReportTask(ArrayList<Appointment> appointmentList) {
            this.appointmentList = appointmentList;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            if (result)
                Toast.makeText(GenerateReportActivity.this, R.string.report_generated, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(GenerateReportActivity.this, R.string.report_failure, Toast.LENGTH_LONG).show();

            finish();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if (ContextCompat.checkSelfPermission(GenerateReportActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(GenerateReportActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE_REQUEST);
            } else {

                File folder = new File(Environment.getExternalStorageDirectory() + "/Barbershop");

                boolean var = false;
                if (!folder.exists())
                    var = folder.mkdir();


                final String filename = folder.toString() + "/" + "Report1.csv";

                try {

                    PrintWriter writer = new PrintWriter(filename, "UTF-8");
                    writer.write('\ufeff');
                    writer.println("Appointment ID,Customer Name,Customer Phone,Customer Bill,Appointment Date A Time");
//
//                    for (Ride ride : appointmentList) {
//                        writer.println(
//                                ride.getRideID() + "," +
//                                        ride.getLicensePlateNum() + "," +
//                                        ride.getDriverName() + "," +
//                                        ride.getDisposalCompanyName() + "," +
//                                        ride.getVolume() + "," +
//                                        ride.getWeight() + "," +
//                                        ride.getOriginSiteName() + "," +
//                                        ride.getTargetSiteName() + "," +
//                                        ((ride.isOpen()) ? getString(R.string.openRides) : getString(R.string.closedRides)) + "," +
//                                        DateUtils.convertDateForReport(ride.getStartDate()) + "," +
//                                        DateUtils.convertDateForReport(ride.getEndDate()));
//                    }

                    double calc = 0;

                    for (Appointment appointment : appointmentList) {


                        Customer customer = dbHandler.getCustomerByID(appointment.getCustomerID());

                        writer.println(
                                appointment.get_id() + "," +
                                        customer.getName() + "," +
                                        customer.getPhone() + "," +
                                        customer.getBill() + "," +
                                        DateUtils.setCalendarToDB(appointment.getcDateAndTime()));
                    }

                    writer.close();

                    MailUtils.sendMail(GenerateReportActivity.this, reportEmail, filename);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            return true;

        }
    }
}
