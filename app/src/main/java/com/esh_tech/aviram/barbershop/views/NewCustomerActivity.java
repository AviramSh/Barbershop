package com.esh_tech.aviram.barbershop.views;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.CustomersDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.R;
import com.esh_tech.aviram.barbershop.Utils.DateUtils;
import com.esh_tech.aviram.barbershop.data.*;

import java.util.Calendar;


public class NewCustomerActivity extends AppCompatActivity implements View.OnClickListener{

    /*
    private boolean gender;
    private boolean remainder;
    private Bitmap CustomerPhoto;*/

    Customer customerProfile;
    Calendar newCalendar;
    Button theDate;

    EditText customerName;
    EditText customerEmail;
    EditText customerCredit;
    CheckBox customerRemainder;
    EditText customerPhone;
    RadioButton rbMale;
    RadioButton rbFemale;
    RadioGroup rg;

    boolean gender =true;
    static final int DIALOG_ID = 0;
    static final int DIALOG_ID_TIME = 1;
//    RadioGroup rg;

//    DB
    BarbershopDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        init();
    }

    private void init() {
        dbHandler = new BarbershopDBHandler(this);
        rg = (RadioGroup)findViewById(R.id.rgGender);
        customerRemainder = (CheckBox)findViewById(R.id.cbReminder);
        customerName = (EditText)findViewById(R.id.etCustomerName);

        customerPhone =(EditText)findViewById(R.id.acetCustomerName);
        customerCredit = (EditText)findViewById(R.id.etCustomerCredit);
        customerEmail = (EditText)findViewById(R.id.etCustomerEmail);
        rbMale = (RadioButton)findViewById(R.id.rbMan);
        rbFemale = (RadioButton)findViewById(R.id.rbWoman);

        newCalendar = Calendar.getInstance();
        theDate = (Button)findViewById(R.id.btTheDate);

        theDate.setText(DateUtils.getOnlyDate(newCalendar));
//        newCalendar.set(1900,1,1);

        customerProfile = new Customer();



//        if customer ID isn't (-1) it's means that the customer is already existed so his for edit


        try{
            Bundle bundle = getIntent().getExtras();
            customerProfile= dbHandler.getCustomerByID(bundle.getInt(CustomersDBConstants.CUSTOMER_ID));
            if(customerProfile.get_id() !=-1 ) {
                customerProfile = dbHandler.getCustomerByID(getIntent().getExtras().getInt(CustomersDBConstants.CUSTOMER_ID));
                setEditCustomer(customerProfile);
            }
        }catch (NullPointerException e){
            e.getMessage();
        }

        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle.getString(CustomersDBConstants.CUSTOMER_PHONE) != null) {
                customerPhone.setText(bundle.getString(CustomersDBConstants.CUSTOMER_PHONE));
            }

        }catch (NullPointerException e){
            e.getMessage();
        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btTheDate:
                showDialog(v);
                break;
            case R.id.btSave:
                addCustomer();
                break;
            case R.id.btClose:
                Intent myIntent = new Intent(this,MainActivity.class);
                startActivity(myIntent);
                this.finish();
                break;


            default:
                Toast.makeText(this, "Not Initialized yet", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    private void setEditCustomer(Customer customerProfile) {

        this.setTitle(R.string.customerEditActivity);
        customerName.setText(customerProfile.getName());
        customerPhone.setText(customerProfile.getPhone());
        customerCredit.setText(String.valueOf(customerProfile.getBill()));
        customerEmail.setText(customerProfile.getEmail());
        theDate.setText(customerProfile.getBirthday());

        Toast.makeText(this, theDate.getText().toString() +"", Toast.LENGTH_SHORT).show();

        if(customerProfile.getRemainder()==1)
            customerRemainder.setChecked(true);
        else customerRemainder.setChecked(false);

        if(customerProfile.getGender()==1) {
            rbMale.setChecked(true);
            rbFemale.setChecked(false);
        }
        else {rbMale.setChecked(false);
            rbFemale.setChecked(true);
        }
    }


    public void addCustomer() {


        String testString ="Error :";


        customerProfile.setName(customerName.getText().toString());
        customerProfile.setPhone(customerPhone.getText().toString());
        customerProfile.setBill(Double.parseDouble(customerCredit.getText().toString()));
        customerProfile.setEmail(customerEmail.getText().toString());


//        Remainder
        if(customerRemainder.isChecked())
            customerProfile.setRemainder(1);//testString += " Yes for SMS";}
        else customerProfile.setRemainder(0);//testString += " No for SMS";}

//        Gender
        if(rg.getCheckedRadioButtonId() == R.id.rbWoman)customerProfile.setGender(0);//testString += " Women";}
        else customerProfile.setGender(1);//testString+= " men";}


        if(newCalendar.get(Calendar.YEAR) <= (Calendar.getInstance().get(Calendar.YEAR)-18)){
            customerProfile.setBirthday(DateUtils.getOnlyDate(newCalendar));
//            Toast.makeText(this, "Birth day saved"+ new DateUtils().getOnlyDateSDF(newCalendar), Toast.LENGTH_SHORT).show();
        }

        if(customerName.getText().toString().length() < 2)
            //testString +="\nUser name to short.";
        if(customerProfile.getPhone().length() < 4)
            testString +="\n+"+R.string.phoneToShort;


        if (!testString.equals("Error :")) {
            Toast.makeText(this, testString, Toast.LENGTH_LONG).show();
        }else{

            if(dbHandler.updateCustomer(customerProfile)){
                Toast.makeText(this, R.string.saved, Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(this, CustomersListActivity.class);
                startActivity(myIntent);
                this.finish();
            }else{
                Toast.makeText(this, R.string.failedToSave, Toast.LENGTH_LONG).show();
            }
        }



    }


    public void showDialog(View v) {

        switch (v.getId()) {
            case R.id.btTheDate:
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
            theDate.setText(DateUtils.getOnlyDate(newCalendar));
//            populateAppointment();
//            restDate(0);
        }
    };

}

