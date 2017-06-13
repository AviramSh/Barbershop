package com.esh_tech.aviram.barbershop;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.CustomersDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class NewAppointmentActivity extends AppCompatActivity implements View.OnClickListener{

    Customer customerProfile;

    //Calendar
    Calendar appointmentCalendar;
    Button theDate;
    Button theTime;
    int year_x;
    int month_x;
    int day_x;
    int hour_x;
    int minute_x;
    static final int DIALOG_ID = 0;
    static final int DIALOG_ID_TIME = 1;
    //static final int DIALOG_ID = 0;

    //List
    ArrayList<String> turnsList = new ArrayList<String>();
    ArrayAdapter<String> turnsAdapter;
    ListView turnsListView;

    //    Database
    BarbershopDBHandler dbHandler;

    TextView cPhone;
    Appointment newAppointment;
    String testPhone;


    int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private AlarmManager aManager;
    private PendingIntent pIntent;
    private static final int REQUEST_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        this.setTitle(R.string.newAppointment);

        init();
    }

    private void init() {
        //        database
        dbHandler = new BarbershopDBHandler(this);
        cPhone = (TextView)findViewById(R.id.etCustomerPhone);
        theDate = (Button)findViewById(R.id.btDate);
        theTime= (Button)findViewById(R.id.btTime);
        appointmentCalendar = Calendar.getInstance();
        customerProfile = new Customer();
        newAppointment =new Appointment();

        setToday();
        testPhone="";




        try{
            Bundle bundle = getIntent().getExtras();
            customerProfile= dbHandler.getCustomerByID(bundle.getInt(CustomersDBConstants.CUSTOMER_ID));
            if(customerProfile.get_id() !=-1 ) {
                customerProfile = dbHandler.getCustomerByID(getIntent().getExtras().getInt(CustomersDBConstants.CUSTOMER_ID));
                cPhone.setText(customerProfile.getName());
            }
        }catch (NullPointerException e){
            e.getMessage();
        }

    }


    //    Update Appointment List - Database.
    private void populateAppointment() {

//        turnsList = dbHandler.getDayAppointments(year_x,month_x,day_x);
        turnsList.add("Ohad - 9:00");
        turnsList.add("Almog - 9:15");
        turnsList.add("nir - 9:30");
        turnsList.add("Ode - 10:00");


//        Test
        /*appointmentCalendar.set(year_x,month_x,day_x);
        Toast.makeText(this, "day "+appointmentCalendar.get(Calendar.DAY_OF_WEEK), Toast.LENGTH_SHORT).show();*/


        //turnsListView.setAdapter(turnsAdapter);
    }

    private void setToday() {

        year_x = appointmentCalendar.get(Calendar.YEAR );
        month_x = appointmentCalendar.get(Calendar.MONTH);
        day_x = appointmentCalendar.get(Calendar.DAY_OF_MONTH);

        theDate.setText(day_x+"/"+(month_x+1)+"/"+year_x);

        hour_x = appointmentCalendar.get(Calendar.HOUR);
        minute_x= appointmentCalendar.get(Calendar.MINUTE);

        theTime.setText(hour_x+":"+minute_x);

    }

    public void showDialog(View v) {

        switch (v.getId()){
            case R.id.btDate:
                showDialog(DIALOG_ID);
                break;
            case R.id.btTime:
                showDialog(DIALOG_ID_TIME);
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id){

        if(id == DIALOG_ID)
            return new DatePickerDialog(this, dpickerListener,year_x,month_x,day_x);
        else if(id == DIALOG_ID_TIME)
            return new TimePickerDialog(this, tpickerListener,hour_x,minute_x,true);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x =year;
            month_x =month;
            day_x = dayOfMonth;

            theDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);

            Toast.makeText(NewAppointmentActivity.this, ""+year+"/"+(month+1)+"/"+dayOfMonth, Toast.LENGTH_SHORT).show();
            populateAppointment();
        }
    };

    private TimePickerDialog.OnTimeSetListener tpickerListener
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            hour_x=hourOfDay;
            minute_x=minute;

            theTime.setText(hour_x+":"+minute_x);

            Toast.makeText(NewAppointmentActivity.this, hour_x+":"+minute_x, Toast.LENGTH_SHORT).show();
            populateAppointment();
        }
    };




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibSave:
                saveAppointment();
                break;
            case R.id.ibAddCustomer:
                importCustomer();
                break;
            case R.id.ibCancel:
                goMain();
            break;

            default:
                Toast.makeText(this, "Not Initialized", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void goMain() {
        Intent myIntent = new Intent(this,MainActivity.class);
        startActivity(myIntent);
        this.finish();
    }

    public void saveAppointment() {

        appointmentCalendar.set(year_x,month_x,day_x,hour_x,minute_x);
        testPhone+=cPhone.getText().toString();
        customerProfile.setPhone(testPhone);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }
//        }else {
//            SmsManager sms  =SmsManager.getDefault();
//            sms.sendTextMessage(etPhone.getText().toString(),null,message,sentPI,deliveredPI);
//        }

        //                Remainder Sms Handler

        Intent m = new Intent(NewAppointmentActivity.this,AlarmService.class);
        m.putExtra("exPhone", customerProfile.getPhone());
        m.putExtra("exSmS", "Auto Sms Sand");


        pIntent = PendingIntent.getService(getApplicationContext(), 0, m, PendingIntent.FLAG_UPDATE_CURRENT);

        aManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        aManager.set(AlarmManager.RTC_WAKEUP, appointmentCalendar.getTimeInMillis(), pIntent);
//        Toast.makeText(getApplicationContext(), "Sms scheduled! " ,Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Sms scheduled! " ,Toast.LENGTH_SHORT).show();



//        Testing filed and create new ,old of guest customer for the creation of an appointment
        if(customerProfile.getPhone().equals("")){
            Toast.makeText(this, R.string.emptyField, Toast.LENGTH_SHORT).show();
        }else{


            if(dbHandler.getCustomerByPhone(customerProfile.getPhone())!=null)
                customerProfile = dbHandler.getCustomerByPhone(customerProfile.getPhone());

            if(customerProfile.get_id() != -1) {
                newAppointment = new Appointment(appointmentCalendar,customerProfile.get_id());


                if(dbHandler.addAppointment(newAppointment)){
                    //                Remainder Sms Handler
                    if(customerProfile.getRemainder() ==1){
                        Intent i = new Intent(NewAppointmentActivity.this,AlarmService.class);
                        i.putExtra("exPhone", customerProfile.getPhone());
                        i.putExtra("exSmS", "Auto Sms Sand");


                        pIntent = PendingIntent.getService(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

                        aManager = (AlarmManager)getSystemService(ALARM_SERVICE);

                        aManager.set(AlarmManager.RTC_WAKEUP, appointmentCalendar.getTimeInMillis(), pIntent);
                        Toast.makeText(getApplicationContext(), "Sms scheduled! " ,Toast.LENGTH_SHORT).show();

                    }
                    Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                    goMain();
                }else{
                    Toast.makeText(this, R.string.failedToSave, Toast.LENGTH_SHORT).show();
                }
//                cPhone.setText(customerProfile.getName());
            }else{
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
                customerProfile.setPhone(customerProfile.getPhone());
                mBuilder.setTitle(R.string.dialogCreateNewCustomer);

                mBuilder.setNeutralButton(R.string.createNewCustomer, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent = new Intent(NewAppointmentActivity.this,NewCustomerActivity.class);
                        myIntent.putExtra(CustomersDBConstants.CUSTOMER_PHONE,customerProfile.getPhone());
                        startActivity(myIntent);
                        NewAppointmentActivity.this.finish();
                    }
                });


                mBuilder.setNegativeButton(R.string.guest, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        customerProfile.setName(getResources().getString(R.string.guest));
                        customerProfile.setPhone(customerProfile.getPhone());
                        if(dbHandler.addCustomer(customerProfile)) {
                            customerProfile = dbHandler.getCustomerByPhone(customerProfile.getPhone());
                            newAppointment = new Appointment(appointmentCalendar, customerProfile.get_id());

                            if (dbHandler.addAppointment(newAppointment)) {
                                Toast.makeText(NewAppointmentActivity.this, R.string.saved, Toast.LENGTH_LONG).show();
                                goMain();
                                if(dbHandler.addAppointment(newAppointment)){
                                    Toast.makeText(NewAppointmentActivity.this, R.string.saved, Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(NewAppointmentActivity.this, R.string.failedToSave, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }else{
                            Toast.makeText(NewAppointmentActivity.this, R.string.failedToSave, Toast.LENGTH_LONG).show();
                        }
                    }
                });


                AlertDialog dialog = mBuilder.create();
                dialog.show();

            }
        }

    }

    //    **Need to import customer name and phone number from database.
    public void importCustomer() {
        // TODO Auto-generated method stub
        Uri uri = Uri.parse("content://contacts");
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        intent.setType(Phone.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    //Choose phone in contact and set edit text
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, i);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = i.getData();
                String[] projection = { Phone.NUMBER, Phone.DISPLAY_NAME };

                Cursor cursor = getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);

                cPhone.setText(number);
            }
        }
    }

/*

    class MyCustomersAdapter extends ArrayAdapter<Customer>{

        public MyCustomersAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Customer> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            Customer customer = getItem(position);

            if (convertView == null){
                Log.e("Test get view","inside if with position"+position);
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_contact_row,parent,false);
            }

            TextView tvName = (TextView) convertView.findViewById(R.id.customerNameET);
            TextView tvLastname = (TextView) convertView.findViewById(R.id.customerNameET);
            TextView tvPhone = (TextView)convertView.findViewById(R.id.customerNameET);
            ImageView customerIcon = (ImageView)convertView.findViewById(R.id.customerIconIv);


            //Data
            tvName.setText(customer.getName());
            tvPhone.setText(customer.getPhone());

            if(customer.isGender())customerIcon.setImageResource(R.drawable.usermale48);
            else customerIcon.setImageResource(R.drawable.userfemale48);

            return convertView;
        }
    }
*/

}
