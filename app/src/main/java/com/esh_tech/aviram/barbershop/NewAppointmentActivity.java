package com.esh_tech.aviram.barbershop;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
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
import com.esh_tech.aviram.barbershop.Constants.SharedPreferencesConstants;
import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class NewAppointmentActivity extends AppCompatActivity implements View.OnClickListener{

    Customer customerProfile;

    //    SharedPreferences
    SharedPreferences settings;


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
//        newAppointment.setDateAndTime(appointmentCalendar);

        settings = PreferenceManager.getDefaultSharedPreferences(NewAppointmentActivity.this);


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


    private void setToday() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy \n EEEE", Locale.getDefault());
        String newFormat = formatter.format(appointmentCalendar.getTime());

        year_x = appointmentCalendar.get(Calendar.YEAR );
        month_x = appointmentCalendar.get(Calendar.MONTH);
        day_x = appointmentCalendar.get(Calendar.DAY_OF_MONTH);

        theDate.setText(newFormat);

        formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        newFormat = formatter.format(appointmentCalendar.getTime());


        hour_x = appointmentCalendar.get(Calendar.HOUR_OF_DAY);
        minute_x= appointmentCalendar.get(Calendar.MINUTE);

        theTime.setText(newFormat);
//        theTime.setText(hour_x+":"+minute_x);

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
            return new DatePickerDialog(this, dpickerListener,
                    appointmentCalendar.get(Calendar.YEAR),
                    appointmentCalendar.get(Calendar.MONTH),
                    appointmentCalendar.get(Calendar.DAY_OF_MONTH));
        else if(id == DIALOG_ID_TIME)
            return new TimePickerDialog(this, tpickerListener,
                    appointmentCalendar.get(Calendar.HOUR_OF_DAY),
                    appointmentCalendar.get(Calendar.MINUTE),true);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//            year_x =year;
//            month_x =month;
//            day_x = dayOfMonth;

            appointmentCalendar.set(Calendar.YEAR,year);
            appointmentCalendar.set(Calendar.MONTH,month);
            appointmentCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

            setToday();
//            theDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
        }
    };

    private TimePickerDialog.OnTimeSetListener tpickerListener
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            hour_x=hourOfDay;
//            minute_x=minute;
            appointmentCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
            appointmentCalendar.set(Calendar.MINUTE,minute);
            setToday();
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

//        appointmentCalendar.set(year_x,month_x,day_x,hour_x,minute_x);
        testPhone+=cPhone.getText().toString();
        customerProfile.setPhone(testPhone);

        if(customerProfile.getPhone().equals("")){
            Toast.makeText(this, R.string.emptyField, Toast.LENGTH_SHORT).show();
        }else{


            if(dbHandler.getCustomerByPhone(customerProfile.getPhone())!=null)
                customerProfile = dbHandler.getCustomerByPhone(customerProfile.getPhone());

            if(customerProfile.get_id() != -1) {
//                newAppointment = new Appointment(appointmentCalendar,customerProfile.get_id());
                newAppointment.setDateAndTime(appointmentCalendar);
                newAppointment.setCustomerID(customerProfile.get_id());



                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String newFormat = formatter.format(appointmentCalendar.getTime());

                if(!dbHandler.isCustomerSchedule(newAppointment,newFormat)){

                    if (dbHandler.addAppointment(newAppointment)) {
                        //                Remainder Sms Handler
                        if (customerProfile.getRemainder() == 1) {
                            setRemainder();
                            Toast.makeText(this, "Remainder saved", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                        goMain();

                    } else {
                        Toast.makeText(this, R.string.failedToSave, Toast.LENGTH_SHORT).show();
                    }
//                cPhone.setText(customerProfile.getName());
                }else{
                    Toast.makeText(this, ""+getResources().getString(R.string.alreadyHaveAppointment), Toast.LENGTH_SHORT).show();
                }

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

    private void setRemainder() {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }else {
                Intent m = new Intent(NewAppointmentActivity.this, AlarmService.class);
                m.putExtra(SharedPreferencesConstants.USER_PHONE_SMS, customerProfile.getPhone());
                try {
                    m.putExtra(SharedPreferencesConstants.USER_SMS_CONTENT, settings.getString(UserDBConstants.USER_DEFAULT_SMS, getResources().getString(R.string.defaultSms)));
                    pIntent = PendingIntent.getService(getApplicationContext(), 0, m, PendingIntent.FLAG_UPDATE_CURRENT);

                    aManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                    aManager.set(AlarmManager.RTC_WAKEUP, appointmentCalendar.getTimeInMillis(), pIntent);
                    Toast.makeText(this, R.string.smsScheduled, Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    e.getMessage();
                    Toast.makeText(this, R.string.smsNotScheduled, Toast.LENGTH_SHORT).show();
                }
            }
    }


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
