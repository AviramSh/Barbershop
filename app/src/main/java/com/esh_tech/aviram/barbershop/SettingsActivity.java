package com.esh_tech.aviram.barbershop;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;


public class SettingsActivity extends AppCompatActivity {


    SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
    }

    private void init() {
        this.setTitle(R.string.settings);
    }


    public void openWorkingDays(View view) {
        Intent myIntent = new Intent(this,WorkingHoursActivity.class);
        startActivity(myIntent);
        this.finish();
    }

    public void goUserProfile(View view) {
        Intent myIntent = new Intent(this,UserProfileActivity.class);
        startActivity(myIntent);
        this.finish();

    }

    public void openHaircutSettings(View view) {
        Intent myIntent = new Intent(this,TimeAndFee.class);
        startActivity(myIntent);
        this.finish();

    }

    public void openPasswordSettings(View view) {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        final View mView =getLayoutInflater().inflate(R.layout.dialog_edit_password,null);

        final EditText etPassword = (EditText)mView.findViewById(R.id.d_etPassword);

        //        SharedPreference
        settings = PreferenceManager.getDefaultSharedPreferences(this);

        mBuilder.setNeutralButton(R.string.enter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(etPassword.getText().toString().equals(settings.getString(UserDBConstants.USER_PASSWORD,""))) {
                    Intent myIntent = new Intent(SettingsActivity.this, UserPasswordActivity.class);
                    startActivity(myIntent);
                }else{
                    Toast.makeText(SettingsActivity.this, R.string.incorrectPassword, Toast.LENGTH_LONG).show();
                }
            }
        });


        mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(SettingsActivity.this, R.string.cancel, Toast.LENGTH_LONG).show();
            }
        });


        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();



    }

    public void openMessageSetting(View view) {
        Intent myIntent = new Intent(this,smsSettings.class);
        startActivity(myIntent);
        this.finish();
    }

    public void logout(View view) {
        Intent myIntent = new Intent(this,LoginActivity.class);
        startActivity(myIntent);
        this.finish();
    }
}
