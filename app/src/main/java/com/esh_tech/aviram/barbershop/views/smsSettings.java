package com.esh_tech.aviram.barbershop.views;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.CustomersDBConstants;
import com.esh_tech.aviram.barbershop.Constants.SharedPreferencesConstants;
import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.R;

public class smsSettings extends AppCompatActivity implements View.OnClickListener{


    //    SharedPreferences
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    //View
    Spinner sMessageTime;
    ArrayAdapter<CharSequence>adapter;
    int userPic;
    int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;

    EditText etEtMessageContent;
    RadioButton rbDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_settings);

        init();
    }

    private void init() {
        settings = PreferenceManager.getDefaultSharedPreferences(this);
//        editor = settings.edit();
        //        boolean register = settings.getBoolean(USER_IS_REGISTER, false);
//        editor.putString(USER_FEMALE_HAIRCUT_TIME,etWomanTime.getText().toString());
//        editor.commit();
        etEtMessageContent = (EditText)findViewById(R.id.etUserMessage);
        rbDefault = (RadioButton)findViewById(R.id.rbDefaultMessage);



        sMessageTime = (Spinner)findViewById(R.id.etSandTime);
        adapter = ArrayAdapter.createFromResource(this,R.array.spinner_sms_time,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sMessageTime.setAdapter(adapter);

        switch (settings.getInt(UserDBConstants.USER_SMS_TIME,0)){
            case 1:
                sMessageTime.setPromptId(0);
                break;
            case 2:
                sMessageTime.setPromptId(1);
                break;
            case 4:
                sMessageTime.setPromptId(2);
                break;
            case 24:
                sMessageTime.setPromptId(3);
                break;
            default:
                sMessageTime.setPromptId(0);
                break;
        }

        sMessageTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userPic =i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





//        sMessageTime.setText(settings.getString(UserDBConstants.USER_SMS_TIME,"30"));

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btSave:
                if(saveMessageSettings()){
                    if(settings.getBoolean(UserDBConstants.USER_IS_REGISTER,false)) {
                        this.finish();
                    }else{
                        Intent passwordIntent = new Intent(this, UserPasswordActivity.class);
                        startActivity(passwordIntent);
                        this.finish();
                    }
                }else{
                    Toast.makeText(this, R.string.notSaved, Toast.LENGTH_LONG).show();
                }
                break;

            default:
                Toast.makeText(this, "Not Initialized yet", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private boolean saveMessageSettings() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        }

        editor = settings.edit();
        editor.putInt(UserDBConstants.USER_SMS_TIME,userPic);
        if(rbDefault.isChecked()){
//            TODO Setup system default message.
//            editor.putString(UserDBConstants.USER_DEFAULT_SMS,getResources().getString(R.string.systemSms));

            editor.putBoolean(SharedPreferencesConstants.SYSTEM_DEFAULT_SMS_IS_CHECKED,true);
            editor.apply();
            return true;
        }else{
            if(etEtMessageContent.getText().toString().length()>2){
                editor.putString(UserDBConstants.USER_DEFAULT_SMS,etEtMessageContent.getText().toString());
                editor.putBoolean(SharedPreferencesConstants.SYSTEM_DEFAULT_SMS_IS_CHECKED,false);
                editor.apply();
                Toast.makeText(this, settings.getString(UserDBConstants.USER_DEFAULT_SMS,""), Toast.LENGTH_SHORT).show();
                return true;
            }else{
                Toast.makeText(this, R.string.messageToShort, Toast.LENGTH_SHORT).show();
            }

        }
        editor.apply();


        return false;
    }
}
