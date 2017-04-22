package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Codes.Customer;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

public class NewCustomerActivity extends AppCompatActivity {

    EditText customerName;
    EditText customerLastName;
    EditText customerPhone;
//    DB
    BarbershopDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        customerName = (EditText)findViewById(R.id.etCustomerName);
        customerLastName= (EditText)findViewById(R.id.etCustomerLastName);
        customerPhone =(EditText)findViewById(R.id.etCustomerPhone);

        dbHandler = new BarbershopDBHandler(this);
    }

    public void addCustomer(View view) {
        Customer c = new Customer(customerName.getText().toString(),customerPhone.getText().toString());

        Toast.makeText(this, c.getName()+ " " + c.getPhone(), Toast.LENGTH_LONG).show();
        dbHandler.addCustomer(c);


        Intent myIntent = new Intent(this,CustomersListActivity.class);
        startActivity(myIntent);
    }

}
