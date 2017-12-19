package com.esh_tech.aviram.barbershop.views;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.CustomersDBConstants;
import com.esh_tech.aviram.barbershop.Constants.SharedPreferencesConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.data.*;
import com.esh_tech.aviram.barbershop.R;


import java.util.ArrayList;
import java.util.Calendar;

public class CustomerActivity extends AppCompatActivity implements View.OnLongClickListener ,View.OnClickListener{

    private static final int GALLERY_REQUEST_CODE = 3;
    private static final int CAMERA_REQUEST_CODE = 4;
    //    Database
    BarbershopDBHandler dbHandler;

    Customer customerProfile;
    ImageView tempCustomerPic;
    ImageView ivCustomerProfile;



    Switch mRemainder;

    int picIndex =0;
    ArrayList<Picture> customerAlbum;


    Bitmap selectedProfilePicture;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    TextView etTel;

    TextView tvCustomerName;
    TextView tvCustomerPhone;
    TextView tvCustomerEmail;
    TextView tvCustomerBill;


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
        customerAlbum = new ArrayList<>();


        mRemainder = findViewById(R.id.remainder_switch);


        int id = getIntent().getIntExtra("customerId",-1);

        customerProfile = dbHandler.getCustomerByID(id);
        setOnClickPictures();

//        Disable the camera if the user has no camera.
        if(!hasCamera()){
            setOffAlbum();
        }

//        set Customer
        tvCustomerName = (TextView)findViewById(R.id.tvCustomerName);
        tvCustomerPhone = (TextView)findViewById(R.id.tvCustomerPhone);
        tvCustomerEmail = (TextView)findViewById(R.id.tvCustomerEmail);
        tvCustomerBill = (TextView)findViewById(R.id.tv_user_bill);

        if (customerProfile.get_id()!=-1){

            tvCustomerName.setText(customerProfile.getName());
            tvCustomerPhone.setText(customerProfile.getPhone());
            tvCustomerEmail.setText(customerProfile.getEmail());
            tvCustomerBill.setText(String.valueOf(customerProfile.getBill()));

            customerAlbum = dbHandler.getAllPicturesByUserID(customerProfile.get_id());
            setCustomerPics();

        }

        mRemainder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    customerProfile.setRemainder(1);
                    dbHandler.updateCustomer(customerProfile);
                }else{
                    dbHandler.updateCustomer(customerProfile);
                }
            }
        });
        mRemainder.setChecked(customerProfile.getRemainder()==1);

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

        if (!customerAlbum.isEmpty()) {

            for (int i = 0; i < customerAlbum.size() && i<5; i++){

                if(customerAlbum.get(i).getBitmapImageData()!=null)
                switch (i){
                    case 0:
                        tempCustomerPic = (ImageView) findViewById(R.id.customerMainPic);
                        tempCustomerPic.setImageBitmap(customerAlbum.get(0).getBitmapImageData());
                        break;
                    case 1:
                        tempCustomerPic = (ImageView) findViewById(R.id.customerPic_1);
                        tempCustomerPic.setImageBitmap(customerAlbum.get(1).getBitmapImageData());
                        break;
                    case 2:
                        tempCustomerPic = (ImageView) findViewById(R.id.customerPic_2);
                        tempCustomerPic.setImageBitmap(customerAlbum.get(2).getBitmapImageData());
                        break;
                    case 3:
                        tempCustomerPic = (ImageView) findViewById(R.id.customerPic_3);
                        tempCustomerPic.setImageBitmap(customerAlbum.get(3).getBitmapImageData());
                        break;
                    case 4:
                        tempCustomerPic = (ImageView) findViewById(R.id.customerPic_4);
                        tempCustomerPic.setImageBitmap(customerAlbum.get(4).getBitmapImageData());
                        break;

                    default:
//                        Toast.makeText(this, "No Pics", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

        }/*else{
            Toast.makeText(this, "List is Empty", Toast.LENGTH_SHORT).show();
        }*/

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
//                tempCustomerPic = (ImageButton)findViewById(R.id.customerMainPic);
                showUserPhoto(R.id.customerMainPic,0);
                break;
            case R.id.customerPic_1:
//                tempCustomerPic = (ImageButton)findViewById(R.id.customerPic_1);
                showUserPhoto(R.id.customerPic_1,1);
                break;
            case R.id.customerPic_2:
//                tempCustomerPic = (ImageButton)findViewById(R.id.customerPic_2);
                showUserPhoto(R.id.customerPic_2,2);
                break;
            case R.id.customerPic_3:
//                tempCustomerPic = (ImageButton)findViewById(R.id.customerPic_3);
                showUserPhoto(R.id.customerPic_3,3);
                break;
            case R.id.customerPic_4:
//                tempCustomerPic = (ImageButton)findViewById(R.id.customerPic_4);
                showUserPhoto(R.id.customerPic_4,4);
//                Toast.makeText(this, "Pic 4", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ibEditCustomer:
                myIntent = new Intent(this,NewCustomerActivity.class);
                myIntent.putExtra(CustomersDBConstants.CUSTOMER_ID,customerProfile.get_id());
                startActivity(myIntent);
                break;
            case R.id.btClose:
                this.finish();
                break;
            default:
                tempCustomerPic = (ImageView) findViewById(R.id.customerMainPic);
                Toast.makeText(this, R.string.not_initialized_yet, Toast.LENGTH_LONG).show();
                break;
        }
    }
    @Override
    public boolean onLongClick(View v) {
//        Toast.makeText(this, "LongClick", Toast.LENGTH_SHORT).show();
        switch (v.getId()) {

            case R.id.customerMainPic:
//                tempCustomerPic = (ImageButton)findViewById(R.id.customerMainPic);
                picIndex = 0;
                userPhotoSet(R.id.customerMainPic);
                break;
            case R.id.customerPic_1:
//                tempCustomerPic = (ImageButton)findViewById(R.id.customerPic_1);
                picIndex = 1;
                userPhotoSet(R.id.customerPic_1);
                break;
            case R.id.customerPic_2:
//                tempCustomerPic = (ImageButton)findViewById(R.id.customerPic_2);
                picIndex = 2;
                userPhotoSet(R.id.customerPic_2);
                break;
            case R.id.customerPic_3:
//                tempCustomerPic = (ImageButton)findViewById(R.id.customerPic_3);
                picIndex = 3;
                userPhotoSet(R.id.customerPic_3);
                break;
            case R.id.customerPic_4:
//                tempCustomerPic = (ImageButton)findViewById(R.id.customerPic_4);
                picIndex = 4;
                userPhotoSet(R.id.customerPic_4);
//                Toast.makeText(this, "Pic 4", Toast.LENGTH_SHORT).show();
                break;

            default:
                tempCustomerPic = (ImageView) findViewById(R.id.customerMainPic);
                Toast.makeText(this, R.string.not_initialized_yet, Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    private void showUserPhoto(int customerPicId ,int position) {

        tempCustomerPic = (ImageView) findViewById(customerPicId);
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

//        ArrayList<Picture> allCustomerPics = dbHandler.getAllPicturesByUserID(customerProfile.get_id());

        if (!customerAlbum.isEmpty()) {
            tempCustomerPic = (ImageView) view.findViewById(R.id.dialogImageView);
            try {
                tempCustomerPic.setImageBitmap(customerAlbum.get(position).getBitmapImageData());
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
        tempCustomerPic = (ImageView) findViewById(customerPicId);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    0);
        }else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
//            TODO Fix pic replacement need an id
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
                    savePic(data);
                }
                break;

            default:

                break;
        }
    }

    private void savePic(Intent data) {

//        Picture myPicture = new Picture(customerAlbum.get(picIndex).getId(),Calendar.getInstance(),selectedProfilePicture,customerProfile.get_id());
//
//        if(picIndex<customerAlbum.size()){
//           while (picIndex<=customerAlbum.size())customerAlbum.add(new Picture());
//        }
        Bitmap picToSave = null;

        try {
            picToSave = (Bitmap) data.getExtras().get("data");

            if(picIndex < customerAlbum.size()) {
                customerAlbum.get(picIndex).setName(Calendar.getInstance());
                customerAlbum.get(picIndex).setBitmapImageData(picToSave);
                dbHandler.updatePicture(customerAlbum.get(picIndex));
                tempCustomerPic.setImageBitmap(customerAlbum.get(picIndex).getBitmapImageData());

            }else {
                Picture myPicture = new Picture(
                        Calendar.getInstance(),(Bitmap) data.getExtras().get("data"),customerProfile.get_id());


                if(!dbHandler.addPicture(myPicture)){
                    Toast.makeText(this, R.string.failedToSave, Toast.LENGTH_SHORT).show();
                }else {
                    customerAlbum.add(myPicture);
                    Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
                }

                tempCustomerPic.setImageBitmap(customerAlbum.get(customerAlbum.size()-1).getBitmapImageData());

            }



        } catch (NullPointerException e) {
            e.printStackTrace();
        }





//        if(dbHandler.updatePicture(customerAlbum.get(picIndex))){
//            Toast.makeText(this, R.string.saved, Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(this, R.string.failedToSave, Toast.LENGTH_SHORT).show();
//        }

//        customerAlbum.get(picIndex).getId();

//        if(dbHandler.addPicture(myPicture)){
//            Toast.makeText(this, R.string.picture_saved, Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(this, R.string.picture_didnt_saved, Toast.LENGTH_SHORT).show();
//        }

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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        customerProfile = dbHandler.getCustomerByID(customerProfile.get_id());
        tvCustomerName.setText(customerProfile.getName());
        tvCustomerPhone.setText(customerProfile.getPhone());
        tvCustomerEmail.setText(customerProfile.getEmail());
        customerAlbum = dbHandler.getAllPicturesByUserID(customerProfile.get_id());
        setCustomerPics();
    }
}
