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

import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.USER_AUTO_LOGIN;
import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.USER_NAME;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener{


    SharedPreferences settings;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
    }

    private void init() {
        this.setTitle(R.string.settings);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layProfile:
                goUserProfile();
                break;

            case R.id.layHaircut:
                openHaircutSettings();
                break;

            case R.id.layBarbershop:

                break;

            case R.id.layWorkingDays:

                break;

            case R.id.layPassword:
                openPasswordSettings();
                break;

            case R.id.layMessages:
                openMessageSetting();
                break;
            case R.id.layAbout:

                break;
            case R.id.layLogout:
                logout();
                break;
            default:
                Toast.makeText(this, "Not Initialized yet", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    public void openWorkingDays(View view) {
        Intent myIntent = new Intent(this,WorkingHoursActivity.class);
        startActivity(myIntent);
        this.finish();
    }

    public void goUserProfile() {
        Intent myIntent = new Intent(this,UserProfileActivity.class);
        startActivity(myIntent);
        this.finish();

    }

    public void openHaircutSettings() {
        Intent myIntent = new Intent(this,TimeAndFee.class);
        startActivity(myIntent);
        this.finish();

    }

    public void openPasswordSettings() {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

        final View mView =getLayoutInflater().inflate(R.layout.dialog_edit_password,null);

        final EditText etPassword = (EditText)mView.findViewById(R.id.d_etPassword);

        //        SharedPreference
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        mBuilder.setTitle(R.string.enterPassword);
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

    public void openMessageSetting() {
        Intent myIntent = new Intent(this,smsSettings.class);
        startActivity(myIntent);
        this.finish();
    }

    public void logout() {
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        editor = settings.edit();
        editor.putBoolean(USER_AUTO_LOGIN,false);
        editor.apply();

        Intent myIntent = new Intent(this,LoginActivity.class);
        startActivity(myIntent);
        this.finish();
    }

}
