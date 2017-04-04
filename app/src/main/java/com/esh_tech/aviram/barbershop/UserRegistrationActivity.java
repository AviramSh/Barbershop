package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.esh_tech.aviram.barbershop.R;

public class UserRegistrationActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);


    }

    public void closeScreen(View view) {
        finish();
    }

    public void passwordActivityNext(View view) {

        Intent passwordIntent = new Intent(this ,UserPasswordActivity.class);
        startActivity(passwordIntent);
        this.finish();
    }
}
