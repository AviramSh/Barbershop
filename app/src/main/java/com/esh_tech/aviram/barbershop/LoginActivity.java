package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.*;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Checking if user are exist and have checked auto login.
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String username = settings.getString(USER_NAME,"");
        boolean savePassword = settings.getBoolean(USER_PASSWORD,false);

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
                this.finish();
                Intent myIntent = new Intent(this, UserRegistrationActivity.class);
                startActivity(myIntent);
            }
    }
}
