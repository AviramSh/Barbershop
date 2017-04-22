package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Codes.Customer;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

public class NewCustomerActivity extends AppCompatActivity {

    /*



    private boolean remainder;
    private Bitmap CustomerPhoto;*/



    EditText customerName;
    EditText customerLastName;
    EditText customerEmail;
    CheckBox customerRemainder;
    EditText customerPhone;

    boolean gender =true;
//    RadioGroup rg;

//    DB
    BarbershopDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

//        Gender
/*        rg = (RadioGroup)findViewById(R.id.rgGender);*/

        customerName = (EditText)findViewById(R.id.etCustomerName);
        customerLastName= (EditText)findViewById(R.id.etCustomerLastName);

        customerPhone =(EditText)findViewById(R.id.etCustomerPhone);

        dbHandler = new BarbershopDBHandler(this);
    }

    public void addCustomer(View view) {

//        Gender
        /*rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                //ZRadioButton rb = (RadioButton)rg.findViewById(checkedId);

                if(checkedId != R.id.rbMan){gender =false;}
            }
        });
*/
        Customer c = new Customer(customerName.getText().toString(),customerPhone.getText().toString());

        Toast.makeText(this, c.getName()+ " " + c.getPhone(), Toast.LENGTH_LONG).show();
        dbHandler.addCustomer(c);


        Intent myIntent = new Intent(this,CustomersListActivity.class);
        startActivity(myIntent);
    }

}
