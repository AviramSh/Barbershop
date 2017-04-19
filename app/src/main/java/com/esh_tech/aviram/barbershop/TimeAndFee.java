package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TimeAndFee extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_and_fee);
    }

    public void fillCustomres(View view) {
        Intent fillCustomresAcivity = new Intent(this,FillCustomersActivity.class);
        startActivity(fillCustomresAcivity);
        this.finish();
    }
}
