package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BarbershopActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barbershop);

        init();
    }

    private void init() {
//        TODO add change settings with Shared preferences

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btBack){
            Intent intent= new Intent(this,SettingsActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
}
