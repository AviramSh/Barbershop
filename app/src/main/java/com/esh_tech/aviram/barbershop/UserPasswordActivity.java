package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


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
        EditText pass1 = (EditText)findViewById(R.id.passwordEt);
        EditText pass2= (EditText)findViewById(R.id.rePasswordEt);

        if(pass1.getText().toString().equals(pass2.getText().toString())) {
            MyGlobalUser myUser = (MyGlobalUser)getApplication();
            myUser.setPassword(pass1.getText().toString());

            Intent myIntent = new Intent(this, WorkingHoursActivity.class);
            startActivity(myIntent);
            this.finish();
        }
    }
}
