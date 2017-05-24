package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import static com.esh_tech.aviram.barbershop.Database.BarbershopConstants.*;


public class UserPasswordActivity extends AppCompatActivity {


    //    SharedPreferences
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    EditText pass1;
    EditText pass2;

    CheckBox savePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_password);

        pass1 = (EditText)findViewById(R.id.passwordEt);
        pass2= (EditText)findViewById(R.id.rePasswordEt);

        savePassword = (CheckBox)findViewById(R.id.cbSavePassword);

    }


    //back to registration Activity
    public void goRegisterAc(View v){
        Intent registrationIntent = new Intent(this ,UserRegistrationActivity.class);
        startActivity(registrationIntent);
        this.finish();
    }

    public void savePassword(View view) {

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        editor = settings.edit();

        if(pass1.getText().toString().equals(pass2.getText().toString())) {
//            MyGlobalUser myUser = (MyGlobalUser)getApplication();
//            myUser.setPassword(pass1.getText().toString());
            editor.putString(USER_PASSWORD,pass1.getText().toString());
            if(savePassword.isChecked()){
                editor.putBoolean(AUTO_LOGIN,true);
            }
            Intent myIntent = new Intent(this, WorkingHoursActivity.class);
            startActivity(myIntent);
            this.finish();
        }

        editor.commit();
    }
}
