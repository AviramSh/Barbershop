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
import com.esh_tech.aviram.barbershop.Utils.DateUtils;

import java.util.Calendar;

public class WorkingHoursActivity extends AppCompatActivity {

    //    SharedPreferences
    SharedPreferences settings;
    SharedPreferences.Editor editor;

//    init

    CheckBox cbDay;
    Spinner sStart;
    Spinner sEnd;

    Calendar myCalendar;

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


        myCalendar = Calendar.getInstance();


    }

    public void timeAndFee(View view) {

        testData();

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean register = settings.getBoolean(USER_IS_REGISTER, false);
        editor = settings.edit();

        if(setWorkingHours(view)) {
            if (register) {
//                this.finish();
            } else {
//                Intent timeFeeActivity = new Intent(this, TimeAndFee.class);
//                startActivity(timeFeeActivity);
//                this.finish();
            }
        }
    }

    private void testData() {

        editor = settings.edit();

        cbDay = (CheckBox)findViewById(R.id.sundayCb);
        if(cbDay.isChecked()) {

            sStart = (Spinner) findViewById(R.id.sundaySspin);
            sEnd = (Spinner) findViewById(R.id.sundayEspin);

            if(sStart.getSelectedItem().equals(getResources().getString(R.string.rest))||
                    sEnd.getSelectedItem().equals(getResources().getString(R.string.rest))){
                saveDate(SharedPreferencesConstants.SUNDAY_TIME_OPEN,SharedPreferencesConstants.SUNDAY_TIME_CLOSE,
                        "","");
            }else{

                saveDate(SharedPreferencesConstants.SUNDAY_TIME_OPEN,SharedPreferencesConstants.SUNDAY_TIME_CLOSE,
                        (String)sStart.getSelectedItem(),(String)sEnd.getSelectedItem());

            }


        }


        cbDay = (CheckBox)findViewById(R.id.mondayCb);

        if(cbDay.isChecked()) {

            sStart = (Spinner) findViewById(R.id.mondaySspin);
            sEnd = (Spinner) findViewById(R.id.mondayEspin);



        }


        cbDay = (CheckBox)findViewById(R.id.tuesdayCb);
        if(cbDay.isChecked()) {


            sStart = (Spinner) findViewById(R.id.tuesdaySspin);
            sEnd = (Spinner) findViewById(R.id.tuesdayEspin);


        }

        cbDay = (CheckBox)findViewById(R.id.wednesdayCb);
        if(cbDay.isChecked()) {


            sStart = (Spinner) findViewById(R.id.wednesdaySspin);
            sEnd = (Spinner) findViewById(R.id.wednesdayEspin);

        }



        cbDay = (CheckBox)findViewById(R.id.thursdayCb);
        if(cbDay.isChecked()) {


            sStart = (Spinner) findViewById(R.id.thursdaySspin);
            sEnd = (Spinner) findViewById(R.id.thursdayEspin);



        }

        cbDay = (CheckBox)findViewById(R.id.fridayCb);
        if(cbDay.isChecked()) {

            sStart = (Spinner) findViewById(R.id.fridaySspin);
            sEnd = (Spinner) findViewById(R.id.fridayEspin);

        }

        cbDay = (CheckBox)findViewById(R.id.saturdayCb);
        if(cbDay.isChecked()) {


            sStart = (Spinner) findViewById(R.id.saturdaySspin);
            sEnd = (Spinner) findViewById(R.id.saturdayEspin);


        }
        editor.apply();

//
//        cbDay = (CheckBox)findViewById(R.id.mondayCb);
//        if(cbDay.isChecked()) {
//            setUpDaysAndHours(R.id.mondaySspin,R.id.mondayEspin,
//                    SharedPreferencesConstants.MONDAY_TIME_OPEN,
//                    SharedPreferencesConstants.MONDAY_TIME_CLOSE);
//        }
//
//        cbDay = (CheckBox)findViewById(R.id.tuesdayCb);
//        if(cbDay.isChecked()) {
//            setUpDaysAndHours(R.id.tuesdaySspin, R.id.tuesdayEspin,
//                    SharedPreferencesConstants.TUESDAY_TIME_OPEN,
//                    SharedPreferencesConstants.TUESDAY_TIME_CLOSE);
//        }
//
//
//        cbDay = (CheckBox)findViewById(R.id.wednesdayCb);
//        if(cbDay.isChecked()) {
//            setUpDaysAndHours(R.id.wednesdaySspin, R.id.wednesdayEspin,
//                    SharedPreferencesConstants.WEDNESDAY_TIME_OPEN,
//                    SharedPreferencesConstants.WEDNESDAY_TIME_CLOSE);
//        }
//
//        cbDay = (CheckBox)findViewById(R.id.thursdayCb);
//        if(cbDay.isChecked()) {
//            setUpDaysAndHours(R.id.thursdaySspin, R.id.thursdayEspin,
//                    SharedPreferencesConstants.THURSDAY_TIME_OPEN,
//                    SharedPreferencesConstants.THURSDAY_TIME_CLOSE);
//        }
//
//        cbDay = (CheckBox)findViewById(R.id.fridayCb);
//        if(cbDay.isChecked()) {
//            setUpDaysAndHours(R.id.fridaySspin, R.id.fridayEspin,
//                    SharedPreferencesConstants.FRIDAY_TIME_OPEN,
//                    SharedPreferencesConstants.FRIDAY_TIME_CLOSE);
//        }
//
//        cbDay = (CheckBox)findViewById(R.id.saturdayCb);
//        if(cbDay.isChecked()) {
//            setUpDaysAndHours(R.id.saturdaySspin, R.id.saturdayEspin,
//                    SharedPreferencesConstants.SATURDAY_TIME_OPEN,
//                    SharedPreferencesConstants.SATURDAY_TIME_CLOSE);
//        }
//
//
//        editor.apply();
    }

    private boolean saveDate(String labelOpen,String labelClose,
                          String openTime,String closeTime) {
        
//        if(openTime.equals(getResources().getString(R.string.rest))|| closeTime.equals(getResources().getString(R.string.rest))) {
//            Toast.makeText(this, "Rest day", Toast.LENGTH_SHORT).show();
//            return true;
//        }
        if(openTime.equals("")|| closeTime.equals("")) {
            Toast.makeText(this, "Rest day", Toast.LENGTH_SHORT).show();
            return true;
        }
        Calendar cOpen = Calendar.getInstance();
        Calendar cClose = Calendar.getInstance();

        String[] separatedOpenTime = openTime.split(":");

        String[] separatedCloseTime = closeTime.split(":");

        cOpen.set(Calendar.HOUR_OF_DAY,Integer.parseInt(separatedOpenTime[0]));
        cOpen.set(Calendar.MINUTE,Integer.parseInt(separatedOpenTime[1]));

        cClose.set(Calendar.HOUR_OF_DAY,Integer.parseInt(separatedCloseTime[0]));
        cClose.set(Calendar.MINUTE,Integer.parseInt(separatedCloseTime[1]));


        Toast.makeText(this, DateUtils.getFullSDF(cOpen)+" - "+DateUtils.getFullSDF(cClose), Toast.LENGTH_SHORT).show();
        editor = settings.edit();




        editor.apply();
        return true;
    }

//    private boolean setUpDaysAndHours(int sId,int eId,String sLabel ,String eLabel) {
//
//        sStart = (Spinner) findViewById(sId);
//        sEnd = (Spinner) findViewById(eId);
//
//        if(sStart.getSelectedItemPosition() < sEnd.getSelectedItemPosition()){
//
//            editor.putInt(
//                    sLabel,
//                    sStart.getSelectedItemPosition());
//
//            editor.putInt(
//                    eLabel ,
//                    sEnd.getSelectedItemPosition());
//        }else{
//            editor.putInt(
//                    eLabel,
//                    sStart.getSelectedItemPosition());
//            editor.putInt(
//                    sLabel ,
//                    sEnd.getSelectedItemPosition());
//        }
//
//        return false;
//    }


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



