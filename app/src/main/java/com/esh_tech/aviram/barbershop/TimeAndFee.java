package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.*;

public class TimeAndFee extends AppCompatActivity implements View.OnClickListener{

    EditText etManPrice;
    EditText etManTime;
    EditText etWomanPrice;
    EditText etWomanTime;


    //    SharedPreferences
    SharedPreferences settings;//Get the values
    SharedPreferences.Editor editor;//To add values
    boolean register;
    String haircut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_and_fee);

        init();
    }

    private void init() {
        etManPrice = (EditText)findViewById(R.id.etManPrice);
        etManTime = (EditText)findViewById(R.id.etManTime);
        etWomanPrice = (EditText)findViewById(R.id.etWomanPrice);
        etWomanTime = (EditText)findViewById(R.id.etWomanTime);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(TimeAndFee.this);

//        Get Values
        register = settings.getBoolean(USER_IS_REGISTER, false);
        editor = settings.edit();


    }

    public void fillCustomers() {


        if(Integer.parseInt(etManPrice.getText().toString()) > 0 &&
                Integer.parseInt(etManTime.getText().toString()) > 0 &&
                        Integer.parseInt(etWomanTime.getText().toString())>0 &&
                                Integer.parseInt(etWomanPrice.getText().toString()) > 0){

            editor.putString(USER_MALE_HAIRCUT_PRICE,etManPrice.getText().toString());
            editor.putString(USER_MALE_HAIRCUT_TIME,etManTime.getText().toString());
            editor.putString(USER_FEMALE_HAIRCUT_PRICE,etWomanPrice.getText().toString());
            editor.putString(USER_FEMALE_HAIRCUT_TIME,etWomanTime.getText().toString());


            editor.commit();
        }
        if(register){
            Intent myIntent = new Intent(this,MainActivity.class);
            startActivity(myIntent);
            this.finish();
        }else{
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
            default:
                Toast.makeText(this, "Not Initialized yet .", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
