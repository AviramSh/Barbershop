package com.esh_tech.aviram.barbershop.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.R;

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

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        etManPrice.setText(String.valueOf(settings.getInt(USER_MALE_HAIRCUT_PRICE,35)));
        etManTime.setText(String.valueOf(settings.getInt(USER_MALE_HAIRCUT_TIME,35)));

        etWomanPrice.setText(String.valueOf(settings.getInt(USER_FEMALE_HAIRCUT_PRICE,35)));
        etWomanTime.setText(String.valueOf(settings.getInt(USER_FEMALE_HAIRCUT_TIME,35)));
//        Get Values
        register = settings.getBoolean(USER_IS_REGISTER, false);



    }

    public void fillCustomers() {
        boolean flag = true;
        editor = settings.edit();
        int haircutTime=0;
        int haircutPrice=0;

        try {
            haircutPrice=Integer.parseInt(etManPrice.getText().toString());
            haircutTime=Integer.parseInt(etManTime.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            flag = false;
        }

        if( haircutTime > 0 &&
                 haircutPrice > 0){

            editor.putInt(USER_MALE_HAIRCUT_PRICE,Integer.parseInt(etManPrice.getText().toString()));
            editor.putInt(USER_MALE_HAIRCUT_TIME,Integer.parseInt(etManTime.getText().toString()));

        }else flag = false;



        haircutTime=0;
        haircutPrice=0;

        try {
            haircutTime = Integer.parseInt(etWomanTime.getText().toString());
            haircutPrice = Integer.parseInt(etWomanPrice.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            flag = false;
        }

        if( haircutTime > 0 &&
                haircutPrice > 0){

            editor.putInt(USER_FEMALE_HAIRCUT_PRICE, Integer.parseInt(etWomanPrice.getText().toString()));
            editor.putInt(USER_FEMALE_HAIRCUT_TIME,Integer.parseInt(etWomanTime.getText().toString()));

        }else flag = false;

        editor.apply();

        if(flag) {
            if (register) {
                this.finish();
            } else {
                Intent fillCustomersActivity = new Intent(this, CustomersListActivity.class);
                startActivity(fillCustomersActivity);
                this.finish();
            }
        }else {
            Toast.makeText(this, R.string.wrongData, Toast.LENGTH_SHORT).show();
        }
//        editor.apply();
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
