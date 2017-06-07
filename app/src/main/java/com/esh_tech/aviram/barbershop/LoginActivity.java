package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.*;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences settings;
    SharedPreferences.Editor editor;
    String username;
    String userPassword;

    EditText etUsername;
    EditText etUserPassword;
    CheckBox cbAutoRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }

    private void init() {

        etUsername = (EditText)findViewById(R.id.etUsername);
        etUserPassword = (EditText)findViewById(R.id.etPassword);
        cbAutoRegister = (CheckBox)findViewById(R.id.cbSavePassword);


//        Checking if user are exist and have checked auto login.
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        username = settings.getString(USER_NAME,"");
        userPassword = settings.getString(USER_PASSWORD,"");

        boolean savePassword = settings.getBoolean(USER_AUTO_LOGIN,false);

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

            default:
                Toast.makeText(this, "Not Initialed yet", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    private void login() {


        if(username.equals(etUsername.getText().toString())){
            if(userPassword.equals(etUserPassword.getText().toString())){
                if(cbAutoRegister.isChecked()){
                    editor = settings.edit();
                    editor.putBoolean(USER_AUTO_LOGIN,true);
                    editor.apply();
                    Intent myIntent=new Intent(this,MainActivity.class);
                    startActivity(myIntent);
                    this.finish();
                }
            }else {
                Toast.makeText(this, R.string.incorrectPassword, Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, R.string.incorrectUserame, Toast.LENGTH_SHORT).show();
        }


    }
}
