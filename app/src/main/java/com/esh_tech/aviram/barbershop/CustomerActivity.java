package com.esh_tech.aviram.barbershop;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.CustomersDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

public class CustomerActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int GALLERY_REQUEST_CODE = 3;
    private static final int CAMERA_REQUEST_CODE = 4;
    //    Database
    BarbershopDBHandler dbHandler;

    Customer customerProfile;
    ImageButton customerPic;
    Bitmap selectedProfilePicture;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageView ivCustomerProfile;
    TextView etTel;

    TextView tvCustomerName;
    TextView tvCustomerPhone;
    TextView tvCustomerEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        init();

    }

    private void init() {
        this.setTitle(R.string.customerProfile);
        //        database
        dbHandler = new BarbershopDBHandler(this);
        customerProfile = new Customer();

        int id = getIntent().getIntExtra("customerId",-1);
        customerProfile = dbHandler.getCustomerByID(id);

        etTel =(TextView)findViewById(R.id.tvCustomerPhone);

        ivCustomerProfile = (ImageView)findViewById(R.id.customerMainPic);


//        Disable the camera if the user has no camera.
        if(!hasCamera()){
            setOffAlbum();
        }

//        set Customer
        tvCustomerName = (TextView)findViewById(R.id.tvCustomerName);
        tvCustomerPhone = (TextView)findViewById(R.id.tvCustomerPhone);
        tvCustomerEmail = (TextView)findViewById(R.id.tvCustomerEmail);

        if (customerProfile.get_id()!=-1){
            tvCustomerName.setText(customerProfile.getName());
            tvCustomerPhone.setText(customerProfile.getPhone());
            tvCustomerEmail.setText(customerProfile.getEmail());
        }
    }

    private void noCustomer() {
        AlertDialog alertDialog = new AlertDialog.Builder(CustomerActivity.this).create();

        alertDialog.setTitle(getResources().getString(R.string.error));
        alertDialog.setMessage(getResources().getString(R.string.noCustonerNotFoundDialog));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent myIntent = new Intent(CustomerActivity.this,MainActivity.class);
                        startActivity(myIntent);
                        CustomerActivity.this.finish();
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btMassage:
                userNewMessage();
                break;
            case R.id.btCallCustomer:
                callCustomer();
                break;
            case R.id.customerMainPic:
                userPhotoSet(R.id.customerMainPic);
                customerPic = (ImageButton)findViewById(R.id.customerMainPic);
                Toast.makeText(this,"Profile pic", Toast.LENGTH_SHORT).show();
                break;
            case R.id.customerPic_1:
                customerPic = (ImageButton)findViewById(R.id.customerPic_1);
                Toast.makeText(this, "Pic 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.customerPic_2:
                customerPic = (ImageButton)findViewById(R.id.customerPic_2);
                Toast.makeText(this, "Pic 2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.customerPic_3:
                customerPic = (ImageButton)findViewById(R.id.customerPic_3);
                Toast.makeText(this, "Pic 3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.customerPic_4:
                customerPic = (ImageButton)findViewById(R.id.customerPic_4);
                Toast.makeText(this, "Pic 4", Toast.LENGTH_SHORT).show();
                break;
            default:
                customerPic = (ImageButton)findViewById(R.id.customerMainPic);
                Toast.makeText(this, "Not Initialized", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void userPhotoSet(int customerPic) {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    0);
        }else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case GALLERY_REQUEST_CODE:

                break;

            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    selectedProfilePicture = (Bitmap)data.getExtras().get("data");
                    customerPic.setImageBitmap(selectedProfilePicture);
                }
                break;

            default:

                break;
        }
    }

    private void callCustomer() {
        String userPhone =etTel.getText().toString();
        if(userPhone.length()>8) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + userPhone));
            startActivity(callIntent);
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

    public void userNewMessage() {

        Intent myIntent = new Intent(this ,SandMessageActivity.class);
        myIntent.putExtra("userPhone",etTel.getText().toString());
        startActivity(myIntent);


    }

}
