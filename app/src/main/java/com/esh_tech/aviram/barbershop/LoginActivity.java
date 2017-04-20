package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Checking if user are exist and have checked auto login.
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String username = settings.getString("username","");
        boolean savePassword = settings.getBoolean("savePassword",false);

        if(username != null && !username.equals("")){
            if (savePassword) {
                //Login with User password
                finish();
                Intent myIntent = new Intent(this, MainActivity.class);
                startActivity(myIntent);
            }else{
                EditText usernameEt = (EditText)findViewById(R.id.usernameET);
                usernameEt.setText(username);
            }
        }else{
            finish();
            Intent myIntent = new Intent(this, UserRegistrationActivity.class);
            startActivity(myIntent);
        }
    }
}
