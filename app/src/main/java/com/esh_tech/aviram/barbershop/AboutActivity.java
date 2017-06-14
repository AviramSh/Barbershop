package com.esh_tech.aviram.barbershop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener{

    Button btClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        init();
    }

    private void init() {
        this.setTitle(R.string.about);

        btClose =(Button)findViewById(R.id.btClose);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btSave){
            this.finish();
        }
    }
}