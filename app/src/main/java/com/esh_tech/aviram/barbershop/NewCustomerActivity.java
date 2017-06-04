package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

public class NewCustomerActivity extends AppCompatActivity {

    /*
    private boolean gender;
    private boolean remainder;
    private Bitmap CustomerPhoto;*/

    EditText customerName;
    EditText customerLastName;
    EditText customerEmail;
    EditText customerCredit;
    CheckBox customerRemainder;
    EditText customerPhone;
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
    }

    public void addCustomer(View view) {

        Customer c = new Customer();
        String testString ="Error :";


        c.setName(customerName.getText().toString()+" "+customerLastName.getText().toString());
        c.setPhone(customerPhone.getText().toString());
        c.setBill(Integer.parseInt(customerCredit.getText().toString()));
        c.setEmail(customerEmail.getText().toString());

        //dbHandler.addCustomer(c);

//        Remainder
        if(customerRemainder.isChecked())
            c.setRemainder(1);//testString += " Yes for SMS";}
        else c.setRemainder(0);//testString += " No for SMS";}

//        Gender
        if(rg.getCheckedRadioButtonId() == R.id.rbWoman)c.setGender(0);//testString += " Women";}
        else c.setGender(1);//testString+= " men";}


        //testString = c.getName()+" ,"+c.getPhone()+" ,"+c.getBill()+" ,"+c.getEmail();

        if(customerName.getText().toString().length() < 2)
            //testString +="\nUser name to short.";
        if(c.getPhone().length() < 4)
            testString +="\nPhone to short.";


        if (!testString.equals("Error :")) {
            Toast.makeText(this, testString, Toast.LENGTH_LONG).show();
        }else{
            if(dbHandler.addCustomer(c)){
                Toast.makeText(this, R.string.saved, Toast.LENGTH_LONG).show();
                Intent myIntent = new Intent(this, CustomersListActivity.class);
                startActivity(myIntent);
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
