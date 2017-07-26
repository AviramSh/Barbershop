package com.esh_tech.aviram.barbershop.views;

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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.CustomersDBConstants;
import com.esh_tech.aviram.barbershop.Constants.SharedPreferencesConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.data.*;
import com.esh_tech.aviram.barbershop.R;


import java.util.ArrayList;

public class CustomerActivity extends AppCompatActivity implements View.OnLongClickListener ,View.OnClickListener{

    private static final int GALLERY_REQUEST_CODE = 3;
    private static final int CAMERA_REQUEST_CODE = 4;
    //    Database
    BarbershopDBHandler dbHandler;

    Customer customerProfile;
    ImageView customerPic;
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
        //        database
        dbHandler = new BarbershopDBHandler(this);
        customerProfile = new Customer();

        int id = getIntent().getIntExtra("customerId",-1);
        customerProfile = dbHandler.getCustomerByID(id);
        etTel =(TextView)findViewById(R.id.tvCustomerPhone);
        setOnClickPictures();

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

            if(!dbHandler.getAllPicturesByUserID(customerProfile.get_id()).isEmpty()) {
                setCustomerPics();
            }
        }

    }

    private void setOnClickPictures() {
        ivCustomerProfile = (ImageView)findViewById(R.id.customerPic_1);
        ivCustomerProfile.setOnClickListener(this);
        ivCustomerProfile.setOnLongClickListener(this);
        ivCustomerProfile = (ImageView)findViewById(R.id.customerPic_2);
        ivCustomerProfile.setOnClickListener(this);
        ivCustomerProfile.setOnLongClickListener(this);
        ivCustomerProfile = (ImageView)findViewById(R.id.customerPic_3);
        ivCustomerProfile.setOnClickListener(this);
        ivCustomerProfile.setOnLongClickListener(this);
        ivCustomerProfile = (ImageView)findViewById(R.id.customerPic_4);
        ivCustomerProfile.setOnClickListener(this);
        ivCustomerProfile.setOnLongClickListener(this);
        ivCustomerProfile = (ImageView)findViewById(R.id.customerMainPic);
        ivCustomerProfile.setOnClickListener(this);
        ivCustomerProfile.setOnLongClickListener(this);
    }

    private void setCustomerPics() {

        ArrayList<Bitmap> allCustomerPics =dbHandler.getAllPicturesByUserID(customerProfile.get_id());

        if (!allCustomerPics.isEmpty()) {

            for (int i=0 ;i<allCustomerPics.size()&&i<5;i++){
                switch (i){
                    case 0:
                        customerPic = (ImageView) findViewById(R.id.customerMainPic);
                        customerPic.setImageBitmap(allCustomerPics.get(0));
                        break;
                    case 1:
                        customerPic = (ImageView) findViewById(R.id.customerPic_1);
                        customerPic.setImageBitmap(allCustomerPics.get(1));
                        break;
                    case 2:
                        customerPic = (ImageView) findViewById(R.id.customerPic_2);
                        customerPic.setImageBitmap(allCustomerPics.get(2));
                        break;
                    case 3:
                        customerPic = (ImageView) findViewById(R.id.customerPic_3);
                        customerPic.setImageBitmap(allCustomerPics.get(3));
                        break;
                    case 4:
                        customerPic = (ImageView) findViewById(R.id.customerPic_4);
                        customerPic.setImageBitmap(allCustomerPics.get(4));
                        break;

                    default:
                        Toast.makeText(this, "No Pics", Toast.LENGTH_SHORT).show();
                        break;
                }
            }





        }else{
            Toast.makeText(this, "List is Empty", Toast.LENGTH_SHORT).show();
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

        Intent myIntent;
        switch (v.getId()){
            case R.id.btMassage:
                myIntent = new Intent(this ,SendMessageActivity.class);
                myIntent.putExtra(SharedPreferencesConstants.CUSTOMER_ID_SMS,customerProfile.get_id());
                startActivity(myIntent);

                break;
            case R.id.btCallCustomer:
                myIntent = new Intent(Intent.ACTION_DIAL);
                myIntent.setData(Uri.parse("tel:" + customerProfile.getPhone()));
                startActivity(myIntent);
                break;
            case R.id.customerMainPic:
//                customerPic = (ImageButton)findViewById(R.id.customerMainPic);
                showUserPhoto(R.id.customerMainPic,0);
                break;
            case R.id.customerPic_1:
//                customerPic = (ImageButton)findViewById(R.id.customerPic_1);
                showUserPhoto(R.id.customerPic_1,1);
                break;
            case R.id.customerPic_2:
//                customerPic = (ImageButton)findViewById(R.id.customerPic_2);
                showUserPhoto(R.id.customerPic_2,2);
                break;
            case R.id.customerPic_3:
//                customerPic = (ImageButton)findViewById(R.id.customerPic_3);
                showUserPhoto(R.id.customerPic_3,3);
                break;
            case R.id.customerPic_4:
//                customerPic = (ImageButton)findViewById(R.id.customerPic_4);
                showUserPhoto(R.id.customerPic_4,4);
//                Toast.makeText(this, "Pic 4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ibEditCustomer:
                myIntent = new Intent(this,NewCustomerActivity.class);
                myIntent.putExtra(CustomersDBConstants.CUSTOMER_ID,customerProfile.get_id());
                startActivity(myIntent);
                this.finish();

                break;
            case R.id.btClose:
                this.finish();
                break;
            default:
                customerPic = (ImageView) findViewById(R.id.customerMainPic);
                Toast.makeText(this, R.string.not_initialized_yet, Toast.LENGTH_LONG).show();
                break;
        }
    }
    @Override
    public boolean onLongClick(View v) {
        Toast.makeText(this, "LongClick", Toast.LENGTH_SHORT).show();
        switch (v.getId()) {

            case R.id.customerMainPic:
//                customerPic = (ImageButton)findViewById(R.id.customerMainPic);
                userPhotoSet(R.id.customerMainPic);
                break;
            case R.id.customerPic_1:
//                customerPic = (ImageButton)findViewById(R.id.customerPic_1);
                userPhotoSet(R.id.customerPic_1);
                break;
            case R.id.customerPic_2:
//                customerPic = (ImageButton)findViewById(R.id.customerPic_2);
                userPhotoSet(R.id.customerPic_2);
                break;
            case R.id.customerPic_3:
//                customerPic = (ImageButton)findViewById(R.id.customerPic_3);
                userPhotoSet(R.id.customerPic_3);
                break;
            case R.id.customerPic_4:
//                customerPic = (ImageButton)findViewById(R.id.customerPic_4);
                userPhotoSet(R.id.customerPic_4);
//                Toast.makeText(this, "Pic 4", Toast.LENGTH_SHORT).show();
                break;

            default:
                customerPic = (ImageView) findViewById(R.id.customerMainPic);
                Toast.makeText(this, "Not Initialized", Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    private void showUserPhoto(int customerPicId ,int position) {

        customerPic = (ImageView) findViewById(customerPicId);
//
//        Dialog settingsDialog = new Dialog(this);
//        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//
//
//        settingsDialog.setContentView(getLayoutInflater().inflate(R.layout.custom_image_layout,null));
//        settingsDialog.show();

        AlertDialog.Builder alertadd = new AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);
        final View view =factory.inflate(R.layout.custom_image_layout,null);

        ArrayList<Bitmap> allCustomerPics =dbHandler.getAllPicturesByUserID(customerProfile.get_id());
        if (!allCustomerPics.isEmpty()) {
            customerPic = (ImageView) view.findViewById(R.id.dialogImageView);
            try {
                customerPic.setImageBitmap(allCustomerPics.get(position));
            } catch (Exception e) {
                Toast.makeText(this, R.string.picture_not_exist, Toast.LENGTH_SHORT).show();
            }


        }

        alertadd.setView(view);
        alertadd.setNeutralButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertadd.show();

    }

    private void userPhotoSet(int customerPicId) {
        customerPic = (ImageView) findViewById(customerPicId);

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
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case GALLERY_REQUEST_CODE:

                break;

            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    selectedProfilePicture = (Bitmap)data.getExtras().get("data");
                    customerPic.setImageBitmap(selectedProfilePicture);
                    if(dbHandler.addPicture(customerProfile.get_id(),selectedProfilePicture)){
                        Toast.makeText(this, R.string.picture_saved, Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, R.string.picture_didnt_saved, Toast.LENGTH_SHORT).show();
                    }

                }
                break;

            default:

                break;
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
}
