package com.esh_tech.aviram.barbershop.views;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.*;
import com.esh_tech.aviram.barbershop.R;
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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        etUserName = (EditText)findViewById(R.id.etUserName);
        etUserLastName = (EditText)findViewById(R.id.et_password);
        etUserTelephone= (EditText)findViewById(R.id.etUserTelephone);

        etBusinessName = (EditText)findViewById(R.id.etBusinessName);
        etBusinessPhone = (EditText)findViewById(R.id.etBusinessPhone);
        etBusinessAddress = (EditText)findViewById(R.id.etBusinessAddress);

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        

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
            case R.id.btSave:
                saveUserChange();
                break;

            default:
                Toast.makeText(this, "Not Initialized", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void saveUserChange() {
        
        editor = settings.edit();

        if(!etUserName.getText().toString().isEmpty()&& etUserName.getText().toString().length()>2)
            editor.putString(USER_NAME,etUserName.getText().toString());

        if(!etUserLastName.getText().toString().isEmpty()&& etUserLastName.getText().toString().length()>2)
            editor.putString(USER_LAST_NAME,etUserLastName.getText().toString());

        if(!etUserTelephone.getText().toString().isEmpty()&& etUserTelephone.getText().toString().length()>6)
            editor.putString(USER_PHONE,etUserTelephone.getText().toString());

        if(!etBusinessName.getText().toString().isEmpty()&& etBusinessName.getText().toString().length()>2)
            editor.putString(USER_BUSINESS_NAME,etBusinessName.getText().toString());

        if(!etBusinessPhone.getText().toString().isEmpty()&& etBusinessPhone.getText().toString().length()>6)
            editor.putString(USER_BUSINESS_PHONE,etBusinessPhone.getText().toString());

        if(!etBusinessAddress.getText().toString().isEmpty()&& etBusinessAddress.getText().toString().length()>4)
            editor.putString(USER_BUSINESS_ADDRESS,etBusinessAddress.getText().toString());

        editor.apply();

        Toast.makeText(this, R.string.save, Toast.LENGTH_SHORT).show();
        this.finish();
    }
}
