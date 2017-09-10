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
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.R;
import com.esh_tech.aviram.barbershop.Utils.MailUtils;
import com.esh_tech.aviram.barbershop.data.Appointment;
import com.esh_tech.aviram.barbershop.data.Customer;
import com.esh_tech.aviram.barbershop.data.MyGlobalUser;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CustomersReportActivity extends AppCompatActivity {

//    TODO Need to test this activity and mail report.

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
        setContentView(R.layout.activity_customers_report);
        initComponents();

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
        /*String myTest ="";
        myTest +=fromDateEdit.getText().toString()+"-"+toDateEdit.getText().toString();
        Toast.makeText(this,myTest, Toast.LENGTH_SHORT).show();
*/

  /*      if(fromDate.after(toDate)){
            Calendar replaceDate = fromDate;
            fromDate = toDate;
            toDate= replaceDate;
        }*/


        reportEmail = targetMailEdit.getText().toString();

        if(!reportEmail.equals("")) {
            ArrayList<Customer> customers = dbHandler.getAllCustomers();

            if (customers.size() > 0) {
                new CustomersReportActivity.GenerateReportTask(customers).execute();
            } else {
                Toast.makeText(CustomersReportActivity.this, R.string.customers_list_is_empty, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(CustomersReportActivity.this, R.string.report_filed, Toast.LENGTH_LONG).show();
        }

    }

    class GenerateReportTask extends AsyncTask<Void, Void, Boolean> {

        ArrayList<Customer> customersList;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CustomersReportActivity.this);
            pd.show();
        }

        public GenerateReportTask(ArrayList<Customer> customerList) {
            this.customersList = customerList;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            if (result)
                Toast.makeText(CustomersReportActivity.this, R.string.report_generated, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(CustomersReportActivity.this, R.string.report_failure, Toast.LENGTH_LONG).show();

            finish();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if (ContextCompat.checkSelfPermission(CustomersReportActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CustomersReportActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE_REQUEST);
            } else {

                File folder = new File(Environment.getExternalStorageDirectory() + "/Barbershop");

                boolean var = false;
                if (!folder.exists())
                    var = folder.mkdir();


                final String filename = folder.toString() + "/" + "Customers_Report.csv";

                try {

                    PrintWriter writer = new PrintWriter(filename, "UTF-8");
                    writer.write('\ufeff');
                    writer.println("Customer ID,Customer Name,Customer Phone,Customer Bill,Customer Gender ,Customer Birthday");
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

                    for (Customer customer : customersList) {


                        writer.println(
                                customer.get_id() + "," +
                                        customer.getName() + "," +
                                        customer.getPhone() + "," +
                                        customer.getBill() + "," +
                                        customer.getGender() + "," +
                                        customer.getBirthday());
                    }

                    writer.close();

                    MailUtils.sendMail(CustomersReportActivity.this, reportEmail, filename);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            return true;

        }
    }
}




