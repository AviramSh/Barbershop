package com.esh_tech.aviram.barbershop.views;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.JobsHandler.MJobScheduler;
import com.esh_tech.aviram.barbershop.Utils.DateUtils;
import com.esh_tech.aviram.barbershop.data.*;
import com.esh_tech.aviram.barbershop.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.USER_AUTO_LOGIN;
import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.USER_FEMALE_HAIRCUT_PRICE;
import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.USER_MALE_HAIRCUT_PRICE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "Programmer";
    //    SharedPreferences
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    ArrayList<Appointment> allAppointments =new ArrayList<Appointment>();
    ListView lsNextAppointment;
    MyAppointmentsAdapter appointmentAdapter;

    //    Database
    BarbershopDBHandler dbHandler;

    //    SMS job test
    private static final int JOB_ID = 101;
    private JobScheduler jobScheduler;
    private JobInfo jobInfo;

//    PERMISSIONS
    private final static int MY_PERMISSIONS_REQUEST_SEND_SMS = 28;
    private final static int MY_PERMISSIONS_REQUEST_IMPORT_CONTACT= 29;

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
                Toast.makeText(this, getResources().getString(R.string.not_initialized_yet), Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


    private void init() {
        this.setTitle("");

        settings = PreferenceManager.getDefaultSharedPreferences(this);

//        PERMISSION
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_IMPORT_CONTACT);
        }


//        SMS Tester
        ComponentName componentName = new ComponentName(this,MJobScheduler.class);


        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID,componentName);

        builder.setPeriodic(5000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);

        jobInfo = builder.build();
        jobScheduler = (JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);

        if (jobScheduler!=null&&jobScheduler.schedule(jobInfo)==JobScheduler.RESULT_SUCCESS ) {
            Log.d(TAG,"SMS tester scheduled.");
        }else{
            Log.d(TAG,"SMS tester wasn't scheduled.");
        }


        //        Database
        dbHandler = new BarbershopDBHandler(this);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        Connect list view
        lsNextAppointment = (ListView) findViewById(R.id.lvNextAppointment);

//        fill components
        populateAppointment();
//        Toast.makeText(this, " "+dbHandler.getAllCustomers().size()+"id_"+dbHandler.getAllCustomers().get(0).get_id(), Toast.LENGTH_SHORT).show();
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
            case R.id.btCustomersService:
//                myIntent = new Intent(this ,CustomersServiceActivity.class);
                myIntent = new Intent(this ,AlbumActivity.class);
                startActivity(myIntent);
                break;

            default:
                Toast.makeText(this, "Not Initialized yet", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateAppointment();
        appointmentAdapter= new MyAppointmentsAdapter(this,R.layout.custom_appointment_row,allAppointments);

        lsNextAppointment.setAdapter(appointmentAdapter);

    }

    public void logout() {

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_menu_close_clear_cancel)
                .setTitle(R.string.logout)
                .setMessage(R.string.are_you_sure_you_whant_to_logout)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor = settings.edit();
                        editor.putBoolean(USER_AUTO_LOGIN,false);
                        editor.apply();

                        Intent myIntent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivity(myIntent);
                        finish();
                    }

                })
                .setNegativeButton(R.string.no, null)
                .show();

    }

    //    Handling the Appointment list view
    private void appointmentHandler(final int position) {

//        Customer customer =dbHandler.getCustomerByID(allAppointments.get(position).getCustomerID());
//        Toast.makeText(this, customer.getName()+"", Toast.LENGTH_SHORT).show();


//        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
//
//        mBuilder.setTitle(R.string.manageAppointment);
//        mBuilder.setMessage(R.string.theCustomerGetAnHaircut);
//
//        mBuilder.setNeutralButton(R.string.gatHaircut, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(MainActivity.this, R.string.gatHaircut, Toast.LENGTH_LONG).show();
//
//                Appointment appointment = allAppointments.get(position);
//
//
//                if(appointment != null) {
////                    purchase.setAppointmentID(appointment.get_id());
////                    purchase.setCustomerID(appointment.getCustomerID());
////                    purchase.setPrice(appointment.getHaircutPrice());//
////                    if (customer != null) {
////                        settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
////
////                        if(customer.getGender()==1){
////                            purchase.setPrice(settings.getInt(UserDBConstants.USER_MALE_HAIRCUT_PRICE,0));
////
////                        }else{
////                            purchase.setPrice(settings.getInt(UserDBConstants.USER_FEMALE_HAIRCUT_PRICE,0));
////                        }
////                        if(customer.getName().equals(getResources().getString(R.string.guest))){
////                            dbHandler.deleteCustomerById(customer.get_id());
////                            Toast.makeText(MainActivity.this, "Delete :"+R.string.guest, Toast.LENGTH_SHORT).show();
////                        }
////                    }
//                    //                Need to update database
//                    appointment.setTackAnHaircut(1);
//
//                    Purchase purchase = new Purchase(appointment);
//                    if(dbHandler.addPurchase(purchase)){
//                        Log.d(TAG,"Purchase saved:");
//                    }else Log.d(TAG,"Purchase Unsaved:");
//
//                }
//
//
//                if(dbHandler.updateAppointment(appointment)){
////                    Toast.makeText(MainActivity.this, "Record updated", Toast.LENGTH_SHORT).show();
//                    assert appointment != null;
//                    Log.d(TAG,"Appointment ID:"+appointment.get_id()+" - Record updated");
//                }
//
//                allAppointments.remove(position);
//                appointmentAdapter.notifyDataSetChanged();
//            }
//        });
//
//
//        mBuilder.setNegativeButton(R.string.DidntGetHaircut, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                Toast.makeText(MainActivity.this, R.string.DidntGetHaircut, Toast.LENGTH_LONG).show();
//
//                allAppointments.remove(position);
//                appointmentAdapter.notifyDataSetChanged();
//            }
//        });
//
//
//
//
//        AlertDialog dialog = mBuilder.create();
//        dialog.show();


        final Customer customer =dbHandler.getCustomerByID(allAppointments.get(position).getCustomerID());
        final CharSequence[] items = {" Got haircut"," Payed"};
        final boolean[] haircutAndFee = {false,false};


        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(customer.getName()+" "+ getResources().getString(R.string.appointment))
                .setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
//                        if (isChecked) {
//                            haircutAndFee[indexSelected]=isChecked;
//                            Toast.makeText(MainActivity.this, "Checked "+indexSelected, Toast.LENGTH_SHORT).show();
//                        } else{
//                            Toast.makeText(MainActivity.this, "Unchecked "+indexSelected, Toast.LENGTH_SHORT).show();
//                        }
                        haircutAndFee[indexSelected]=isChecked;

                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Appointment appointment = allAppointments.get(position);


                        if(appointment != null) {
                            //                    purchase.setAppointmentID(appointment.get_id());
                            //                    purchase.setCustomerID(appointment.getCustomerID());
                            //                    purchase.setPrice(appointment.getHaircutPrice());//
                            //                    if (customer != null) {
                            //                        settings = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                            //
                            //                        if(customer.getGender()==1){
                            //                            purchase.setPrice(settings.getInt(UserDBConstants.USER_MALE_HAIRCUT_PRICE,0));
                            //
                            //                        }else{
                            //                            purchase.setPrice(settings.getInt(UserDBConstants.USER_FEMALE_HAIRCUT_PRICE,0));
                            //                        }
                            //                        if(customer.getName().equals(getResources().getString(R.string.guest))){
                            //                            dbHandler.deleteCustomerById(customer.get_id());
                            //                            Toast.makeText(MainActivity.this, "Delete :"+R.string.guest, Toast.LENGTH_SHORT).show();
                            //                        }
                            //                    }
                            //                Need to update database
                            if (haircutAndFee[0]){
                                appointment.setTackAnHaircut(1);
                            }

                            if(!haircutAndFee[1]) {
                                if(customer.getGender()==1) {
                                    double fee = customer.getBill() - settings.getInt(USER_MALE_HAIRCUT_PRICE, 0);
                                    customer.setBill(fee);
                                }else{
                                    double fee = customer.getBill() - settings.getInt(USER_FEMALE_HAIRCUT_PRICE, 0);
                                    customer.setBill(fee);
                                }


                                if(dbHandler.updateCustomer(customer))
                                    Log.d(TAG,"Failed to update customer bill");
                            }

                            Purchase purchase = new Purchase(appointment);
                            if(dbHandler.addPurchase(purchase)){
                                Log.d(TAG,"Purchase saved:");
                            }else Log.d(TAG,"Purchase Unsaved:");

                        }


                        if(dbHandler.updateAppointment(appointment)){
        //                    Toast.makeText(MainActivity.this, "Record updated", Toast.LENGTH_SHORT).show();
                            assert appointment != null;
                            Log.d(TAG,"Appointment ID:"+appointment.get_id()+" - Record updated");
                        }

                        allAppointments.remove(position);
                        appointmentAdapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                    }
                }).create();
        dialog.show();
    }

    private void populateAppointment() {

        allAppointments = new ArrayList<Appointment>();
        ArrayList<Customer> getCustomers = dbHandler.getAllCustomers();

        for (Customer customer :
                getCustomers) {
            Log.d(TAG,"Add Customer "+customer);
        }
//        SimpleDateFormat sdf = new SimpleDateFormat(
//                "dd/MM/yyyy", Locale.getDefault());
//        String dateForDisplay = sdf.format(Calendar.getInstance().getTime());

        allAppointments = dbHandler.getWaitingListAppointments(Calendar.getInstance());

//minutes, hour, day,month, year, customerName, customerID
//        allAppointments.add(new Appointment(15,8,4,5,1987,"Avi",3));
//        allAppointments.add(new Appointment(30,8,4,5,1987,"Tal",3));
//        allAppointments.add(new Appointment(50,8,4,5,1987,"Michal",3));
//        allAppointments.add(new Appointment(13,8,4,5,1987,"avi",3));
//        allAppointments.add(new Appointment(13,8,4,5,1987,"avi",3));
//        allAppointments.add(new Appointment(13,8,4,5,1987,"avi",3));
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


            Log.d(TAG,""+dbHandler.getCustomerByID(appointment.getCustomerID()));
//            Data

//            Toast.makeText(MainActivity.this, ""+appointment.getCustomerID(), Toast.LENGTH_SHORT).show();
            if (appointment != null) {
                tvTime.setText(
                        DateUtils.getOnlyTime(appointment.getcDateAndTime()));
                if(appointment.getCustomerID()==1){
                    tvName.setText(getResources().getString(R.string.guest));
                }
                else {
                    tvName.setText(dbHandler.getCustomerByID(appointment.getCustomerID()).getName());
                }
                tvName.setTextColor(getResources().getColor(android.R.color.holo_green_light));


                if(new DateUtils().compareDates(
                        appointment.getcDateAndTime(),
                        Calendar.getInstance())==1) {
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
