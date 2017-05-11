package com.esh_tech.aviram.barbershop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BalanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        this.setTitle(R.string.balance);
    }

    public void closeBalance(View view) {
        this.finish();
    }
}
