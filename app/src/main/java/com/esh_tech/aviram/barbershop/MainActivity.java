package com.esh_tech.aviram.barbershop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<Appointment> allAppointments =new ArrayList<Appointment>();
    ListView lsNextAppointment;
    MyAppointmentsAdapter appointmentAdapter;

    //    Database
    BarbershopDBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //        Database
        dbHandler = new BarbershopDBHandler(this);

//        Connect list view
        lsNextAppointment = (ListView) findViewById(R.id.lvNextAppointment);

//        fill components
        populateAppointment();

//        Connect adapter with custom view
        appointmentAdapter= new MyAppointmentsAdapter(this,R.layout.custom_appointment_row,allAppointments);

        lsNextAppointment.setAdapter(appointmentAdapter);

        lsNextAppointment.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        appointmentHandler(position);
                        return false;
                    }
                }
        );



    }


//    Handling the Appointment list view
    private void appointmentHandler(final int position) {


        Toast.makeText(this, allAppointments.get(position).getCustomerName()+"", Toast.LENGTH_SHORT).show();


        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        mBuilder.setTitle(R.string.manageAppointment);
        mBuilder.setMessage(R.string.theCustomerGetAnHaircut);

        mBuilder.setNeutralButton(R.string.getHaircut, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, R.string.getHaircut, Toast.LENGTH_LONG).show();

//                Need to update database
                allAppointments.remove(position);
                appointmentAdapter.notifyDataSetChanged();
            }
        });


        mBuilder.setNegativeButton(R.string.DidntGetHaircut, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(MainActivity.this, R.string.DidntGetHaircut, Toast.LENGTH_LONG).show();

                allAppointments.remove(position);
                appointmentAdapter.notifyDataSetChanged();
            }
        });


        AlertDialog dialog = mBuilder.create();
        dialog.show();

    }

    private void populateAppointment() {

//minutes, hour, day,month, year, customerName, customerID
        allAppointments.add(new Appointment(15,8,4,5,1987,"Avi",3));
        allAppointments.add(new Appointment(30,8,4,5,1987,"Tal",3));
        allAppointments.add(new Appointment(50,8,4,5,1987,"Michal",3));
        allAppointments.add(new Appointment(13,8,4,5,1987,"avi",3));
        allAppointments.add(new Appointment(13,8,4,5,1987,"avi",3));
        allAppointments.add(new Appointment(13,8,4,5,1987,"avi",3));

    }

    private void settime() {

    }

    public void openCustomersList(View view) {
        Intent myIntent = new Intent(this ,CustomersListActivity.class);
        startActivity(myIntent);

    }

    public void openBalance(View view) {
        Intent myIntent = new Intent(this ,BalanceActivity.class);
        startActivity(myIntent);
    }

    public void openStock(View view) {
        Intent myIntent = new Intent(this ,StockActivity.class);
        startActivity(myIntent);
    }

    public void openAppointmentList(View view) {
        Intent myIntent = new Intent(this ,AppointmentListActivity.class);
        startActivity(myIntent);
    }

    public void openSettings(View view) {
        Intent myIntent = new Intent(this ,SettingsActivity.class);
        startActivity(myIntent);
    }

    public void openNewAppointment(View view) {
        Intent myIntent = new Intent(this ,NewAppointmentActivity.class);
        startActivity(myIntent);
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
