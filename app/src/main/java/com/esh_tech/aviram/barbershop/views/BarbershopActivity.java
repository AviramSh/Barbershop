package com.esh_tech.aviram.barbershop.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.SharedPreferencesConstants;
import com.esh_tech.aviram.barbershop.R;



public class BarbershopActivity extends AppCompatActivity implements View.OnClickListener{

    //    SharedPreferences
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    EditText businessName;
    EditText businessAddress;
    EditText businessPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barbershop);

        init();
    }

    private void init() {
//        TODO add change settings with Shared preferences
//        TODO need to convert shared preferences Key Value in registration

        settings = PreferenceManager.getDefaultSharedPreferences(this);

        settings.getString(SharedPreferencesConstants.BUSINESS_NAME,"MY_BUSINESS_NAME");


        businessName = (EditText)findViewById(R.id.etBusinessName);
        businessAddress = (EditText)findViewById(R.id.etBusinessAddress);
        businessPhone = (EditText)findViewById(R.id.etBusinessPhone);

        businessName.setText(settings.getString(SharedPreferencesConstants.BUSINESS_NAME,"MY_BUSINESS_NAME"));
        businessAddress.setText(settings.getString(SharedPreferencesConstants.BUSINESS_ADDRESS,"MY_BUSINESS_ADDRESS"));
        businessPhone.setText(settings.getString(SharedPreferencesConstants.BUSINESS_PHONE,"MY_BUSINESS_PHONE"));

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btBack){
            Intent intent= new Intent(this,SettingsActivity.class);
            startActivity(intent);
            this.finish();
        }
        switch (v.getId()){
            case R.id.btBack:

                break;
            case R.id.btSave:
//                TODO Save data in shared Preferences is user want to save data
                //        editor = settings.edit();
                //        editor.putString(SharedPreferencesConstants.BUSINESS_NAME,"MY_BUSINESS_NAME");
                //        editor.apply();
                break;

            default:
                Toast.makeText(this, "Not Initialized yet", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
