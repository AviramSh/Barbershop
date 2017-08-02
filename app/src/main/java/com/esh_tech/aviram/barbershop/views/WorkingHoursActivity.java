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

import com.esh_tech.aviram.barbershop.Constants.SharedPreferencesConstants;
import com.esh_tech.aviram.barbershop.R;

import java.util.Calendar;

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
        settings = PreferenceManager.getDefaultSharedPreferences(this);

        cbDay = (CheckBox)findViewById(R.id.sundayCb);
        sStart = (Spinner)findViewById(R.id.sundaySspin);
        sEnd = (Spinner)findViewById(R.id.sundayEspin);



    }

    public void timeAndFee(View view) {

        testData();

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
        /*Toast.makeText(this,
                settings.getString(SharedPreferencesConstants.SUNDAY_TIME_OPEN,"Err")
                        +" - "+
                        settings.getString(SharedPreferencesConstants.SUNDAY_TIME_CLOSE,"Err")
                        +"\n"+
                        settings.getString(SharedPreferencesConstants.MONDAY_TIME_OPEN,"Err")
                        +" - "+
                        settings.getString(SharedPreferencesConstants.MONDAY_TIME_CLOSE,"Err")
                        +"\n"+
                        settings.getString(SharedPreferencesConstants.TUESDAY_TIME_OPEN,"Err")
                        +" - "+
                        settings.getString(SharedPreferencesConstants.TUESDAY_TIME_CLOSE,"Err")
                        +"\n"+
        "", Toast.LENGTH_SHORT).show();*/

    }

    private void testData() {
        Calendar myCalendar;
        editor = settings.edit();


        cbDay = (CheckBox)findViewById(R.id.sundayCb);
        if(cbDay.isChecked()) {
            setUpDaysAndHours(R.id.sundaySspin,R.id.sundayEspin,
                    SharedPreferencesConstants.SUNDAY_TIME_OPEN,
                    SharedPreferencesConstants.SUNDAY_TIME_CLOSE);
        }

        cbDay = (CheckBox)findViewById(R.id.mondayCb);
        if(cbDay.isChecked()) {
            setUpDaysAndHours(R.id.mondaySspin,R.id.mondayEspin,
                    SharedPreferencesConstants.MONDAY_TIME_OPEN,
                    SharedPreferencesConstants.MONDAY_TIME_CLOSE);
        }

        cbDay = (CheckBox)findViewById(R.id.tuesdayCb);
        if(cbDay.isChecked()) {
            setUpDaysAndHours(R.id.tuesdaySspin, R.id.tuesdayEspin,
                    SharedPreferencesConstants.TUESDAY_TIME_OPEN,
                    SharedPreferencesConstants.TUESDAY_TIME_CLOSE);
        }


        cbDay = (CheckBox)findViewById(R.id.wednesdayCb);
        if(cbDay.isChecked()) {
            setUpDaysAndHours(R.id.wednesdaySspin, R.id.wednesdayEspin,
                    SharedPreferencesConstants.WEDNESDAY_TIME_OPEN,
                    SharedPreferencesConstants.WEDNESDAY_TIME_CLOSE);
        }

        cbDay = (CheckBox)findViewById(R.id.thursdayCb);
        if(cbDay.isChecked()) {
            setUpDaysAndHours(R.id.thursdaySspin, R.id.thursdayEspin,
                    SharedPreferencesConstants.THURSDAY_TIME_OPEN,
                    SharedPreferencesConstants.THURSDAY_TIME_CLOSE);
        }

        cbDay = (CheckBox)findViewById(R.id.fridayCb);
        if(cbDay.isChecked()) {
            setUpDaysAndHours(R.id.fridaySspin, R.id.fridayEspin,
                    SharedPreferencesConstants.FRIDAY_TIME_OPEN,
                    SharedPreferencesConstants.FRIDAY_TIME_CLOSE);
        }

        cbDay = (CheckBox)findViewById(R.id.saturdayCb);
        if(cbDay.isChecked()) {
            setUpDaysAndHours(R.id.saturdaySspin, R.id.saturdayEspin,
                    SharedPreferencesConstants.SATURDAY_TIME_OPEN,
                    SharedPreferencesConstants.SATURDAY_TIME_CLOSE);
        }


        editor.apply();
    }

    private boolean setUpDaysAndHours(int sId,int eId,String sLabel ,String eLabel) {

        sStart = (Spinner) findViewById(sId);
        sEnd = (Spinner) findViewById(eId);

        if(sStart.getSelectedItemId() < sEnd.getSelectedItemId()){
            editor.putString(
                    sLabel,
                    sStart.getSelectedItem().toString());
            editor.putString(
                    eLabel ,
                    sEnd.getSelectedItem().toString());
        }else{
            editor.putString(
                    eLabel,
                    sStart.getSelectedItem().toString());
            editor.putString(
                    sLabel ,
                    sEnd.getSelectedItem().toString());
        }

        return false;
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


