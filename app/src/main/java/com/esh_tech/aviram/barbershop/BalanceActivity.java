package com.esh_tech.aviram.barbershop;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.text.PluralRules;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

public class BalanceActivity extends AppCompatActivity implements View.OnClickListener{


    //    Database
    BarbershopDBHandler dbHandler;
    private double bill;
    private int haircutCounter;

    ArrayList<Purchase> myPurchases;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);


        init();
    }

    private void init() {
        this.setTitle(R.string.balance);

        dbHandler = new BarbershopDBHandler(this);

        etBill = (TextView)findViewById(R.id.etPayment);
        etCounter= (TextView)findViewById(R.id.etCounter);

        bill = 0;
        haircutCounter =0;

        myPurchases = new ArrayList<Purchase>();

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

    private String getDateToDisplay(Calendar startCalendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
        String newFormat = formatter.format(startCalendar.getTime());
        return newFormat;
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

            if(startCalendar.getTimeInMillis() > endCalendar.getTimeInMillis()) {
                endCalendar.set(year_x, month_x, day_x);
            }else{
                endCalendar.setTime(startCalendar.getTime());
            }

            btUntilDate.setText(getDateToDisplay(endCalendar));
        }
    };

    @Override
    public void onClick(View v) {


        switch (v.getId()){
            case R.id.ibCalc:
                calculateBill();
                break;

            default:

                break;
        }

    }


    private void calculateBill() {



        if (startCalendar.getTimeInMillis() > endCalendar.getTimeInMillis()) {
            Toast.makeText(this, R.string.wrongDate, Toast.LENGTH_SHORT).show();
        }else{

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            String startFormat = formatter.format(startCalendar.getTime());
            String endFormat = formatter.format(endCalendar.getTime());


//            myPurchases = dbHandler.getPurchaseByDate(startCalendar,endCalendar);
            Toast.makeText(this, startFormat + " - "+endFormat , Toast.LENGTH_SHORT).show();
//            haircutCounter = dbHandler.getBillByDate(startFormat,endFormat);
        }
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


}
