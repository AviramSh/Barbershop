package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.esh_tech.aviram.barbershop.R;

public class FillCustomers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_customers);
    }

    public void goMain(View view) {
        Intent mainIntent = new Intent(this,MainActivity.class);
        startActivity(mainIntent);
        this.finish();
    }
}
