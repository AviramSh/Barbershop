package com.esh_tech.aviram.barbershop.views;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;


import com.esh_tech.aviram.barbershop.Utils.DateUtils;
import com.esh_tech.aviram.barbershop.data.*;
import com.esh_tech.aviram.barbershop.R;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AppointmentListActivity extends AppCompatActivity implements View.OnClickListener{

    //Calendar
//    final Calendar appointmentCalendar = Calendar.getInstance();

    Calendar newCalendar;
    Button theDate;


    CheckBox rbGetAllHaircut;
    CheckBox rbGetHaircut;
    CheckBox rbDidntgetHaircut;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        Database
        dbHandler = new BarbershopDBHandler(this);

//        Set Date
        theDate = (Button)findViewById(R.id.btDate);
        newCalendar = Calendar.getInstance();

        rbGetAllHaircut =(CheckBox)findViewById(R.id.rbGetAllHaircut);
        rbGetHaircut =(CheckBox)findViewById(R.id.rbGetHaircut);
        rbDidntgetHaircut =(CheckBox)findViewById(R.id.rbDidntGetHaircut);

        rbGetAllHaircut.setChecked(true);
        rbGetHaircut.setChecked(false);
        rbDidntgetHaircut.setChecked(false);

//        setTheDate();


//        Connect list view
        lvAppointment = (ListView) findViewById(R.id.lvAppointment);



//        fill components
        populateAppointment();

//        Connect adapter with custom view
        appointmentAdapter = new MyAppointmentsAdapter(this, R.layout.custom_appointment_row, allAppointments);
        lvAppointment.setAdapter(appointmentAdapter);
        lvAppointment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                final Customer c1 = dbHandler.getCustomerByID(allAppointments.get(i).getCustomerID());
                final Appointment a1 = allAppointments.get(i);
                Toast.makeText(AppointmentListActivity.this, " "+c1.getName(), Toast.LENGTH_SHORT).show();

                final AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentListActivity.this);
                builder.setTitle(R.string.manageAppointment)
                        .setMessage(getResources().getString(
                                R.string.are_you_sure_you_whant_to_delete)+
                                " \n"+getResources().getString(R.string.appointment)
                                +": "+c1.getName()+" "+DateUtils.getDateAndTime(a1.getcDateAndTime())
                        )
                        .setNegativeButton(R.string.delete_appointment, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(dbHandler.deleteAppointmentById(a1.get_id())) {
                                    Toast.makeText(AppointmentListActivity.this, c1.getName() + " " +
                                            getResources().getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                                    populateAppointment();
                                }
                            }
                        })
                        .setPositiveButton(R.string.gatHaircut, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(AppointmentListActivity.this, c1.getName()+" Got Haircut", Toast.LENGTH_SHORT).show();
                                a1.setTackAnHaircut(1);
                                dbHandler.updateAppointment(a1);
                                populateAppointment();
                            }
                        }).create();

                builder.show();


                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btNextDay:
                restDate(1);
                break;
            case R.id.btDayBack:
                restDate(-1);
                break;
            case R.id.btDate:
                showDialog(v);
                break;
            case R.id.rbGetAllHaircut:
//                rbGetAllHaircut.setChecked(!rbGetAllHaircut.isChecked());
                rbGetHaircut.setChecked(false);
                rbDidntgetHaircut.setChecked(false);
                restDate(0);
                break;
            case R.id.rbGetHaircut:
                restDate(0);
                break;
            case R.id.rbDidntGetHaircut:
                restDate(0);
                break;
//            case R.id.btBack:
//                this.finish();
//                break;
            default:
                this.finish();
                Toast.makeText(this, "Not Initialized yet", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void populateAppointment() {

        if(DateUtils.getOnlyDate(newCalendar).contains(DateUtils.getOnlyDate(Calendar.getInstance()))) {
            theDate.setText(DateUtils.getOnlyDate(newCalendar)+" \n"+
            getResources().getString(R.string.today));
        }else{
            theDate.setText(DateUtils.getDateAndName(newCalendar));
        }

//        if(dateForDisplay.contains(new DateUtils().getOnlyDateSDF(Calendar.getInstance()))) {
//            theDate.setText(new DateUtils().getOnlyDateSDF(Calendar.getInstance())+" \n"+
//            getResources().getString(R.string.today));
//        }else{
//            theDate.setText(dateForDisplay);
//        }


        allAppointments = dbHandler.getAllAppointments(newCalendar);

    }

    public void restDate(int day) {
        newCalendar.add(Calendar.DAY_OF_MONTH, day);
        populateAppointment();
        appointmentAdapter.notifyDataSetChanged();
        appointmentAdapter = new MyAppointmentsAdapter(this, R.layout.custom_appointment_row, allAppointments);
        lvAppointment.setAdapter(appointmentAdapter);
    }

    private class MyAppointmentsAdapter extends ArrayAdapter<Appointment> {

        private MyAppointmentsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Appointment> objects) {
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
            tvTime.setText(DateUtils.getOnlyTime(appointment.getcDateAndTime()));

            if(rbGetHaircut.isChecked()&&appointment.getTackAnHaircut()==1){
                tvName.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                tvTime.setTextColor(getResources().getColor(android.R.color.holo_green_light));
                rbGetAllHaircut.setChecked(false);
            }else if(rbDidntgetHaircut.isChecked()&&appointment.getTackAnHaircut() == 0 &&
                    new DateUtils().compareDates(
                            appointment.getcDateAndTime(),Calendar.getInstance()) == 1){
                tvName.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                tvTime.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                rbGetAllHaircut.setChecked(false);
            }

            appointmentAdapter.notifyDataSetChanged();
            return convertView;
        }
    }



    public void showDialog(View v) {

        switch (v.getId()) {
            case R.id.btDate:
                showDialog(DIALOG_ID);
                break;
//            case R.id.btTime:
//                showDialog(DIALOG_ID_TIME);
//                break;
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
            restDate(0);
        }
    };




}
