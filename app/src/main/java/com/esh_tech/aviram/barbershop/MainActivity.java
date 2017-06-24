package com.esh_tech.aviram.barbershop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.USER_AUTO_LOGIN;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //    SharedPreferences
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    ArrayList<Appointment> allAppointments =new ArrayList<Appointment>();
    ListView lsNextAppointment;
    MyAppointmentsAdapter appointmentAdapter;

    //    Database
    BarbershopDBHandler dbHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

//    Menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent;
        switch (item.getItemId()){
            case R.id.action_settings:
                myIntent = new Intent(this,SettingsActivity.class);
                startActivity(myIntent);
                break;
            case R.id.action_about:
                myIntent = new Intent(this,AboutActivity.class);
                startActivity(myIntent);
                break;
            case R.id.action_logout:
                logout();
                break;
            default:
                Toast.makeText(this, "Not Initialized yet", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


    private void init() {
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

    public void logout() {
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        editor = settings.edit();
        editor.putBoolean(USER_AUTO_LOGIN,false);
        editor.apply();

        Intent myIntent = new Intent(this,LoginActivity.class);
        startActivity(myIntent);
        this.finish();
    }

    //    Handling the Appointment list view
    private void appointmentHandler(final int position) {


        Toast.makeText(this, dbHandler.getCustomerByID(allAppointments.get(position).getCustomerID())+"", Toast.LENGTH_SHORT).show();


        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        mBuilder.setTitle(R.string.manageAppointment);
        mBuilder.setMessage(R.string.theCustomerGetAnHaircut);

        mBuilder.setNeutralButton(R.string.getHaircut, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, R.string.getHaircut, Toast.LENGTH_LONG).show();

                Purchase purchase = new Purchase();
                Appointment appointment = allAppointments.get(position);


                if(appointment != null) {
                    purchase.setAppointmentID(appointment.get_id());
                    purchase.setCustomerID(appointment.getCustomerID());

                    Customer customer = dbHandler.getCustomerByID(appointment.getCustomerID());


                    if (customer != null) {
                        settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        if(customer.getGender()==1){
                            purchase.setPrice(settings.getInt(UserDBConstants.USER_MALE_HAIRCUT_PRICE,0));

                        }else{
                            purchase.setPrice(settings.getInt(UserDBConstants.USER_FEMALE_HAIRCUT_PRICE,0));
                        }
                    }
                }
                //                Need to update database
                if(dbHandler.addPurchase(purchase)){
                    Log.d("Aviram","Purchase saved:");
                }else Log.d("Aviram","Purchase Unsaved:");

                allAppointments.get(position).setTackAnHaircut(1);
                if(dbHandler.updateAppointment(allAppointments.get(position))){
                    Toast.makeText(MainActivity.this, "Record updated", Toast.LENGTH_SHORT).show();
                }

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
        allAppointments = new ArrayList<Appointment>();

//        SimpleDateFormat sdf = new SimpleDateFormat(
//                "dd/MM/yyyy", Locale.getDefault());
//        String dateForDisplay = sdf.format(Calendar.getInstance().getTime());

        allAppointments = dbHandler.getWaitingListAppointments(new DateHandler().getOnlyDateSDF(Calendar.getInstance()));

//minutes, hour, day,month, year, customerName, customerID
//        allAppointments.add(new Appointment(15,8,4,5,1987,"Avi",3));
//        allAppointments.add(new Appointment(30,8,4,5,1987,"Tal",3));
//        allAppointments.add(new Appointment(50,8,4,5,1987,"Michal",3));
//        allAppointments.add(new Appointment(13,8,4,5,1987,"avi",3));
//        allAppointments.add(new Appointment(13,8,4,5,1987,"avi",3));
//        allAppointments.add(new Appointment(13,8,4,5,1987,"avi",3));
    }

    private void settime() {

    }


    @Override
    public void onClick(View v) {
        Intent myIntent =null;

        switch (v.getId()){
            case R.id.btCustomersList:
                myIntent = new Intent(this ,CustomersListActivity.class);
                startActivity(myIntent);
                break;
            case R.id.btBalance:
                myIntent = new Intent(this ,BalanceActivity.class);
                startActivity(myIntent);
                break;
            case R.id.btStock:
                myIntent = new Intent(this ,StockActivity.class);
                startActivity(myIntent);
                break;

            case R.id.btAppointmentsList:
                myIntent = new Intent(this ,AppointmentListActivity.class);
                startActivity(myIntent);
                break;
            case R.id.btSettings:
                myIntent = new Intent(this ,SettingsActivity.class);
                startActivity(myIntent);
                break;
            case R.id.btNewAppointment:
                myIntent = new Intent(this ,NewAppointmentActivity.class);
                startActivity(myIntent);
                break;

            default:
                Toast.makeText(this, "Not Initialized yet", Toast.LENGTH_SHORT).show();
                break;
        }
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
            if (appointment != null) {
                tvTime.setText(appointment.getTime());
                tvName.setText(dbHandler.getCustomerByID(appointment.getCustomerID()).getName());
                tvName.setTextColor(getResources().getColor(android.R.color.holo_green_light));


                if(new DateHandler().compareDates(
                        appointment.getDateAndTimeToDisplay(),
                        new DateHandler().getFullSDF(Calendar.getInstance()))==1) {
                    tvName.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }
            }



//            tvTime.setText(String.valueOf(appointment.getHour()) +":"+ String.valueOf(appointment.getMinutes()));

//            if(appointment.isGender())customerIcon.setImageResource(R.drawable.usermale48);
//            else customerIcon.setImageResource(R.drawable.userfemale48);

            return convertView;
        }
    }
}
