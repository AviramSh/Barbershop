package com.esh_tech.aviram.barbershop.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.*;

import com.esh_tech.aviram.barbershop.Constants.SharedPreferencesConstants;
import com.esh_tech.aviram.barbershop.R;
import com.esh_tech.aviram.barbershop.Utils.DateUtils;
import com.esh_tech.aviram.barbershop.data.Config;
import com.esh_tech.aviram.barbershop.data.SendMail;

import java.util.Calendar;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    String username;
    String userPassword;

    EditText etUsername;
    TextView forgetPassword;
    EditText etUserPassword;
    CheckBox cbAutoRegister;
    Button btExit;
    Button btLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {

        etUsername = (EditText)findViewById(R.id.etUsername);
        etUserPassword = (EditText)findViewById(R.id.et_password);
        cbAutoRegister = (CheckBox)findViewById(R.id.cbSavePassword);
        forgetPassword = (TextView)findViewById(R.id.tvForgetPassword);
        btLogin = (Button)findViewById(R.id.btLogin);
        btExit = (Button)findViewById(R.id.exitApp);

        forgetPassword.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        btExit.setOnClickListener(this);

//        Checking if user are exist and have checked auto login.
        settings = PreferenceManager.getDefaultSharedPreferences(this);


        username = settings.getString(USER_NAME,"");
        userPassword = settings.getString(USER_PASSWORD,"");

        boolean savePassword = settings.getBoolean(USER_AUTO_LOGIN,false);

        cbAutoRegister.setChecked(savePassword);

        if(!username.equals("")){
            if (savePassword) {
                //Login with User password
                Intent myIntent = new Intent(this, MainActivity.class);
                startActivity(myIntent);
                this.finish();
            }else{
                EditText usernameEt = (EditText)findViewById(R.id.etUsername);
                usernameEt.setText(username);

            }
        }else{
            Intent myIntent = new Intent(this, UserRegistrationActivity.class);
            startActivity(myIntent);
            this.finish();
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btLogin:
                login();
                break;
            case R.id.exitApp:
                this.finish();
                break;
            case R.id.tvForgetPassword:
                resatPassword();
                break;

            default:
                Toast.makeText(this, "Not Initialed yet", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    private void resatPassword() {
//        TODO Test if record is ok if not connect to support
        String todayDate = DateUtils.getDateAndTime(Calendar.getInstance());
        String testDate= settings.getString(
                SharedPreferencesConstants.PASSWORD_DATE_FOR_REST,"");


        if(!todayDate.equals(testDate)){
            editor = settings.edit();
            editor.putInt(SharedPreferencesConstants.PASSWORD_COUNTER,1);
            editor.putString(SharedPreferencesConstants.PASSWORD_DATE_FOR_REST,todayDate);
            editor.apply();
        }

        if(settings.getInt(SharedPreferencesConstants.PASSWORD_COUNTER,3) < 3 &&
                todayDate.equals(testDate)){

        }



            if(username.equals(etUsername.getText().toString())){

                SendMail sm = new SendMail(this,Config.EMAIL,getResources().getString(R.string.resatYourPassWord),
                        settings.getString(USER_PASSWORD,"Error"));
                sm.execute();

                int counter =settings.getInt(SharedPreferencesConstants.PASSWORD_COUNTER,3);
                editor =settings.edit();
                editor.putInt(SharedPreferencesConstants.PASSWORD_COUNTER,++counter);
                editor.apply();

//                Toast.makeText(this, R.string.passwordSentToYourEmail, Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, R.string.incorrectUserName, Toast.LENGTH_SHORT).show();
        }
    }

    private void login() {

            if (username.equals(etUsername.getText().toString())) {
                if (userPassword.equals(etUserPassword.getText().toString())) {
                    if (cbAutoRegister.isChecked()) {
                        editor =settings.edit();
                        editor.putBoolean(USER_AUTO_LOGIN, true);
                        editor.apply();
                    }
                    Intent myIntent = new Intent(this, MainActivity.class);
                    startActivity(myIntent);
                    this.finish();
                } else {
                    Toast.makeText(this, R.string.incorrectPassword, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, R.string.incorrectUserName, Toast.LENGTH_SHORT).show();
            }
        }
}
