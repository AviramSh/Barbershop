package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class TimeAndFee extends AppCompatActivity implements View.OnClickListener{

    EditText etManPrice;
    EditText etManTime;
    EditText etWomanPrice;
    EditText etWomanTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_and_fee);

        init();
    }

    private void init() {
        etManPrice = (EditText)findViewById(R.id.etManPrice);
        etManPrice = (EditText)findViewById(R.id.etManTime);
        etWomanPrice = (EditText)findViewById(R.id.etWomanPrice);
        etWomanTime = (EditText)findViewById(R.id.etWomanTime);
    }



    public void fillCustomers() {

        if(Integer.parseInt(etManPrice.getText().toString()) > 0 &&
                Integer.parseInt(etManTime.getText().toString()) > 0 &&
                        Integer.parseInt(etWomanTime.getText().toString())>0 &&
                                Integer.parseInt(etWomanPrice.getText().toString()) > 0){
            Intent fillCustomersActivity = new Intent(this,FillCustomersActivity.class);
            startActivity(fillCustomersActivity);
            this.finish();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btSave:
                fillCustomers();
                break;
        }
    }
}
