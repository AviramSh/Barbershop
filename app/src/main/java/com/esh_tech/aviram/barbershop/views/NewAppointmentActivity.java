package com.esh_tech.aviram.barbershop.views;

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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.BundleConstants;
import com.esh_tech.aviram.barbershop.Constants.CustomersDBConstants;
import com.esh_tech.aviram.barbershop.Constants.LogConstants;
import com.esh_tech.aviram.barbershop.Constants.SharedPreferencesConstants;
import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.Utils.DateUtils;
import com.esh_tech.aviram.barbershop.data.AlarmService;
import com.esh_tech.aviram.barbershop.data.Appointment;
import com.esh_tech.aviram.barbershop.data.Customer;
import com.esh_tech.aviram.barbershop.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.USER_FEMALE_HAIRCUT_PRICE;
import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.USER_FEMALE_HAIRCUT_TIME;
import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.USER_MALE_HAIRCUT_PRICE;
import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.USER_MALE_HAIRCUT_TIME;

@RequiresApi(api = Build.VERSION_CODES.N)
public class NewAppointmentActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "MY_TAG";
    Customer customerProfile;
    ArrayList<Customer> customersList;
    ArrayList<String> customersNames;

//    SharedPreferences
    SharedPreferences settings;


    //Calendar
    Calendar appointmentCalendar;

    Button theDate;
    Button theTime;

    static final int DIALOG_ID = 0;
    static final int DIALOG_ID_TIME = 1;

//    Logs
//    private static final String TAG="";
    private static final String APPOINTMENT_HANDLER="APPOINTMENT_HANDLER";
    private static final String CUSTOMER_HANDLER="CUSTOMER_HANDLER";
    private static final String DATE_HANDLER="DATE_HANDLER";


    //List
    ArrayList<String> turnsList = new ArrayList<String>();
    ArrayAdapter<String> turnsAdapter;
    ListView turnsListView;

    //    Database
    BarbershopDBHandler dbHandler;

    AutoCompleteTextView customerNameSearch;
    Appointment newAppointment;


    private AlarmManager aManager;
    private PendingIntent pIntent;
    int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final int REQUEST_CODE = 2;
    private static final int REQUEST_CODE_GET_USER = 3;


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
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        setAutoComplete();
        theDate = (Button)findViewById(R.id.btDate);
        theTime= (Button)findViewById(R.id.btTime);

        appointmentCalendar = Calendar.getInstance();
        appointmentCalendar.add(Calendar.DAY_OF_MONTH,1);

        customerProfile = new Customer();
        customerProfile.setName(getResources().getString(R.string.guest));

        newAppointment =new Appointment();
        newAppointment.setHaircutTime(settings.getInt(USER_MALE_HAIRCUT_TIME,35));
        newAppointment.setHaircutPrice(settings.getInt(USER_MALE_HAIRCUT_PRICE,35));

        setToday();

        try{
            Bundle bundle = getIntent().getExtras();
            customerProfile= dbHandler.getCustomerByID(bundle.getInt(CustomersDBConstants.CUSTOMER_ID));
            if(customerProfile.get_id() !=-1 ) {
                customerProfile = dbHandler.getCustomerByID(getIntent().getExtras().getInt(CustomersDBConstants.CUSTOMER_ID));
                customerNameSearch.setText(customerProfile.getName());
            }
        }catch (NullPointerException e){
            e.getMessage();
            Log.e(CUSTOMER_HANDLER,"Null pointer , No customer Id on Intent Extras");
        }
        customerNameSearch.setText(customerProfile.getName());
    }

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
                this.finish();
                break;

            default:
                Toast.makeText(this, "Not Initialized", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void setAutoComplete() {
        customerNameSearch = (AutoCompleteTextView)findViewById(R.id.acetCustomerName);
        customerNameSearch.setText("");
        customersList = dbHandler.getAllCustomers();
        customersNames = new ArrayList<String>();
        String[] myNames= {null};
        int i = 0;

        for (Customer index :
                customersList) {
            if (index.get_id() != 1){
                customersNames.add(index.getName()+"\n"+index.getPhone());
            }
        }

        ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.select_dialog_item,customersNames);
        customerNameSearch.setThreshold(1);
        customerNameSearch.setAdapter(adapter);
        customerNameSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                customerProfile = dbHandler.getCustomerByPhone(customerNameSearch.getText().toString().split("\n")[1]);
                customerNameSearch.setText(customerProfile.getName());


            }
        });
    }

    private void setToday() {


        if(appointmentCalendar.before(Calendar.getInstance())){

            appointmentCalendar.setTime(Calendar.getInstance().getTime());
            appointmentCalendar.add(Calendar.MINUTE,5);
            setTime(appointmentCalendar);

        }else{
            setTime(appointmentCalendar);
        }


        String[] workTime = getOpenDaysAndHours(appointmentCalendar.get(Calendar.DAY_OF_WEEK));
        for (int i=0;workTime == null || workTime[0].equals("")|| workTime[0].equals("-1");i++){
            appointmentCalendar.add(Calendar.DAY_OF_MONTH,1);
            workTime = getOpenDaysAndHours(appointmentCalendar.get(Calendar.DAY_OF_WEEK));
            if(i==15) {
                Log.e(APPOINTMENT_HANDLER, "SetToday() error in finding an appointment date..for is to long.");
                break;
            }
        }


        theDate.setText(DateUtils.getDateDaySDF(appointmentCalendar));
        theTime.setText(DateUtils.getTimeSDF(appointmentCalendar.getTime()));

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

            Calendar cTestDate = Calendar.getInstance();
            cTestDate.set(year,month,dayOfMonth);

//            if(cTestDate.before(Calendar.getInstance())){
            if(!(cTestDate.get(Calendar.YEAR)>=Calendar.getInstance().get(Calendar.YEAR)&&
                    cTestDate.get(Calendar.MONTH)>=Calendar.getInstance().get(Calendar.MONTH)&&
                    cTestDate.get(Calendar.DAY_OF_MONTH)>=Calendar.getInstance().get(Calendar.DAY_OF_MONTH))){

                appointmentCalendar.setTime(Calendar.getInstance().getTime());
                setToday();
//                /*SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.dateDayNameFormat, Locale.getDefault());
//                String newFormat = formatter.format(appointmentCalendar.getTime());
//
//                theDate.setText(newFormat);*/
            }else{

                String[] str = getOpenDaysAndHours(cTestDate.get(Calendar.DAY_OF_WEEK));
                if(str==null){
                    Log.d(APPOINTMENT_HANDLER,"getOpenDaysAndHours() return null.");
                }else{

                    if(str[0].equals("-1")|| str[0].equals("")){
                        Toast.makeText(NewAppointmentActivity.this, DateUtils.getTheDayName(cTestDate.getTime())+
                                " "+getResources().getString(R.string.is_a_free_day), Toast.LENGTH_SHORT).show();
                        Log.d(APPOINTMENT_HANDLER,"getOpenDaysAndHours() return Illegal date : "+str[0]);
                    }else{
//                    appointmentCalendar.set(year,month,dayOfMonth);
                        appointmentCalendar.setTime(cTestDate.getTime());
                        setToday();
                    }
                }

            }





        }
    };

//    private String testPicDate(Calendar cTestDate) {
//
//        switch (cTestDate.get(Calendar.DAY_OF_WEEK)){
//
//            case Calendar.SUNDAY:
//                return settings.getString(SharedPreferencesConstants.SUNDAY_TIME_OPEN,"")
//                break;
//            case Calendar.MONDAY:
//
//                break;
//            case Calendar.TUESDAY:
//
//                break;
//            case Calendar.WEDNESDAY:
//
//                break;
//            case Calendar.THURSDAY:
//
//                break;
//            case Calendar.FRIDAY:
//
//                break;
//            case Calendar.SATURDAY:
//
//                break;
//
//
//
//
//            default:
//                Toast.makeText(this, R.string.not_initialized_yet, Toast.LENGTH_SHORT).show();
//                break;
//        }
//
//
//        return null;
//    }


    private TimePickerDialog.OnTimeSetListener tpickerListener
            = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            Calendar cTest = Calendar.getInstance();
            cTest.setTime(appointmentCalendar.getTime());

            cTest.set(Calendar.HOUR_OF_DAY,hourOfDay);
            cTest.set(Calendar.MINUTE,minute);

            if (cTest.getTimeInMillis()<Calendar.getInstance().getTimeInMillis())
            {
                Log.d(TAG,"Date not good" );
            }else
            {
                appointmentCalendar.set(Calendar.HOUR_OF_DAY ,hourOfDay);
                appointmentCalendar.set(Calendar.MINUTE,minute);
            }
////            EditText et = (EditText) findViewById(R.id.acetCustomerName);
////            et.setHint("");
//
//      /*Calendar cOpen = Calendar.getInstance();
//        Calendar cClose = Calendar.getInstance();
//
//        String[] separatedOpenTime = openTime.split(":");
//
//        String[] separatedCloseTime = closeTime.split(":");
//
//        cOpen.set(Calendar.HOUR_OF_DAY,Integer.parseInt(separatedOpenTime[0]));
//        cOpen.set(Calendar.MINUTE,Integer.parseInt(separatedOpenTime[1]));
//
//        cClose.set(Calendar.HOUR_OF_DAY,Integer.parseInt(separatedCloseTime[0]));
//        cClose.set(Calendar.MINUTE,Integer.parseInt(separatedCloseTime[1]));
//
//
//        Toast.makeText(this, DateUtils.getFullSDF(cOpen)+" - "+DateUtils.getFullSDF(cClose), Toast.LENGTH_SHORT).show();*/
//
//
//            /*String[] separatedTime = getOpenDaysAndHours().split(":");
//
//            if(separatedTime !=null ){
//                if(separatedTime[0].equals("")){
//
//                }
//            }*/
//
////
////            if(settings.getInt(SharedPreferencesConstants.MONDAY_TIME_CLOSE,0) > hourOfDay
////                    ||settings.getInt(SharedPreferencesConstants.MONDAY_TIME_CLOSE,0) == hourOfDay &&
////                    m1.getHours() > minute) {
////                appointmentCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
////                appointmentCalendar.set(Calendar.MINUTE, minute);
////            }

            setToday();
//            setTime(appointmentCalendar);

//            hour_x=hourOfDay;
//            minute_x=minute;

        }
    };

    private String[] getOpenDaysAndHours(int day) {

        switch (day){
            case Calendar.SUNDAY:
                Log.d(DATE_HANDLER,"Input Day "+day+" found .");
                return settings.getString(SharedPreferencesConstants.SUNDAY_TIME_OPEN,"-1").split(":");

            case Calendar.MONDAY:
                Log.d(DATE_HANDLER,"Input Day "+day+" found .");
                return settings.getString(SharedPreferencesConstants.MONDAY_TIME_OPEN,"-1").split(":");

            case Calendar.TUESDAY:
                Log.d(DATE_HANDLER,"Input Day "+day+" found .");
                return settings.getString(SharedPreferencesConstants.TUESDAY_TIME_OPEN,"-1").split(":");

            case Calendar.WEDNESDAY:
                Log.d(DATE_HANDLER,"Input Day "+day+" found .");
                return settings.getString(SharedPreferencesConstants.WEDNESDAY_TIME_OPEN,"-1").split(":");

            case Calendar.THURSDAY:
                Log.d(DATE_HANDLER,"Input Day "+day+" found .");
                return settings.getString(SharedPreferencesConstants.THURSDAY_TIME_OPEN,"-1").split(":");

            case Calendar.FRIDAY:
                Log.d(DATE_HANDLER,"Input Day "+day+" found .");
                return settings.getString(SharedPreferencesConstants.FRIDAY_TIME_OPEN,"-1").split(":");

            case Calendar.SATURDAY:
                Log.d(DATE_HANDLER,"Input Day "+day+" found .");
                return settings.getString(SharedPreferencesConstants.SATURDAY_TIME_OPEN,"-1").split(":");

            default:
                Log.e(DATE_HANDLER,"Input Error Day "+day+"found .");
                return null;

        }
    }

    private void setTime(Calendar appointmentCalendar) {

        switch (appointmentCalendar.get(Calendar.MINUTE)%10){
            case 1:
                appointmentCalendar.add(Calendar.MINUTE,-1);
                break;
            case 2:
                appointmentCalendar.add(Calendar.MINUTE,-2);
                break;
            case 3:
                appointmentCalendar.add(Calendar.MINUTE,2);
                break;
            case 4:
                appointmentCalendar.add(Calendar.MINUTE,1);
                break;
            case 6:
                appointmentCalendar.add(Calendar.MINUTE,-1);
                break;
            case 7:
                appointmentCalendar.add(Calendar.MINUTE,-2);
                break;
            case 8:
                appointmentCalendar.add(Calendar.MINUTE,2);
                break;
            case 9:
                appointmentCalendar.add(Calendar.MINUTE,1);
                break;
        }
    }


    public void saveAppointment() {

//        Toast.makeText(this, ""+customerProfile.get_id(), Toast.LENGTH_SHORT).show();
//         Toast.makeText(this, ""+customerProfile.getName(), Toast.LENGTH_SHORT).show();

        if(customerProfile.get_id()!=-1 || customerProfile.get_id()==1)
            registerHandler();
        else if(dbHandler.getCustomerByName(customerNameSearch.getText().toString())!= null)
        {
            customerProfile = dbHandler.getCustomerByName(customerNameSearch.getText().toString());
        }else
            if(dbHandler.getCustomerByPhone(customerNameSearch.getText().toString())!= null)
            {
                customerProfile =dbHandler.getCustomerByPhone(customerNameSearch.getText().toString());
            }

        if(customerProfile.get_id() != -1 )
        {
            registerHandler();
        }
        else
        {
            guestHandler();
        }


    }

    private void registerHandler() {

//                newAppointment = new Appointment(appointmentCalendar,customerProfile.get_id());
        newAppointment.setDateAndTime(appointmentCalendar);
        newAppointment.setCustomerID(customerProfile.get_id());

        if(customerProfile.getGender() == 0){
            newAppointment.setHaircutTime(settings.getInt(USER_FEMALE_HAIRCUT_TIME,45));
            newAppointment.setHaircutPrice(settings.getInt(USER_FEMALE_HAIRCUT_PRICE,45));
        }



        String newDateFormat = DateUtils.getFullSDF(appointmentCalendar.getTime());
        String newDateAndTimeFormat = DateUtils.getFullSDF(appointmentCalendar.getTime());

        newAppointment.setDateAndTime(appointmentCalendar);


//                TODO Schedule customer test with DB
        if(!dbHandler.isCustomerSchedule(newAppointment,newDateFormat)&&
                dbHandler.testIfAppointmentAvailable(this,newAppointment)){

            if (dbHandler.addAppointment(newAppointment)) {
                //                Remainder Sms Handler
                if (customerProfile.getRemainder() == 1) {
                    setRemainder();
                    Toast.makeText(this, R.string.remainder_saved, Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                this.finish();

            } else {
                Toast.makeText(this, R.string.failedToSave, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void guestHandler() {

        customerProfile.set_id(1);

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

                    newAppointment.setHaircutTime(settings.getInt(USER_MALE_HAIRCUT_TIME,35));
                    newAppointment.setHaircutPrice(settings.getInt(USER_MALE_HAIRCUT_PRICE,35));

                    if (dbHandler.addAppointment(newAppointment)&&
                            dbHandler.testIfAppointmentAvailable(NewAppointmentActivity.this,newAppointment)) {
                        Toast.makeText(NewAppointmentActivity.this, R.string.saved, Toast.LENGTH_LONG).show();
                        NewAppointmentActivity.this.finish();
                    }
                }/*else{
                    Toast.makeText(NewAppointmentActivity.this, R.string.failedToSave, Toast.LENGTH_LONG).show();
                }*/
            }
        });

        AlertDialog dialog = mBuilder.create();
        dialog.show();

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
                    String sendMessage="";

                    if(settings.getBoolean(SharedPreferencesConstants.SYSTEM_DEFAULT_SMS_IS_CHECKED,false)){
                        sendMessage +=getResources().getString(R.string.system_sms_1_add_name)+
                        customerProfile.getName()+
                                getResources().getString(R.string.system_sms_2_add_time)+
                        newAppointment.getDateAndTimeToDisplay()+
                                getResources().getString(R.string.system_sms_3add_business)+
                        settings.getString(SharedPreferencesConstants.BUSINESS_NAME,".");
                    }else{
                        sendMessage +=settings.getString(UserDBConstants.USER_DEFAULT_SMS,"");
                    }

                    m.putExtra(
                            SharedPreferencesConstants.USER_SMS_CONTENT,sendMessage);

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

            Intent getCustomerIntent = new Intent(this , CustomersListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(BundleConstants.GET_CUSTOMER,0);
            getCustomerIntent.putExtras(bundle);

            startActivityForResult(getCustomerIntent, REQUEST_CODE_GET_USER);
    }

    //Choose phone in contact and set edit text
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                String[] projection = { Phone.NUMBER, Phone.DISPLAY_NAME };

                Cursor cursor = getContentResolver().query(uri, projection,
                        null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);

                if(dbHandler.getCustomerByPhone(number)!=null)
                {
                    customerProfile = dbHandler.getCustomerByPhone(number);
                    customerNameSearch.setText(customerProfile.getName());
                }else{
                    customerNameSearch.setText(number);
                }

            }
        }
        if(requestCode == REQUEST_CODE_GET_USER)
            if (resultCode == RESULT_OK ){
                Bundle bundle = data.getExtras();
                bundle.getInt(BundleConstants.GET_CUSTOMER);
                customerProfile = dbHandler.getCustomerByID(bundle.getInt(BundleConstants.GET_CUSTOMER));
                customerNameSearch.setText(customerProfile.getName());

            }
    }


}
