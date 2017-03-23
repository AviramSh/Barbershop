package com.esh_tech.aviram.barbershop;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.N)
public class NewAppointmentActivity extends AppCompatActivity {

    //Calendar
    final Calendar c = Calendar.getInstance();
    Button theDate;
    int year_x;
    int month_x;
    int day_x;
    static final int DIALOG_ID = 0;

    //List
    ArrayList<String> turns = new ArrayList<String>();
    ArrayAdapter<String> turnsAdapter;
    ListView turnsListView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        setToday();
    }

    private void setToday() {

        year_x = c.get(Calendar.YEAR );
        month_x = c.get(Calendar.MONTH);
        day_x = c.get(Calendar.DAY_OF_MONTH);
        theDate = (Button)findViewById(R.id.button3);
        theDate.setText(day_x+"/"+(month_x+1)+"/"+year_x);

    }
    public void showDialog(View v) {
        showDialog(DIALOG_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id){

        if(id == DIALOG_ID)
            return new DatePickerDialog(this,dpickerListner ,year_x,month_x,day_x);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x =year;
            month_x =month;
            day_x = dayOfMonth;
            theDate = (Button)findViewById(R.id.button3);
            theDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
            Toast.makeText(NewAppointmentActivity.this, ""+year+"/"+(month+1)+"/"+dayOfMonth, Toast.LENGTH_SHORT).show();
        }
    };
}
