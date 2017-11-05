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
import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
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

        /*settings.getString(SharedPreferencesConstants.BUSINESS_NAME,
                getResources().getString(R.string.default_business_name));*/


        businessName = (EditText)findViewById(R.id.etBusinessName);
        businessAddress = (EditText)findViewById(R.id.etBusinessAddress);
        businessPhone = (EditText)findViewById(R.id.etBusinessPhone);

        businessName.setText(settings.getString(UserDBConstants.USER_BUSINESS_NAME,
                getResources().getString(R.string.default_business_name)));

        businessAddress.setText(settings.getString(UserDBConstants.USER_BUSINESS_ADDRESS,
                getResources().getString(R.string.default_business_address)));
        businessPhone.setText(settings.getString(UserDBConstants.USER_PHONE,
                getResources().getString(R.string.default_business_phone)));

    }


    @Override
    public void onClick(View v) {
//        if(v.getId() == R.id.btBack){
//            Intent intent= new Intent(this,SettingsActivity.class);
//            startActivity(intent);
//            this.finish();
//        }
        switch (v.getId()){
            case R.id.btBack:

                break;
            case R.id.btSave:
                if(saveBusinessData())this.finish();
                else Toast.makeText(this, R.string.fields_are_not_full, Toast.LENGTH_SHORT).show();
                break;

            default:
                Toast.makeText(this, getResources().getString(R.string.not_initialized_yet), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private boolean saveBusinessData() {
//                TODO Save data in shared Preferences is user want to save data

        if(businessName.getText().toString().equals("")||
                businessPhone.getText().toString().equals("")||
                businessAddress.getText().toString().equals("")){
            Toast.makeText(this, getResources().getString(R.string.fields_are_not_full), Toast.LENGTH_SHORT).show();
            return false;
        }

        try {

            editor = settings.edit();
            editor.putString(UserDBConstants.USER_BUSINESS_NAME,businessName.getText().toString());
            editor.putString(UserDBConstants.USER_PHONE,businessPhone.getText().toString());
            editor.putString(UserDBConstants.USER_BUSINESS_ADDRESS,businessAddress.getText().toString());
            editor.apply();
//            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
//            Toast.makeText(this, "DATA ERROR", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
