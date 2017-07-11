package com.esh_tech.aviram.barbershop.views;


import android.support.v7.app.AppCompatActivity;
import com.esh_tech.aviram.barbershop.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener{

    Button btClose;
    TextView tvAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        init();
    }

    private void init() {

        btClose = (Button)findViewById(R.id.btClose);
        tvAbout = (TextView)findViewById(R.id.tvAboutContent);

        btClose.setOnClickListener(this);
        tvAbout.setText(getResources().getText(R.string.aboutActivityContent));


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btClose){
            this.finish();
        }
    }
}
