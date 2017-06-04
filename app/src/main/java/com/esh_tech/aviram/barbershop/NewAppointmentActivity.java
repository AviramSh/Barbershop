package com.esh_tech.aviram.barbershop;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class NewAppointmentActivity extends AppCompatActivity {


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
    //static final int DIALOG_ID = 0;

    //List
    ArrayList<String> turnsList = new ArrayList<String>();
    ArrayAdapter<String> turnsAdapter;
    ListView turnsListView;

    //    Database
    BarbershopDBHandler dbHandler;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        this.setTitle(R.string.newAppointment);

        //        database
        dbHandler = new BarbershopDBHandler(this);

        theDate = (Button)findViewById(R.id.btDate);
        theTime= (Button)findViewById(R.id.btTime);
        setToday();

    }


//    Update Appointment List - Database.
    private void populateAppointment() {

//        turnsList = dbHandler.getDayAppointments(year_x,month_x,day_x);
        turnsList.add("Ohad - 9:00");
        turnsList.add("Almog - 9:15");
        turnsList.add("nir - 9:30");
        turnsList.add("Ode - 10:00");


//        Test
        /*c.set(year_x,month_x,day_x);
        Toast.makeText(this, "day "+c.get(Calendar.DAY_OF_WEEK), Toast.LENGTH_SHORT).show();*/


        //turnsListView.setAdapter(turnsAdapter);
    }

    private void setToday() {

        year_x = c.get(Calendar.YEAR );
        month_x = c.get(Calendar.MONTH);
        day_x = c.get(Calendar.DAY_OF_MONTH);

        theDate.setText(day_x+"/"+(month_x+1)+"/"+year_x);

        hour_x = c.get(Calendar.HOUR);
        minute_x= c.get(Calendar.MINUTE);

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







    public void saveAppointment(View view) {

//        need if
        Appointment newAppointment =new Appointment();
        Customer testCustomer;

        TextView cName = (TextView)findViewById(R.id.etNewCustomerName);
        TextView cPhone = (TextView)findViewById(R.id.etNewCustomerPhone);

        if(cName.getText().toString().isEmpty()||cPhone.getText().toString().isEmpty()){
            Toast.makeText(this, R.string.emptyField, Toast.LENGTH_SHORT).show();
        }else{


//            Integer.parseInt(cPhone.getText().toString())
            testCustomer =dbHandler.getCustomerByPhone(cPhone.getText().toString());

            if(testCustomer != null) {
                c.set(year_x,month_x,day_x,hour_x,minute_x);
                newAppointment = new Appointment(c,testCustomer.get_id());
            }


            if(dbHandler.addAppointment(newAppointment)){
                Toast.makeText(this, R.string.saved, Toast.LENGTH_LONG).show();

                /*Intent myIntent = new Intent(this,MainActivity.class);
                startActivity(myIntent);*/
            }else{
                Toast.makeText(this, R.string.failedToSave, Toast.LENGTH_LONG).show();
            }
        }

    }


//    **Need to import customer name and phone number from database.
    public void importCustomer(View view) {

    }

    public void closeNewAppointment(View view) {

        Intent myIntent = new Intent(this,MainActivity.class);
        startActivity(myIntent);
        this.finish();
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
