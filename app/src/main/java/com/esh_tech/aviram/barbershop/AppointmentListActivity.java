package com.esh_tech.aviram.barbershop;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AppointmentListActivity extends AppCompatActivity {

    //Calendar
    final Calendar c = Calendar.getInstance();
    Button theDate;
    Button theTime;
    int year_x;
    int month_x;
    int day_x;
    int hour_x;
    int minute_x;
    static final int DIALOG_ID = 0;
    static final int DIALOG_ID_TIME = 1;

    //    View
    ArrayList<Appointment> allAppointments = new ArrayList<Appointment>();
    ListView lvAppointment;
    MyAppointmentsAdapter appointmentAdapter;

    //    Database
    BarbershopDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);

        this.setTitle(R.string.appointments);

//        Database
        dbHandler = new BarbershopDBHandler(this);

//        Set Date
        theDate = (Button)findViewById(R.id.btDate);

        year_x = c.get(Calendar.YEAR);
        month_x = c.get(Calendar.MONTH);
        day_x = c.get(Calendar.DAY_OF_MONTH);

//        Connect list view
        lvAppointment = (ListView) findViewById(R.id.lvAppointment);

//        fill components
        populateAppointment(day_x, month_x, year_x);

//        Connect adapter with custom view
        appointmentAdapter = new MyAppointmentsAdapter(this, R.layout.custom_appointment_row, allAppointments);

        lvAppointment.setAdapter(appointmentAdapter);
    }

    private void populateAppointment(int dayOfMonth,int month,int year) {


        if(c.get(Calendar.YEAR)==year && c.get(Calendar.MONTH)==month &&c.get(Calendar.DAY_OF_MONTH)==dayOfMonth)
            theDate.setText(R.string.today);
        else theDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

//          minutes, hour, day,month, year, customerName, customerID


        allAppointments = dbHandler.getTodayAppointments(day_x, month_x, year_x);

        if(!allAppointments.isEmpty())
            Toast.makeText(this, allAppointments.get(0).getCustomerName(), Toast.LENGTH_SHORT).show();
        else {allAppointments.add(new Appointment(00,00,00,00,00,"",00));}

        lvAppointment.setAdapter(appointmentAdapter);

    }

    public void nextDay(View view) {
//        c.get(Calendar.M)
        populateAppointment(++day_x, month_x, year_x);
    }
    public void dayBack(View view) {
        populateAppointment(--day_x, month_x, year_x);
    }

    public void closeAppointmentList(View view) {
        Intent myIntent = new Intent(this,MainActivity.class);
        startActivity(myIntent);
        this.finish();
    }

    class MyAppointmentsAdapter extends ArrayAdapter<Appointment> {

        public MyAppointmentsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Appointment> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            Appointment appointment = getItem(position);

            if (convertView == null) {
                Log.e("Test get view", "inside if with position" + position);
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_appointment_row, parent, false);
            }

            TextView tvName = (TextView) convertView.findViewById(R.id.tvCustomerName);

            TextView tvTime = (TextView) convertView.findViewById(R.id.tvCustomerTime);


//            Data
            tvName.setText(appointment.getCustomerName());
            tvTime.setText(String.valueOf(appointment.getHour()) + ":" + String.valueOf(appointment.getMinutes()));

//            if(appointment.isGender())customerIcon.setImageResource(R.drawable.usermale48);
//            else customerIcon.setImageResource(R.drawable.userfemale48);

            return convertView;
        }
    }



    public void showDialog(View v) {

        switch (v.getId()) {
            case R.id.btDate:
                showDialog(DIALOG_ID);
                break;
            case R.id.btTime:
                showDialog(DIALOG_ID_TIME);
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == DIALOG_ID)
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        else if (id == DIALOG_ID_TIME)
            return new TimePickerDialog(this, tpickerListener, hour_x, minute_x, true);
        return null;
    }


    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month;
            day_x = dayOfMonth;

//            Toast.makeText(AppointmentListActivity.this, "" + year + "/" + (month + 1) + "/" + dayOfMonth, Toast.LENGTH_SHORT).show();
            populateAppointment(day_x, month_x, year_x);
        }
    };

    private TimePickerDialog.OnTimeSetListener tpickerListener
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_x=hourOfDay;
            minute_x=minute;

            theTime.setText(hour_x+":"+minute_x);

            Toast.makeText(AppointmentListActivity.this, hour_x+":"+minute_x, Toast.LENGTH_SHORT).show();
        }
    };



}
