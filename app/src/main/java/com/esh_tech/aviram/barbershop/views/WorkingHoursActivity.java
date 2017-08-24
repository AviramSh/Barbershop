package com.esh_tech.aviram.barbershop.views;
//import com.esh_tech.aviram.barbershop.Codes.Barbershop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.USER_IS_REGISTER;

import com.esh_tech.aviram.barbershop.Constants.SharedPreferencesConstants;
import com.esh_tech.aviram.barbershop.R;

import java.util.ArrayList;
import java.util.Calendar;

public class WorkingHoursActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    //    SharedPreferences
    SharedPreferences settings;
    SharedPreferences.Editor editor;

//    init

    ImageButton save;
    CheckBox cbDay;
    ArrayList<Spinner>spinners;
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
        spinners = new ArrayList<Spinner>();
        int i=0;
        settings = PreferenceManager.getDefaultSharedPreferences(this);


        save = (ImageButton)findViewById(R.id.btSave);
        cbDay = (CheckBox)findViewById(R.id.sundayCb);

        sStart = (Spinner)findViewById(R.id.sundaySspin);
        sEnd = (Spinner)findViewById(R.id.sundayEspin);


        spinners.add((Spinner)findViewById(R.id.sundaySspin));
        spinners.add((Spinner)findViewById(R.id.sundayEspin));

        spinners.add((Spinner)findViewById(R.id.mondaySspin));
        spinners.add((Spinner)findViewById(R.id.mondayEspin));

        spinners.add((Spinner)findViewById(R.id.tuesdaySspin));
        spinners.add((Spinner)findViewById(R.id.tuesdayEspin));

        spinners.add((Spinner)findViewById(R.id.wednesdaySspin));
        spinners.add((Spinner)findViewById(R.id.wednesdayEspin));

        spinners.add((Spinner)findViewById(R.id.thursdaySspin));
        spinners.add((Spinner)findViewById(R.id.thursdayEspin));

        spinners.add((Spinner)findViewById(R.id.fridaySspin));
        spinners.add((Spinner)findViewById(R.id.fridayEspin));

        spinners.add((Spinner)findViewById(R.id.saturdaySspin));
        spinners.add((Spinner)findViewById(R.id.saturdayEspin));


        for (Spinner index:spinners) {
            index.setOnItemSelectedListener(this);
//            index.setOnItemClickListener(this);
        }



        myCalendar = Calendar.getInstance();
        save.setOnClickListener(this);

        if(settings.getBoolean(USER_IS_REGISTER,false)){
            setData();
        }

    }

    private void setData() {

        setHour(SharedPreferencesConstants.SUNDAY_TIME_OPEN,0,(CheckBox)findViewById(R.id.sundayCb));

        setHour(SharedPreferencesConstants.MONDAY_TIME_OPEN,2,(CheckBox)findViewById(R.id.mondayCb));

        setHour(SharedPreferencesConstants.TUESDAY_TIME_OPEN,4,(CheckBox)findViewById(R.id.tuesdayCb));

        setHour(SharedPreferencesConstants.WEDNESDAY_TIME_OPEN,6,(CheckBox)findViewById(R.id.wednesdayCb));

        setHour(SharedPreferencesConstants.THURSDAY_TIME_OPEN,8,(CheckBox)findViewById(R.id.thursdayCb));;

        setHour(SharedPreferencesConstants.FRIDAY_TIME_OPEN,10,(CheckBox)findViewById(R.id.fridayCb));

        setHour(SharedPreferencesConstants.SATURDAY_TIME_OPEN,12,(CheckBox)findViewById(R.id.saturdayCb));

    }

    private boolean setHour(String label,int index,CheckBox cbId) {
        int i=0;
        String[] hoursArray = getResources().getStringArray(R.array.spinner_hours);
        String dayHours = settings.getString(label,"");
        String[] splitDayHours = dayHours.split(":");


        cbId.setChecked(false);

        if(!splitDayHours[0].equals("") &&
                !splitDayHours[0].equals(getResources().getString(R.string.rest))){

            Log.d("TAG","New Spinner");

            for (i=0 ; i < hoursArray.length ; i++) {
                Log.d("TAG",i+")First Loop: dayHours:"+dayHours+" contains ==> "+hoursArray[i]);

                if(dayHours.contains(hoursArray[i])) {
                    Log.d("TAG","Start Yes");
                    spinners.get(index).setSelection(i);
                    cbId.setChecked(true);
                    i++;
                    break;

                }

            }

            for (; i < hoursArray.length ; i++){
                Log.d("TAG",i+")Second Loop: dayHours:"+dayHours+" contains ==> "+hoursArray[i]+"-"+hoursArray[i].length());

                if(dayHours.contains(hoursArray[i])){
                    spinners.get(index+1).setSelection(i);
                    Log.d("TAG","End Yes");
                    break;
                }
            }

        }else{
            spinners.get(index).setSelection(0);
        }
        return false;

    }


    private void testData() {
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean register = settings.getBoolean(USER_IS_REGISTER, false);
        editor = settings.edit();


        cbDay = (CheckBox)findViewById(R.id.sundayCb);
        if(cbDay.isChecked()) {

            sStart = (Spinner) findViewById(R.id.sundaySspin);
            sEnd = (Spinner) findViewById(R.id.sundayEspin);

            if(sStart.getSelectedItemPosition()==sEnd.getSelectedItemPosition()&& sStart.getSelectedItemPosition()!=0){
                Toast.makeText(this, R.string.sunday_invalid_time, Toast.LENGTH_SHORT).show();
            }else {
                /*saveDate(SharedPreferencesConstants.SUNDAY_TIME_OPEN, SharedPreferencesConstants.SUNDAY_TIME_CLOSE,
                        (String) sStart.getSelectedItem(), (String) sEnd.getSelectedItem());*/
                saveDate(SharedPreferencesConstants.SUNDAY_TIME_OPEN,
                        (String) sStart.getSelectedItem(), (String) sEnd.getSelectedItem());
            }

        }else{
            saveDate(SharedPreferencesConstants.SUNDAY_TIME_OPEN,
                    getResources().getString(R.string.rest), getResources().getString(R.string.rest));
        }


        cbDay = (CheckBox)findViewById(R.id.mondayCb);

        if(cbDay.isChecked()) {

            sStart = (Spinner) findViewById(R.id.mondaySspin);
            sEnd = (Spinner) findViewById(R.id.mondayEspin);
            if(sStart.getSelectedItemPosition()==sEnd.getSelectedItemPosition()&& sStart.getSelectedItemPosition()!=0){
                Toast.makeText(this, R.string.monday_invalid_time, Toast.LENGTH_SHORT).show();
            }else {
                saveDate(SharedPreferencesConstants.MONDAY_TIME_OPEN,
                        (String) sStart.getSelectedItem(), (String) sEnd.getSelectedItem());
            }
        }else{
            saveDate(SharedPreferencesConstants.MONDAY_TIME_OPEN,
                    getResources().getString(R.string.rest), getResources().getString(R.string.rest));
        }

        cbDay = (CheckBox)findViewById(R.id.tuesdayCb);
        if(cbDay.isChecked()) {

            sStart = (Spinner) findViewById(R.id.tuesdaySspin);
            sEnd = (Spinner) findViewById(R.id.tuesdayEspin);

            if(sStart.getSelectedItemPosition()==sEnd.getSelectedItemPosition()&& sStart.getSelectedItemPosition()!=0){
                Toast.makeText(this, R.string.tuesday_invalid_time, Toast.LENGTH_SHORT).show();
            }else {
                saveDate(SharedPreferencesConstants.TUESDAY_TIME_OPEN,
                        (String) sStart.getSelectedItem(), (String) sEnd.getSelectedItem());
            }
        }else{
            saveDate(SharedPreferencesConstants.TUESDAY_TIME_OPEN,
                    getResources().getString(R.string.rest), getResources().getString(R.string.rest));
        }

        cbDay = (CheckBox)findViewById(R.id.wednesdayCb);
        if(cbDay.isChecked()) {


            sStart = (Spinner) findViewById(R.id.wednesdaySspin);
            sEnd = (Spinner) findViewById(R.id.wednesdayEspin);

            if(sStart.getSelectedItemPosition()==sEnd.getSelectedItemPosition()&& sStart.getSelectedItemPosition()!=0){
                Toast.makeText(this, R.string.wednesday_invalid_time, Toast.LENGTH_SHORT).show();
            }else {
                saveDate(SharedPreferencesConstants.WEDNESDAY_TIME_OPEN,
                        (String) sStart.getSelectedItem(), (String) sEnd.getSelectedItem());
            }
        }else{
            saveDate(SharedPreferencesConstants.WEDNESDAY_TIME_OPEN,
                    getResources().getString(R.string.rest), getResources().getString(R.string.rest));
        }



        cbDay = (CheckBox)findViewById(R.id.thursdayCb);
        if(cbDay.isChecked()) {


            sStart = (Spinner) findViewById(R.id.thursdaySspin);
            sEnd = (Spinner) findViewById(R.id.thursdayEspin);

            if(sStart.getSelectedItemPosition()==sEnd.getSelectedItemPosition()&& sStart.getSelectedItemPosition()!=0){
                Toast.makeText(this, R.string.thursday_invalid_time, Toast.LENGTH_SHORT).show();
            }else {
                saveDate(SharedPreferencesConstants.THURSDAY_TIME_OPEN,
                        (String) sStart.getSelectedItem(), (String) sEnd.getSelectedItem());
            }

        }else{
            saveDate(SharedPreferencesConstants.THURSDAY_TIME_OPEN,
                    getResources().getString(R.string.rest), getResources().getString(R.string.rest));
        }

        cbDay = (CheckBox)findViewById(R.id.fridayCb);
        if(cbDay.isChecked()) {

            sStart = (Spinner) findViewById(R.id.fridaySspin);
            sEnd = (Spinner) findViewById(R.id.fridayEspin);

            if(sStart.getSelectedItemPosition()==sEnd.getSelectedItemPosition() && sStart.getSelectedItemPosition()!=0){
                Toast.makeText(this, R.string.friday_invalid_time, Toast.LENGTH_SHORT).show();
            }else {
                saveDate(SharedPreferencesConstants.FRIDAY_TIME_OPEN,
                        (String) sStart.getSelectedItem(), (String) sEnd.getSelectedItem());
            }

        }else{
            saveDate(SharedPreferencesConstants.FRIDAY_TIME_OPEN,
                    getResources().getString(R.string.rest), getResources().getString(R.string.rest));
        }

        cbDay = (CheckBox)findViewById(R.id.saturdayCb);
        if(cbDay.isChecked()) {
            sStart = (Spinner) findViewById(R.id.saturdaySspin);
            sEnd = (Spinner) findViewById(R.id.saturdayEspin);
            if(sStart.getSelectedItemPosition()==sEnd.getSelectedItemPosition()&& sStart.getSelectedItemPosition()!=0){
                Toast.makeText(this, R.string.saturday_invalid_time, Toast.LENGTH_SHORT).show();
            }else {
                saveDate(SharedPreferencesConstants.SATURDAY_TIME_OPEN,
                        (String) sStart.getSelectedItem(), (String) sEnd.getSelectedItem());
            }
        }else{
            saveDate(SharedPreferencesConstants.SATURDAY_TIME_OPEN,
                    getResources().getString(R.string.rest), getResources().getString(R.string.rest));
        }

        editor.apply();
        if (register) {
                this.finish();
        } else {
                Intent timeFeeActivity = new Intent(this, TimeAndFee.class);
                startActivity(timeFeeActivity);
                this.finish();
        }
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

    private boolean saveDate(String labelOpen,
                          String openTime,String closeTime) {


        if(openTime.equals(getResources().getString(R.string.rest))|| closeTime.equals(getResources().getString(R.string.rest))) {
//            Toast.makeText(this, "Rest day", Toast.LENGTH_SHORT).show();
            editor.putString(labelOpen,"");
//            editor.putString(labelClose,"");
            return true;
        }else{
            editor.putString(labelOpen,openTime+":"+closeTime);
//            editor.putString(labelClose,closeTime);
        }


/*Test data*/
//        /*Calendar cOpen = Calendar.getInstance();
//        Calendar cClose = Calendar.getInstance();
//
//        String[] separatedOpenTime = openTime.split(":");
//
//        String[] separatedCloseTime = closeTime.split(":");
//
//        cOpen.set(Calendar.HOUR_OF_DAY,Integer.parseInt(separatedOpenTime[0]));
//        cOpen.set(Calendar.MINUTE,Integer.parseInt(separatedOpenTime[1]));
//
//        cClose.set(Calendar.HOUR_OF_DAY,Integer.parseInt(separatedCloseTime[0]));
//        cClose.set(Calendar.MINUTE,Integer.parseInt(separatedCloseTime[1]));
//
//
//        Toast.makeText(this, DateUtils.getFullSDF(cOpen)+" - "+DateUtils.getFullSDF(cClose), Toast.LENGTH_SHORT).show();*/

        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btSave:
                testData();
                break;

            default:
                Toast.makeText(this, R.string.not_initialized_yet, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//        Toast.makeText(this, adapterView.getItemIdAtPosition(position)+"Position : "+position, Toast.LENGTH_SHORT).show();
        Spinner spinnerHandler;
//        if(i == 0){
//
//        }

        switch (adapterView.getId()){

            case R.id.sundaySspin:
                spinnerHandler = (Spinner)findViewById(R.id.sundayEspin);
                if(spinnerHandler.getSelectedItemPosition() < position || position == 0){
                    spinnerHandler.setSelection(position);
                }else{
                    if(spinnerHandler.getSelectedItemPosition() == 0)
                        spinnerHandler.setSelection(1);
                }
                break;

            case R.id.sundayEspin:
                spinnerHandler = (Spinner)findViewById(R.id.sundaySspin);
                if(spinnerHandler.getSelectedItemPosition() > position || position == 0){
                    spinnerHandler.setSelection(position);
                }else{
                    if(spinnerHandler.getSelectedItemPosition() == 0)
                        spinnerHandler.setSelection(1);
                }
                break;

            case R.id.mondaySspin:
                spinnerHandler = (Spinner)findViewById(R.id.mondayEspin);
                if(spinnerHandler.getSelectedItemPosition() < position || position == 0){
                    spinnerHandler.setSelection(position);
                }else{
                    if(spinnerHandler.getSelectedItemPosition() == 0)
                        spinnerHandler.setSelection(1);
                }
                break;

            case R.id.mondayEspin:
                spinnerHandler = (Spinner)findViewById(R.id.mondaySspin);
                if(spinnerHandler.getSelectedItemPosition() > position || position == 0){
                    spinnerHandler.setSelection(position);
                }else{
                    if(spinnerHandler.getSelectedItemPosition() == 0)
                        spinnerHandler.setSelection(1);
                }
                break;

            case R.id.tuesdaySspin:
                spinnerHandler = (Spinner)findViewById(R.id.tuesdayEspin);
                if(spinnerHandler.getSelectedItemPosition() < position || position == 0){
                    spinnerHandler.setSelection(position);
                }else{
                    if(spinnerHandler.getSelectedItemPosition() == 0)
                        spinnerHandler.setSelection(1);
                }
                break;

            case R.id.tuesdayEspin:
                spinnerHandler = (Spinner)findViewById(R.id.tuesdaySspin);
                if(spinnerHandler.getSelectedItemPosition() > position || position == 0){
                    spinnerHandler.setSelection(position);
                }else{
                    if(spinnerHandler.getSelectedItemPosition() == 0)
                        spinnerHandler.setSelection(1);
                }
                break;


            case R.id.wednesdaySspin:
                spinnerHandler = (Spinner)findViewById(R.id.wednesdayEspin);
                if(spinnerHandler.getSelectedItemPosition() < position || position == 0){
                    spinnerHandler.setSelection(position);
                }else{
                    if(spinnerHandler.getSelectedItemPosition() == 0)
                        spinnerHandler.setSelection(1);
                }
                break;

            case R.id.wednesdayEspin:
                spinnerHandler = (Spinner)findViewById(R.id.wednesdaySspin);
                if(spinnerHandler.getSelectedItemPosition() > position || position == 0){
                    spinnerHandler.setSelection(position);
                }else{
                    if(spinnerHandler.getSelectedItemPosition() == 0)
                        spinnerHandler.setSelection(1);
                }
                break;

            case R.id.thursdaySspin:
                spinnerHandler = (Spinner)findViewById(R.id.thursdayEspin);
                if(spinnerHandler.getSelectedItemPosition() < position || position == 0){
                    spinnerHandler.setSelection(position);
                }else{
                    if(spinnerHandler.getSelectedItemPosition() == 0)
                        spinnerHandler.setSelection(1);
                }
                break;

            case R.id.thursdayEspin:
                spinnerHandler = (Spinner)findViewById(R.id.thursdaySspin);
                if(spinnerHandler.getSelectedItemPosition() > position || position == 0){
                    spinnerHandler.setSelection(position);
                }else{
                    if(spinnerHandler.getSelectedItemPosition() == 0)
                        spinnerHandler.setSelection(1);
                }
                break;

            case R.id.fridaySspin:
                spinnerHandler = (Spinner)findViewById(R.id.fridayEspin);
                if(spinnerHandler.getSelectedItemPosition() < position || position == 0){
                    spinnerHandler.setSelection(position);
                }else{
                    if(spinnerHandler.getSelectedItemPosition() == 0)
                        spinnerHandler.setSelection(1);
                }
                break;

            case R.id.fridayEspin:
                spinnerHandler = (Spinner)findViewById(R.id.fridaySspin);
                if(spinnerHandler.getSelectedItemPosition() > position || position == 0){
                    spinnerHandler.setSelection(position);
                }else{
                    if(spinnerHandler.getSelectedItemPosition() == 0)
                        spinnerHandler.setSelection(1);
                }
                break;
            case R.id.saturdaySspin:
                spinnerHandler = (Spinner)findViewById(R.id.saturdayEspin);
                if(spinnerHandler.getSelectedItemPosition() < position || position == 0){
                    spinnerHandler.setSelection(position);
                }else{
                    if(spinnerHandler.getSelectedItemPosition() == 0)
                        spinnerHandler.setSelection(1);
                }
                break;

            case R.id.saturdayEspin:
                spinnerHandler = (Spinner)findViewById(R.id.saturdaySspin);
                if(spinnerHandler.getSelectedItemPosition() > position || position == 0){
                    spinnerHandler.setSelection(position);
                }else{
                    if(spinnerHandler.getSelectedItemPosition() == 0)
                        spinnerHandler.setSelection(1);
                }
                break;

            default:
//                Toast.makeText(this, R.string.not_initialized_yet, Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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

//
//    //    Need Work
//    private boolean setWorkingHours(View v) {
///*
//
//        cbDay = (CheckBox)findViewById(R.id.sundayCb);
//        sStart = (Spinner) findViewById(R.id.sundaySspin);
//        sEnd = (Spinner) findViewById(R.id.sundayEspin);
//
//        if(((CheckBox) findViewById(R.id.sundayCb)).isChecked()){
//
//        }
//
//        */
///*if(cbDay.isChecked()) {
//            if(testHuors(sStart.getSelectedItem(),sEnd.getSelectedItem()))
//        }*//*
//
//
//        cbDay = (CheckBox)findViewById(R.id.mondayCb);
//        sStart = (Spinner) findViewById(R.id.mondaySspin);
//        sEnd = (Spinner) findViewById(R.id.mondayEspin);
//
//        if(cbDay.isChecked()) {
//            sStart.getSelectedItem();
//            sEnd.getSelectedItem();
//        }
//
//        Toast.makeText(this, sStart.getSelectedItem().toString()+"", Toast.LENGTH_SHORT).show();
//
//*/
//
////        editor.putString(USER_PASSWORD,pass1.getText().toString());
//        return true;
//    }

//    private boolean testHuors(String selectedItem, String selectedItem1) {
//        selectedItem.
//        return false;
//    }
}




