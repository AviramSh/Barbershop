package com.esh_tech.aviram.barbershop.views;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.CustomersDBConstants;
import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

import com.esh_tech.aviram.barbershop.R;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import com.esh_tech.aviram.barbershop.Utils.DateUtils;
import com.esh_tech.aviram.barbershop.Utils.MailUtils;
import com.esh_tech.aviram.barbershop.data.*;

public class BalanceActivity extends AppCompatActivity implements View.OnClickListener{


    //    Database
    BarbershopDBHandler dbHandler;
    private double bill;
    private int haircutCounter;

    SharedPreferences settings;


    ArrayList<Purchase> allPurchases;
    ImageButton ibCalc;
    ImageButton ibReport;

    //Calendar
    Calendar startCalendar;
    Calendar endCalendar;
    Button btFromDate;
    Button btUntilDate;
    int year_x;
    int month_x;
    int day_x;
    static final int DIALOG_ID = 0;
    static final int DIALOG_ID_2 = 1;

    TextView etBill;
    TextView etCounter;


    AlertDialog.Builder mBuilder;
    String reportEmail;
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST = 144;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);


        init();
    }

    private void init() {

        dbHandler = new BarbershopDBHandler(this);
        settings = PreferenceManager.getDefaultSharedPreferences(this);

        etBill = (TextView)findViewById(R.id.etPayment);
        etCounter= (TextView)findViewById(R.id.etCounter);

        ibCalc = (ImageButton)findViewById(R.id.ibCalcHaircut);
        ibReport  = (ImageButton)findViewById(R.id.ibCreateReport);

        ibCalc.setOnClickListener(this);
        ibReport.setOnClickListener(this);

        bill = 0;
        haircutCounter = 0;

        allPurchases = new ArrayList<Purchase>();

        etBill.setText(String.valueOf(bill));
        etCounter.setText(String.valueOf(haircutCounter));


        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();

        startCalendar.set(Calendar.DAY_OF_MONTH,1);

        year_x = startCalendar.get(Calendar.YEAR);
        month_x = startCalendar.get(Calendar.MONTH);;
        day_x = startCalendar.get(Calendar.DAY_OF_MONTH);;

        btFromDate = (Button)findViewById(R.id.btDateFrom);
        btUntilDate = (Button)findViewById(R.id.btDateUntil);

        btFromDate.setText(getDateToDisplay(startCalendar));
        btUntilDate.setText(getDateToDisplay(endCalendar));
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.ibCalcHaircut:
                calculateBill();
                break;
            case R.id.ibCreateReport:
//                Intent intent = new Intent(this,GenerateReportActivity.class);
//                startActivity(intent);
//                this.finish();
                generateReport();
//                    Toast.makeText(this, R.string.the_report_was_sent, Toast.LENGTH_SHORT).show();
//                }else{
//                    Toast.makeText(this, R.string.the_report_was_not_sent, Toast.LENGTH_SHORT).show();
//
//                }
                break;

            default:

                break;
        }

    }

    private String getDateToDisplay(Calendar startCalendar) {
//        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
//        formatter.format(startCalendar.getTime());
        return DateUtils.getOnlyDate(startCalendar);

    }

    public void closeBalance(View view) {
        this.finish();
    }

    public void showDialog(View v) {

        if(v.getId() == R.id.btDateFrom){
            showDialog(DIALOG_ID);
        }else if(v.getId() == R.id.btDateUntil){
            showDialog(DIALOG_ID_2);
        }

    }



    @Override
    protected Dialog onCreateDialog(int id){

        if(id == DIALOG_ID)
            return new DatePickerDialog(this, dpickerListener,year_x,month_x,day_x);
        else if(id == DIALOG_ID_2)
            return new DatePickerDialog(this, dpickerListener2,year_x,month_x,day_x);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month;
            day_x = dayOfMonth;

            startCalendar.set(year_x, month_x, day_x);
            btFromDate.setText(getDateToDisplay(startCalendar));

        }
    };

    private DatePickerDialog.OnDateSetListener dpickerListener2
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month;
            day_x = dayOfMonth;

//            if(startCalendar.getTimeInMillis() > endCalendar.getTimeInMillis()) {
                endCalendar.set(year_x, month_x, day_x);
//            }else{
//                endCalendar.setTime(startCalendar.getTime());
//            }

            btUntilDate.setText(getDateToDisplay(endCalendar));
        }
    };


    private void calculateBill() {

        bill = 0;
        haircutCounter = 0;


        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        String startFormat = formatter.format(startCalendar.getTime());
        String endFormat = formatter.format(endCalendar.getTime());

        Calendar tempStart = Calendar.getInstance();


        if (startCalendar.getTimeInMillis() > endCalendar.getTimeInMillis()) {
            tempStart.setTime(startCalendar.getTime());
            startCalendar.setTime(endCalendar.getTime());
            endCalendar.setTime(tempStart.getTime());
        }
        tempStart.setTime(startCalendar.getTime());
        Toast.makeText(this, getDateToDisplay(startCalendar)+ " - "+getDateToDisplay(endCalendar)
                , Toast.LENGTH_SHORT).show();

        tempStart.setTime(startCalendar.getTime());
        while (tempStart.getTimeInMillis() <= endCalendar.getTimeInMillis()) {

            allPurchases = dbHandler.getPurchaseByDate(tempStart);
            for (Purchase indexPurchase :
                    allPurchases) {
                bill +=indexPurchase.getPrice();
                haircutCounter++;
            }

            tempStart.add(Calendar.DATE,1);
        }

        etBill.setText(String.valueOf(bill));
        etCounter.setText(String.valueOf(haircutCounter));

    }
//    private TimePickerDialog.OnTimeSetListener tpickerListener
//            = new TimePickerDialog.OnTimeSetListener() {
//        @Override
//        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            hour_x=hourOfDay;
//            minute_x=minute;
//
//            theTime.setText(hour_x+":"+minute_x);
//
//            Toast.makeText(NewAppointmentActivity.this, hour_x+":"+minute_x, Toast.LENGTH_SHORT).show();
//            populateAppointment();
//        }
//    };





//     Report handler

    private boolean generateReport() {

//        String myTest ="";
//        myTest +=fromDateEdit.getText().toString()+"-"+toDateEdit.getText().toString();
//        Toast.makeText(this,myTest, Toast.LENGTH_SHORT).show();


        if(startCalendar.after(endCalendar)){
            Calendar replaceDate = startCalendar;
            startCalendar = endCalendar;
            startCalendar= replaceDate;
        }



        createReportDialog();


        return false;
    }

    private void createReportDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.send_report);
        alertDialog.setMessage(
                getResources().getString(R.string.send_bills_reports_from)+
                        "\n"+DateUtils.getOnlyDate(startCalendar)+" - "+DateUtils.getOnlyDate(endCalendar));

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.send) ,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        reportEmail = settings.getString(UserDBConstants.USER_EMAIL,"");

                        if(!reportEmail.equals("")) {
                            ArrayList<Appointment> appointments = dbHandler.getAllAppointmentsFromTo(startCalendar,endCalendar);

                            if (appointments.size() > 0) {
                                new GenerateReportTask(appointments).execute();
                            } else {
                                Toast.makeText(BalanceActivity.this, R.string.no_haircuts_for_this_dates, Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(BalanceActivity.this, R.string.report_filed, Toast.LENGTH_LONG).show();
                        }

                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.cancel) ,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        alertDialog.show();


//
//        mBuilder.setNeutralButton(R.string.saveBt, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//
//            }
//        });
//
//
//        mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                Toast.makeText(StockActivity.this, R.string.cancel, Toast.LENGTH_LONG).show();
//            }
//        });
//
//
//        mBuilder.setView(mView);
//        AlertDialog dialog = mBuilder.create();
//        dialog.show();
    }

    class GenerateReportTask extends AsyncTask<Void, Void, Boolean> {

        ArrayList<Appointment> appointmentList;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(BalanceActivity.this);
            pd.show();
        }

        public GenerateReportTask(ArrayList<Appointment> appointmentList) {
            this.appointmentList = appointmentList;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            if (result)
                Toast.makeText(BalanceActivity.this, R.string.report_generated, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(BalanceActivity.this, R.string.report_failure, Toast.LENGTH_LONG).show();

            finish();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if (ContextCompat.checkSelfPermission(BalanceActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(BalanceActivity.this,
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
                    writer.println("Appointment ID,Customer Name,Customer Phone,Customer Bill,Date,Paid");

                    double calc = 0;

                    for (Appointment appointment : appointmentList) {


                        Customer customer = dbHandler.getCustomerByID(appointment.getCustomerID());

                        writer.println(
                                appointment.get_id() + "," +
                                        customer.getName() + "," +
                                        customer.getPhone() + "," +
                                        customer.getBill() + "," +
                                        DateUtils.setCalendarToDB(appointment.getcDateAndTime()) + "," +
                                        appointment.getHaircutPrice());
                        calc += 0;appointment.getHaircutPrice();
                    }
                    writer.println(
                            "" + "," +
                                    "" + "," +
                                    "" + "," +
                                    "" + "," +
                                    "" + "," +
                                    calc);

                    writer.close();

                    MailUtils.sendMail(BalanceActivity.this, reportEmail, filename);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            return true;

        }
    }


}
