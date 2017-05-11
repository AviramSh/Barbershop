package com.esh_tech.aviram.barbershop;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

import java.util.ArrayList;
import java.util.List;

public class AppointmentListActivity extends AppCompatActivity {

    ArrayList<Appointment> allAppointments =new ArrayList<Appointment>();
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

//        Connect list view
        lvAppointment =(ListView)findViewById(R.id.lvAppointment);

//        fill components
        populateAppointment();

//        Connect adapter with custom view
        appointmentAdapter= new MyAppointmentsAdapter(this,R.layout.custom_appointment_row,allAppointments);

        lvAppointment.setAdapter(appointmentAdapter);
    }

    private void populateAppointment() {

//minutes, hour, day,month, year, customerName, customerID

        dbHandler.addAppointment(new Appointment(15,8,4,5,1987,"Avi",1));//0505000001
        dbHandler.addAppointment(new Appointment(30,8,4,5,1987,"Tal",2));//0505000002
        dbHandler.addAppointment(new Appointment(50,8,4,5,1987,"Michal shatrr",3));//0505000003
        dbHandler.addAppointment(new Appointment(13,8,4,5,1987,"avi",4));//0505000004
        dbHandler.addAppointment(new Appointment(13,8,4,5,1987,"avi",5));//0505000005
        dbHandler.addAppointment(new Appointment(13,8,4,5,1987,"avi",6));//0505000006


        
        
        allAppointments = dbHandler.getAllAppointments();

    }

    class MyAppointmentsAdapter extends ArrayAdapter<Appointment> {

        public MyAppointmentsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Appointment> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            Appointment appointment = getItem(position);

            if (convertView == null){
                Log.e("Test get view","inside if with position"+position);
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_appointment_row,parent,false);
            }

            TextView tvName = (TextView) convertView.findViewById(R.id.tvCustomerName);

            TextView tvTime = (TextView)convertView.findViewById(R.id.tvCustomerTime);



//            Data
            tvName.setText(appointment.getCustomerName());
            tvTime.setText(String.valueOf(appointment.getHour()) +":"+ String.valueOf(appointment.getMinutes()));

//            if(appointment.isGender())customerIcon.setImageResource(R.drawable.usermale48);
//            else customerIcon.setImageResource(R.drawable.userfemale48);

            return convertView;
        }
    }
}
