package com.esh_tech.aviram.barbershop.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.*;
import com.esh_tech.aviram.barbershop.R;

public class UserRegistrationActivity extends AppCompatActivity {


    SharedPreferences settings;
    SharedPreferences.Editor editor;

    EditText name;
    EditText lastName;
    EditText phone;
    EditText businessName;
    EditText businessPhone;
    EditText businessAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        init();
    }

    private void init() {

        //        SharedPreference
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        editor = settings.edit();

        name = (EditText)findViewById(R.id.etUserName);
        lastName = (EditText)findViewById(R.id.etUserLastName);
        phone = (EditText)findViewById(R.id.etUserTelephone);

        businessName = (EditText)findViewById(R.id.etBusinessName);
        businessPhone = (EditText)findViewById(R.id.etBusinessPhone);
        businessAddress = (EditText)findViewById(R.id.etBusinessAddress);


        name.setText(settings.getString(USER_NAME,""));
        lastName.setText(settings.getString(USER_LAST_NAME,""));
        phone.setText(settings.getString(USER_PHONE,""));

        businessName.setText(settings.getString(USER_BUSINESS_NAME,""));
        businessPhone.setText(settings.getString(USER_BUSINESS_PHONE,""));
        businessAddress.setText(settings.getString(USER_BUSINESS_ADDRESS,""));

    }

    public void closeScreen(View view) {
        finish();
    }

    public void passwordActivityNext(View view) {
//                    Checking if user are exist and have checked auto login.
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(UserRegistrationActivity.this);
        boolean register = settings.getBoolean(USER_IS_REGISTER, false);

        if(saveUserdata() && !register){
            Intent passwordIntent = new Intent(this, UserPasswordActivity.class);
            startActivity(passwordIntent);
            this.finish();
        }else{
            if(register)
            this.finish();
        }

    }


    private boolean saveUserdata() {
        String testString;
        String errorMassage="Error : \n";

        boolean flag = true;


//First Name testing

        testString = name.getText().toString();

        if(testString.equals("")){
            flag = false; errorMassage += "First name is null\n";
        }
        else if(testString.length()<2){
            flag = false; errorMassage += "First name is to short\n";
        }else{
            editor.putString(USER_NAME,testString);
        }


//Last Name testing
        testString = lastName.getText().toString();
        if(testString.equals("")){flag = false; errorMassage += "Last name is null\n";}
        else if(testString.length()<2){flag = false; errorMassage += "Last name is to short\n";}else {
            editor.putString(USER_LAST_NAME,testString);
        }
//Phone testing
        testString = phone.getText().toString();
        if(testString.equals("")){flag = false; errorMassage += "Phone is null\n";}
        else if(testString.length()<8){flag = false; errorMassage += "Phone is to short\n";}else {
            editor.putString(USER_PHONE,testString);
        }

//Business Name testing
        testString = businessName.getText().toString();
        if(testString.equals("")){flag = false; errorMassage += "Business Name is null\n";}
        else if(testString.length()<2){flag = false; errorMassage += "Business Name is to short\n";}else {
            editor.putString(USER_BUSINESS_NAME,testString);
        }

//Business Phone testing
        testString = businessPhone.getText().toString();
        if(testString.equals("")){flag = false; errorMassage += "Business phone is null\n";}
        else if(testString.length()<8){flag = false; errorMassage += "Business phone is to short\n";}else {
            editor.putString(USER_BUSINESS_PHONE,testString);
        }

//Business Address testing
        testString = businessAddress.getText().toString();
        if(testString.equals("")){flag = false; errorMassage += "Business address is null\n";}
        else if(testString.length()<4){flag = false; errorMassage += "Business address is to short\n";}else {
            editor.putString(USER_BUSINESS_ADDRESS,testString);
        }

        editor.apply();

        if(!flag){
            Toast.makeText(this, errorMassage+"", Toast.LENGTH_SHORT).show();
        }

        return flag;
    }
}



