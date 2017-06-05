package com.esh_tech.aviram.barbershop;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

import java.text.SimpleDateFormat;

public class BalanceActivity extends AppCompatActivity {


    //    Database
    BarbershopDBHandler dbHandler;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);


        init();
    }

    private void init() {
        this.setTitle(R.string.balance);
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        year_x = startCalendar.get(Calendar.YEAR);
        month_x = startCalendar.get(Calendar.MONTH);;
        day_x = startCalendar.get(Calendar.DAY_OF_MONTH);;

        btFromDate = (Button)findViewById(R.id.btDateFrom);
        btUntilDate = (Button)findViewById(R.id.btDateUntil);

        btFromDate.setText(getDateToDisplay(startCalendar));
        btUntilDate.setText(getDateToDisplay(startCalendar));
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

            endCalendar.set(year_x, month_x, day_x);
            btUntilDate.setText(getDateToDisplay(endCalendar));
        }
    };
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
