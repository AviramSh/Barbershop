package com.esh_tech.aviram.barbershop;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomerActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView ivCustomerProfile;
    TextView etTel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        etTel =(TextView)findViewById(R.id.tvUserPhone);

        ivCustomerProfile = (ImageView)findViewById(R.id.customerMainPic);

//        Disable the camera if the user has no camera.
        if(!hasCamera()){
            setOffAlbum();
        }

    }

    private void setOffAlbum() {
        ivCustomerProfile.setEnabled(false);
        findViewById(R.id.customerPic_1).setEnabled(false);
        findViewById(R.id.customerPic_2).setEnabled(false);
        findViewById(R.id.customerPic_3).setEnabled(false);
        findViewById(R.id.customerPic_4).setEnabled(false);
    }

    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public void userNewMessage(View view) {

        Intent myIntent = new Intent(this ,SandMessageActivity.class);
        myIntent.putExtra("userPhone",etTel.getText().toString());
        startActivity(myIntent);


    }

    public void addCustomerPic(View view) {

        switch (view.getId()){
            case R.id.customerMainPic:
                Toast.makeText(this,"Profile pic", Toast.LENGTH_SHORT).show();
                break;
            case R.id.customerPic_1:
                Toast.makeText(this, "Pic 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.customerPic_2:
                Toast.makeText(this, "Pic 2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.customerPic_3:
                Toast.makeText(this, "Pic 3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.customerPic_4:
                Toast.makeText(this, "Pic 4", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, R.string.error, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
