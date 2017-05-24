package com.esh_tech.aviram.barbershop;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import static com.esh_tech.aviram.barbershop.Database.BarbershopConstants.*;

public class UserProfileActivity extends AppCompatActivity {

    //    SharedPreferences
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    EditText etUserName;
    EditText etUserLastName;
    EditText etUserTelephone;

    EditText etBusinessName;
    EditText etBusinessPhone;
    EditText etBusinessAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        this.setTitle(R.string.myProfile);

        etUserName = (EditText)findViewById(R.id.etUserName);
        etUserLastName = (EditText)findViewById(R.id.etUserLastName);
        etUserTelephone= (EditText)findViewById(R.id.etUserTelephone);

        etBusinessName = (EditText)findViewById(R.id.etBusinessName);
        etBusinessPhone = (EditText)findViewById(R.id.etBusinessPhone);
        etBusinessAddress = (EditText)findViewById(R.id.etBusinessAddress);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        editor = settings.edit();

        etUserName.setText(settings.getString(USER_NAME,""));
        etUserLastName.setText(settings.getString(USER_LAST_NAME,""));
        etUserTelephone.setText(settings.getString(USER_PHONE,""));
//
        etBusinessName.setText(settings.getString(USER_BUSINESS_NAME,""));
        etBusinessPhone.setText(settings.getString(USER_BUSINESS_PHONE,""));
        etBusinessAddress.setText(settings.getString(USER_BUSINESS_ADDRESS,""));
    }



}
