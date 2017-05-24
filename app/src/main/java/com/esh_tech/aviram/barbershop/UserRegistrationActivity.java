package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.esh_tech.aviram.barbershop.Database.BarbershopConstants.*;


public class UserRegistrationActivity extends AppCompatActivity {

    //      My Global User class
//    MyGlobalUser myUser = (MyGlobalUser)getApplication();

//    SharedPreferences
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

        name = (EditText)findViewById(R.id.etUserName);
        lastName = (EditText)findViewById(R.id.etUserLastName);
        phone = (EditText)findViewById(R.id.etUserTelephone);

        businessName = (EditText)findViewById(R.id.etBusinessName);
        businessPhone = (EditText)findViewById(R.id.etBusinessPhone);
        businessAddress = (EditText)findViewById(R.id.etBusinessAddress);
    }

    public void closeScreen(View view) {
        finish();
    }

    public void passwordActivityNext(View view) {

        if(saveUserdata()){
            Intent passwordIntent = new Intent(this, UserPasswordActivity.class);
            startActivity(passwordIntent);
            this.finish();
        }

    }

    //Save your on shared preferences.
    private boolean saveUserdata() {

        //test all user data
        if(!testData())
            return false;

        return true;
    }

    private boolean testData() {
        String testString;
        String errorMassage="Error : \n";

        boolean flag = true;

        //        SharedPreference
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        editor = settings.edit();


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
        else if(testString.length()<4){flag = false; errorMassage += "Phone is to short\n";}else {
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
        else if(testString.length()<4){flag = false; errorMassage += "Business phone is to short\n";}else {
            editor.putString(USER_BUSINESS_PHONE,testString);
        }

//Business Address testing
        testString = businessAddress.getText().toString();
        if(testString.equals("")){flag = false; errorMassage += "Business address is null\n";}
        else if(testString.length()<4){flag = false; errorMassage += "Business address is to short\n";}else {
            editor.putString(USER_BUSINESS_ADDRESS,testString);
        }

        editor.commit();
// Data Massages
        if(flag) Toast.makeText(this, "Hi "+ name.getText().toString() +" "+lastName.getText().toString(), Toast.LENGTH_LONG).show();
        else Toast.makeText(this, errorMassage, Toast.LENGTH_LONG).show();

        return flag;
    }
}



