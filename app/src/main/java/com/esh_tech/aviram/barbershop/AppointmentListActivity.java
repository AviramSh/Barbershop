package com.esh_tech.aviram.barbershop;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AppointmentListActivity extends AppCompatActivity {

    //Calendar
//    final Calendar appointmentCalendar = Calendar.getInstance();

    Calendar newCalendar;
    Button theDate;

    static final int DIALOG_ID = 0;
    static final int DIALOG_ID_TIME = 1;

    //    View
    ArrayList<Appointment> allAppointments;
    ListView lvAppointment;
    MyAppointmentsAdapter appointmentAdapter;

    //    Database
    BarbershopDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);

        init();
    }

    private void init() {
        this.setTitle(R.string.appointments);

//        Database
        dbHandler = new BarbershopDBHandler(this);

//        Set Date
        theDate = (Button)findViewById(R.id.btDate);
        newCalendar = Calendar.getInstance();
//        setTheDate();


//        Connect list view
        lvAppointment = (ListView) findViewById(R.id.lvAppointment);

//        fill components
        populateAppointment();

//        Connect adapter with custom view
        appointmentAdapter = new MyAppointmentsAdapter(this, R.layout.custom_appointment_row, allAppointments);
        lvAppointment.setAdapter(appointmentAdapter);
    }

    private void populateAppointment() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy \n EEEE", Locale.getDefault());
        String dateForDisplay = sdf.format(newCalendar.getTime());
        theDate.setText(dateForDisplay);

        sdf = new SimpleDateFormat("dd/MM/yyyy",Locale.getDefault());
        dateForDisplay = sdf.format(newCalendar.getTime());

        allAppointments = dbHandler.getAllAppointments(dateForDisplay);


    }

    public void nextDay(View view) {

        newCalendar.add(Calendar.DAY_OF_MONTH, 1);
//        setTheDate();
        populateAppointment();
        appointmentAdapter = new MyAppointmentsAdapter(this, R.layout.custom_appointment_row, allAppointments);
        lvAppointment.setAdapter(appointmentAdapter);

    }
    public void dayBack(View view) {
        newCalendar.add(Calendar.DAY_OF_MONTH, -1);
//        setTheDate(newCalendar);
        populateAppointment();
        appointmentAdapter.notifyDataSetChanged();
        appointmentAdapter = new MyAppointmentsAdapter(this, R.layout.custom_appointment_row, allAppointments);
        lvAppointment.setAdapter(appointmentAdapter);
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

            tvName.setText(dbHandler.getCustomerByID(appointment.getCustomerID()).getName());
            tvTime.setText(appointment.getDateAndTimeToDisplay());

            appointmentAdapter.notifyDataSetChanged();
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
            return new DatePickerDialog(this,
                    dpickerListener,
                    newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        return null;
    }


    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            newCalendar.set(year,month,dayOfMonth);
            populateAppointment();
        }
    };




}
