package com.esh_tech.aviram.barbershop.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.R;

import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.*;


public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    BarbershopDBHandler dbHandler;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        lodData();
    }

    private void lodData() {
        this.setTitle(R.string.title_splash_activity);
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        loading = (ProgressBar) findViewById(R.id.progressBar);
        loading.computeScroll();
        setUpDefaultData();

        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);

//                    Checking if user are exist and have checked auto login.

                    String username = settings.getString(USER_NAME, "");
                    boolean savePassword = settings.getBoolean(USER_AUTO_LOGIN, false);

                    boolean register = settings.getBoolean(USER_IS_REGISTER, false);

                    if (!username.equals("") && savePassword && register) {
                        //Login with User password
                        //    Database
//                        dbHandler =new BarbershopDBHandler(SplashScreenActivity.this);
//                        dbHandler.addCustomer(new Customer());

                        Intent myIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
                        startActivity(myIntent);
                        SplashScreenActivity.this.finish();

                    } else if (!register) {
                        Intent myIntent = new Intent(SplashScreenActivity.this, UserRegistrationActivity.class);
                        startActivity(myIntent);
                        SplashScreenActivity.this.finish();
                    } else if (register && !savePassword) {
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

    private void setUpDefaultData() {

        editor = settings.edit();

        editor.putString(USER_NAME, "אורן");
        editor.putString(USER_LAST_NAME, "לוי");
        editor.putString(USER_PHONE, "0506792353");
        editor.putString(USER_BUSINESS_NAME, "אורן עיצוב");
        editor.putString(USER_BUSINESS_PHONE, "039000000");
        editor.putString(USER_BUSINESS_ADDRESS, "מרבד הקסמים 1 ראש העין");

        editor.putString(USER_PASSWORD, "1234");
        editor.putBoolean(USER_AUTO_LOGIN, true);
        editor.putBoolean(USER_IS_REGISTER, true);

        editor.putInt(USER_FEMALE_HAIRCUT_TIME, 45);
        editor.putInt(USER_MALE_HAIRCUT_PRICE, 35);
        editor.putInt(USER_MALE_HAIRCUT_TIME, 35);
        editor.putInt(USER_FEMALE_HAIRCUT_PRICE, 45);
        editor.putInt(USER_FEMALE_HAIRCUT_TIME, 45);

        editor.putString(UserDBConstants.USER_EMAIL, "aviram.note@gmail.com");
        editor.putString(UserDBConstants.USER_EMAIL_PASSWORD, "avi304287");

        editor.apply();
    }
}

