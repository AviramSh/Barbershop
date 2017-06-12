package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.R;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;
import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.USER_FEMALE_HAIRCUT_TIME;

public class smsSettings extends AppCompatActivity implements View.OnClickListener{


    //    SharedPreferences
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    //View
    EditText etMessageTime;
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
        etMessageTime = (EditText)findViewById(R.id.etSandTime);
        etEtMessageContent = (EditText)findViewById(R.id.etUserMessage);
        rbDefault = (RadioButton)findViewById(R.id.rbDefaultMessage);


        etMessageTime.setText(settings.getString(UserDBConstants.USER_SMS_TIME,"30"));

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btSave:
                if(saveMessageSettings()){
                    Intent myIntent = new Intent(this,MainActivity.class);
                    startActivity(myIntent);
                    this.finish();
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

        if(rbDefault.isChecked()){
            editor.putString(UserDBConstants.USER_DEFAULT_SMS,getResources().getString(R.string.systemSms));
        }else{
            if(etEtMessageContent.getText().toString().length()>2){
                editor.putString(UserDBConstants.USER_DEFAULT_SMS,etEtMessageContent.getText().toString());
            }else{
                Toast.makeText(this, R.string.messageToShort, Toast.LENGTH_SHORT).show();
            }

        }


        return false;
    }
}
