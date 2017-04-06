package com.esh_tech.aviram.barbershop;
import com.esh_tech.aviram.barbershop.Codes.Barbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.esh_tech.aviram.barbershop.R;

import java.util.ArrayList;

public class WorkingHoursActivity extends AppCompatActivity {

    //ArrayList <Barbershop>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_hours);
    }

    public void timeAndfee(View view) {
        Intent timeFeeActivity = new Intent(this,TimeAndFee.class);
        startActivity(timeFeeActivity);
        this.finish();
    }
}


