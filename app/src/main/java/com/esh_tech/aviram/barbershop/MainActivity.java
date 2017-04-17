package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextClock;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settime();
    }

    private void settime() {

    }

    public void openCustomersList(View view) {
        Intent myIntent = new Intent(this ,CustomersListActivity.class);
        startActivity(myIntent);

    }

    public void openBalance(View view) {
        Intent myIntent = new Intent(this ,BalanceActivity.class);
        startActivity(myIntent);
    }

    public void openStock(View view) {
        Intent myIntent = new Intent(this ,StockActivity.class);
        startActivity(myIntent);
    }

    public void openAppointmentList(View view) {
        Intent myIntent = new Intent(this ,AppointmentListActivity.class);
        startActivity(myIntent);
    }

    public void openSettings(View view) {
        Intent myIntent = new Intent(this ,SettingsActivity.class);
        startActivity(myIntent);
    }

    public void openNewAppointment(View view) {
        Intent myIntent = new Intent(this ,NewAppointmentActivity.class);
        startActivity(myIntent);
    }
}
