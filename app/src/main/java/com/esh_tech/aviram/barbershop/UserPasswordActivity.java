package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.*;


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

        init();

    }

    private void init() {

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
        boolean register = settings.getBoolean(USER_IS_REGISTER, false);
        editor = settings.edit();

        if(pass1.getText().toString().equals(pass2.getText().toString())&&pass1.getText().toString().length()>=3) {
//            MyGlobalUser myUser = (MyGlobalUser)getApplication();
//            myUser.setPassword(pass1.getText().toString());
            editor.putString(USER_PASSWORD,pass1.getText().toString());
            if(savePassword.isChecked()){
                editor.putBoolean(USER_AUTO_LOGIN,true);
            }
            editor.commit();
            if(register){
                Intent myIntent = new Intent(this, MainActivity.class);
                startActivity(myIntent);
                this.finish();
            }else{
                Intent myIntent = new Intent(this, WorkingHoursActivity.class);
                startActivity(myIntent);
                this.finish();
            }
//            Intent myIntent = new Intent(this, WorkingHoursActivity.class);
//            startActivity(myIntent);
//            this.finish();
        }


    }
}
