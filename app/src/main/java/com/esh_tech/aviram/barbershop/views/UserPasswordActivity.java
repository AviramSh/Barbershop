package com.esh_tech.aviram.barbershop.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.R;
import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.*;



public class UserPasswordActivity extends AppCompatActivity implements View.OnClickListener{


    //    SharedPreferences
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    EditText pass1;
    EditText pass2;

    CheckBox savePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_password);

        init();
    }

    private void init() {

        pass1 = (EditText)findViewById(R.id.passwordEt);
        pass2= (EditText)findViewById(R.id.rePasswordEt);

        savePassword = (CheckBox)findViewById(R.id.cbSavePassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btSavePassword:
                savePassword();
                break;
            default:
                Toast.makeText(this, "Not Initialized yet", Toast.LENGTH_SHORT).show();
                break;

        }
    }



    public void savePassword() {

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean register = settings.getBoolean(USER_IS_REGISTER, false);
        editor = settings.edit();

        if(pass1.getText().toString().equals(pass2.getText().toString())) {
            if(pass1.getText().toString().length()>3 ) {
                editor.putString(USER_PASSWORD, pass1.getText().toString());
                if (savePassword.isChecked()) {
                    editor.putBoolean(USER_AUTO_LOGIN, true);
                    editor.apply();
                }
                editor.apply();
                if (register) {
                    this.finish();
                } else {
                    Intent myIntent = new Intent(this, WorkingHoursActivity.class);
                    startActivity(myIntent);
                    this.finish();
                }
            }else Toast.makeText(this, R.string.passwordsTooShort, Toast.LENGTH_SHORT).show();
        }else {

            Toast.makeText(this, R.string.passwordsDoNotMatch, Toast.LENGTH_SHORT).show();
        }


    }
}
