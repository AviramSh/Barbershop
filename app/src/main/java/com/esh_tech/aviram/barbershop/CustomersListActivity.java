package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CustomersListActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

    }

    // *Create new customer
    public void openNewCustomer(View view) {
        Intent myIntent = new Intent(this ,NewCustomerActivity.class);
        startActivity(myIntent);
    }
}
