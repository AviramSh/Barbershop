package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.CustomersDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

public class NewCustomerActivity extends AppCompatActivity {

    /*
    private boolean gender;
    private boolean remainder;
    private Bitmap CustomerPhoto;*/
    Customer customerProfile;

    EditText customerName;
    EditText customerLastName;
    EditText customerEmail;
    EditText customerCredit;
    CheckBox customerRemainder;
    EditText customerPhone;
    RadioButton rbMale;
    RadioButton rbFemale;
    RadioGroup rg;

    boolean gender =true;
//    RadioGroup rg;

//    DB
    BarbershopDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        init();
    }

    private void init() {
        this.setTitle(R.string.newCustomer);
        dbHandler = new BarbershopDBHandler(this);
        rg = (RadioGroup)findViewById(R.id.rgGender);
        customerRemainder = (CheckBox)findViewById(R.id.cbReminder);
        customerName = (EditText)findViewById(R.id.etCustomerName);
        customerLastName= (EditText)findViewById(R.id.etCustomerLastName);
        customerPhone =(EditText)findViewById(R.id.etCustomerPhone);
        customerCredit = (EditText)findViewById(R.id.etCustomerCredit);
        customerEmail = (EditText)findViewById(R.id.etCustomerEmail);
        rbMale = (RadioButton)findViewById(R.id.rbMan);
        rbFemale = (RadioButton)findViewById(R.id.rbWoman);


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

    private void setEditCustomer(Customer customerProfile) {

        customerName.setText(customerProfile.getName());
        customerPhone.setText(customerProfile.getPhone());
        customerCredit.setText(String.valueOf(customerProfile.getBill()));
        customerEmail.setText(customerProfile.getEmail());

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


    public void addCustomer(View view) {


        String testString ="Error :";


        customerProfile.setName(customerName.getText().toString()+" "+customerLastName.getText().toString());
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


        //testString = appointmentCalendar.getName()+" ,"+appointmentCalendar.getPhone()+" ,"+appointmentCalendar.getBill()+" ,"+appointmentCalendar.getEmail();

        if(customerName.getText().toString().length() < 2)
            //testString +="\nUser name to short.";
        if(customerProfile.getPhone().length() < 4)
            testString +="\n+"+R.string.phoneToShort;


        if (!testString.equals("Error :")) {
            Toast.makeText(this, testString, Toast.LENGTH_LONG).show();
        }else{
            if(dbHandler.addCustomer(customerProfile)){
                Toast.makeText(this, R.string.saved, Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(this, CustomersListActivity.class);
                startActivity(myIntent);
                this.finish();
            }else{
                Toast.makeText(this, R.string.failedToSave, Toast.LENGTH_LONG).show();
            }
        }



    }

    public void closeNewCustomer(View view) {
        Intent myIntent = new Intent(this,MainActivity.class);
        startActivity(myIntent);
        this.finish();

    }
}
