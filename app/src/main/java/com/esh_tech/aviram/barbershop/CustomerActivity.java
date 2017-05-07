package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class CustomerActivity extends AppCompatActivity {

    TextView etTel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        etTel =(TextView)findViewById(R.id.tvUserPhone);
    }

    public void userNewMessage(View view) {

        Intent myIntent = new Intent(this ,SandMessageActivity.class);
        myIntent.putExtra("userPhone",etTel.getText().toString());
        startActivity(myIntent);


    }
}
