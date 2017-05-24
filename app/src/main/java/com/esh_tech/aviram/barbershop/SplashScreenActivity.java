package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
        loding = (ProgressBar) findViewById(R.id.progressBar);
        loding.computeScroll();
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);

//                    Checking if user are exist and have checked auto login.
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(SplashScreenActivity.this);
                    String username = settings.getString("username", "");
                    boolean savePassword = settings.getBoolean("savePassword", false);

                    boolean register = settings.getBoolean("register", true);

                    if (username != null && !username.equals("") && savePassword && register) {
                        //Login with User password
                        SplashScreenActivity.this.finish();
                        Intent myIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(myIntent);

                    } else if(register){
                        SplashScreenActivity.this.finish();
                        Intent myIntent = new Intent(SplashScreenActivity.this, UserRegistrationActivity.class);
                        startActivity(myIntent);
                    }else {
                        Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(myIntent);
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }

}
