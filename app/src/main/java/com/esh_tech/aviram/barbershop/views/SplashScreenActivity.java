package com.esh_tech.aviram.barbershop.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.R;
import com.esh_tech.aviram.barbershop.data.Customer;

import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.*;


public class SplashScreenActivity extends AppCompatActivity {

    BarbershopDBHandler dbHandler;
    ProgressBar loding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        this.setTitle(R.string.title_splash_activity);
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
                    String username = settings.getString(USER_NAME, "");
                    boolean savePassword = settings.getBoolean(USER_AUTO_LOGIN, false);

                    boolean register = settings.getBoolean(USER_IS_REGISTER, false);

                    if (!username.equals("") && savePassword && register) {
                        //Login with User password
                        //    Database
                        /*dbHandler =new BarbershopDBHandler(SplashScreenActivity.this);
                        dbHandler.addCustomer(new Customer());*/

                        Intent myIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(myIntent);
                        SplashScreenActivity.this.finish();

                    } else if(!register){
                        Intent myIntent = new Intent(SplashScreenActivity.this, UserRegistrationActivity.class);
                        startActivity(myIntent);
                        SplashScreenActivity.this.finish();
                    }else if(register && !savePassword){
                        Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(myIntent);
                        SplashScreenActivity.this.finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }

}
