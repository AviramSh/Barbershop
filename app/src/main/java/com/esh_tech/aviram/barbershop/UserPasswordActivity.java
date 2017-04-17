package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class UserPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_password);
    }


    //back to registration Activity
    public void goRegisterAc(View v){
        Intent registrationIntent = new Intent(this ,UserRegistrationActivity.class);
        startActivity(registrationIntent);
        this.finish();
    }

    public void savePassword(View view) {
        Intent myIntent = new Intent(this , WorkingHoursActivity.class);
        startActivity(myIntent);
        this.finish();

    }
}
