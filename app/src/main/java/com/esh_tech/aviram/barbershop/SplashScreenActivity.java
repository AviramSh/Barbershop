package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashScreenActivity extends AppCompatActivity {

    ProgressBar loding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        lodData();
    }

    private void lodData() {
        loding =(ProgressBar)findViewById(R.id.progressBar);
        loding.computeScroll();
        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent myIntent = new Intent(getApplicationContext(),UserRegistrationActivity.class);
                    startActivity(myIntent);
                    finish();

                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();


    }


}
