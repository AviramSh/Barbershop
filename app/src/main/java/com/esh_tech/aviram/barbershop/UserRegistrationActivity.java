package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UserRegistrationActivity extends AppCompatActivity {

    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);


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
        name = (EditText)findViewById(R.id.firstNameEt);
        EditText lastName = (EditText)findViewById(R.id.lastNameEt);
        EditText phone = (EditText)findViewById(R.id.phoneEt);

        EditText businessName = (EditText)findViewById(R.id.businessNameEt);
        EditText businessPhone = (EditText)findViewById(R.id.businessPhoneEt);
        EditText businessAddress = (EditText)findViewById(R.id.businessAddressEt);



//First Name testing
        testString = name.getText().toString();
        if(testString.equals("")){flag = false; errorMassage += "First name is null\n";}
        else if(testString.length()<2){flag = false; errorMassage += "First name is to short\n";}
//Last Name testing
        testString = lastName.getText().toString();
        if(testString.equals("")){flag = false; errorMassage += "Last name is null\n";}
        else if(testString.length()<2){flag = false; errorMassage += "Last name is to short\n";}
//Phone testing
        testString = phone.getText().toString();
        if(testString.equals("")){flag = false; errorMassage += "Phone is null\n";}
        else if(testString.length()<4){flag = false; errorMassage += "Phone is to short\n";}

//Business Name testing
        testString = businessName.getText().toString();
        if(testString.equals("")){flag = false; errorMassage += "Business Name is null\n";}
        else if(testString.length()<2){flag = false; errorMassage += "Business Name is to short\n";}

//Business Phone testing
        testString = businessPhone.getText().toString();
        if(testString.equals("")){flag = false; errorMassage += "Business phone is null\n";}
        else if(testString.length()<4){flag = false; errorMassage += "Business phone is to short\n";}

//Business Address testing
        testString = businessAddress.getText().toString();
        if(testString.equals("")){flag = false; errorMassage += "Business address is null\n";}
        else if(testString.length()<4){flag = false; errorMassage += "Business address is to short\n";}


// Data Massages
        if(flag) Toast.makeText(this, "Hi "+ name.getText().toString() +" "+lastName.getText().toString(), Toast.LENGTH_LONG).show();
        else Toast.makeText(this, errorMassage, Toast.LENGTH_LONG).show();

        return flag;
    }
}



