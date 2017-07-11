package com.esh_tech.aviram.barbershop.views;
//import com.esh_tech.aviram.barbershop.Codes.Barbershop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;


import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.USER_IS_REGISTER;
import com.esh_tech.aviram.barbershop.R;

public class WorkingHoursActivity extends AppCompatActivity {

    //    SharedPreferences
    SharedPreferences settings;
    SharedPreferences.Editor editor;

//    init

    CheckBox cbDay;
    Spinner sStart;
    Spinner sEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_working_hours);

        init();
    }

    private void init() {
//        TODO set all settings for user working days and time.
    }

    public void timeAndFee(View view) {

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean register = settings.getBoolean(USER_IS_REGISTER, false);
        editor = settings.edit();

        if(setWorkingHours(view)) {
            if (register) {
                this.finish();
            } else {
                Intent timeFeeActivity = new Intent(this, TimeAndFee.class);
                startActivity(timeFeeActivity);
                this.finish();
            }
        }

    }


//    Need Work
    private boolean setWorkingHours(View v) {
/*

        cbDay = (CheckBox)findViewById(R.id.sundayCb);
        sStart = (Spinner) findViewById(R.id.sundaySspin);
        sEnd = (Spinner) findViewById(R.id.sundayEspin);

        if(((CheckBox) findViewById(R.id.sundayCb)).isChecked()){

        }

        */
/*if(cbDay.isChecked()) {
            if(testHuors(sStart.getSelectedItem(),sEnd.getSelectedItem()))
        }*//*


        cbDay = (CheckBox)findViewById(R.id.mondayCb);
        sStart = (Spinner) findViewById(R.id.mondaySspin);
        sEnd = (Spinner) findViewById(R.id.mondayEspin);

        if(cbDay.isChecked()) {
            sStart.getSelectedItem();
            sEnd.getSelectedItem();
        }

        Toast.makeText(this, sStart.getSelectedItem().toString()+"", Toast.LENGTH_SHORT).show();

*/

//        editor.putString(USER_PASSWORD,pass1.getText().toString());
        return true;
    }

//    private boolean testHuors(String selectedItem, String selectedItem1) {
//        selectedItem.
//        return false;
//    }
}


