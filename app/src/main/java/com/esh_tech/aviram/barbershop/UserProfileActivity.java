package com.esh_tech.aviram.barbershop;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.*;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener{

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

        init();
    }

    private void init() {

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


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            default:
                Toast.makeText(this, "Not Initialized", Toast.LENGTH_LONG).show();
                break;
        }
    }
}
