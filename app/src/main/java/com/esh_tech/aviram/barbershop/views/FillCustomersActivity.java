package com.esh_tech.aviram.barbershop.views;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.esh_tech.aviram.barbershop.data.*;

import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;
import com.esh_tech.aviram.barbershop.R;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.USER_AUTO_LOGIN;
import static com.esh_tech.aviram.barbershop.Constants.UserDBConstants.USER_IS_REGISTER;

public class FillCustomersActivity extends AppCompatActivity {

    private int MY_PERMISSIONS_REQUEST_IMPORT_CONTACT = 1;
    private static final String TAG = "Aviram";//FillCustomersActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;     // contacts unique ID

    ArrayList<Customer> allCustomers =new ArrayList<Customer>();
    ListView customerListView;
//    FillCustomersActivity.MyCustomersAdapter usersAdapter;

    //    Database
    BarbershopDBHandler dbHandler;

    //    SharedPreferences
    SharedPreferences settings;
    SharedPreferences.Editor editor;

    MyCustomerAdapter customersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_customers);

        init();

    }

    private void init() {
//        Database
        dbHandler = new BarbershopDBHandler(this);
        allCustomers = dbHandler.getAllCustomers();
        //        Connect list view
        customerListView =(ListView)findViewById(R.id.fillCustomersLv);

        //        Connect adapter with custom view
//        usersAdapter = new MyCustomersAdapter(this,R.layout.custom_contact_row,allCustomers);

//        customerListView.setAdapter(usersAdapter);

        customersAdapter = new MyCustomerAdapter(this,
                R.layout.custom_contact_row,
                allCustomers);
        customerListView.setAdapter(customersAdapter);
    }

    //allCustomers.add(new Customer(retrieveContactName(),"050-342-3242","aaa@mmm.com",true));
    public void goMain(View view) {
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean register = settings.getBoolean(USER_IS_REGISTER, false);
        if(!register) {
            editor = settings.edit();
            editor.putBoolean(USER_IS_REGISTER,true);
            editor.apply();
        }

        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        this.finish();
    }

    //Creating custom Adapter for the list view GUI
//    class MyCustomersAdapter extends ArrayAdapter<Customer> {
//
//        public MyCustomersAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Customer> objects) {
//            super(context, resource, objects);
//        }
//
//        @NonNull
//        @Override
//        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//            Customer customer = getItem(position);
//
//            if (convertView == null){
//                Log.e("Test get view","inside if with position"+position);
//                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_contact_row,parent,false);
//            }
//
//            TextView tvName = (TextView) convertView.findViewById(R.id.customerNameET);
//            TextView tvPhone = (TextView)convertView.findViewById(R.id.customerPhoneEt);
//            ImageView customerIcon = (ImageView)convertView.findViewById(R.id.customerIconIv);
//
//
//
//            //Data
//            tvName.setText(customer.getName());
//            tvPhone.setText(customer.getPhone());
//
//            if(customer.getGender()==1) {
//                customerIcon.setImageResource(R.drawable.usermale48);
//            }else {
//                customerIcon.setImageResource(R.drawable.userfemale48);
//            }
//
//            if(customer.getPhoto() != null) {
//                customerIcon.setImageBitmap(customer.getPhoto());
//            }
//            return convertView;
//        }
//    }

    public void onClickSelectContact(View btnSelectContact) {

        // using native contacts selection
        // Intent.ACTION_PICK = Pick an item from the data, returning what was selected.

        //        Check Permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_IMPORT_CONTACT);
        }else startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Customer newCustomer =new Customer();

            if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
                Log.d(TAG, "Response: " + data.toString());
                uriContact = data.getData();

                newCustomer.setName(retrieveContactName());
                newCustomer.setPhone(retrieveContactNumber());
                Bitmap photo = retrieveContactPhoto();

                try {
                    newCustomer.setPhoto(photo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                Toast.makeText(this, newCustomer.getName()+" :"+newCustomer.getPhone(), Toast.LENGTH_SHORT).show();

//                allCustomers.add(newCustomer);

                if(dbHandler.getCustomerByPhone(newCustomer.getPhone())== null) {
                    if (dbHandler.addCustomer(newCustomer)) {
                        allCustomers.add(newCustomer);
//                        usersAdapter.notifyDataSetChanged();
//                        allCustomers.add(newCustomer);
                        customersAdapter.notifyDataSetChanged();
                        Toast.makeText(this, newCustomer.getName() + R.string.saved, Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(this, newCustomer.getName() + R.string.failedToSave, Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, R.string.customerExist, Toast.LENGTH_SHORT).show();
                }

            }
    }

    //    Customer Data
    private String retrieveContactName() {


        String contactID = null;
        String contactName = null;
//        String contactPhone = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactID = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            Toast.makeText(this, "contact name is " + contactName, Toast.LENGTH_SHORT).show();

            cursor.close();
///*//            Contact number
//            String contactNumber = null;
//
//            // getting contacts ID
//            Cursor cursorID = getContentResolver().query(uriContact,
//                    new String[]{ContactsContract.Contacts._ID},
//                    null, null, null);
//
//            if (cursorID.moveToFirst()) {
//
//                contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
//            }
//
//            cursorID.close();
//
//            Log.d(TAG, "Contact ID: " + contactID);
//
//            // Using the contact ID now we will get contact phone number
//            Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
//
//                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
//                            ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
//                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
//
//                    new String[]{contactID},
//                    null);
//
//            if (cursorPhone.moveToFirst()) {
//                contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            }
//
//            cursorPhone.close();
//
//            Log.d(TAG, "Contact Phone Number: " + contactNumber);
//
//            Toast.makeText(this, "Contact Phone Number: " + contactNumber, Toast.LENGTH_LONG).show();*/
////            Toast.makeText(this, "Contact Name : " + contactName, Toast.LENGTH_LONG).show();
        }



        Log.d(TAG, "Contact Name: " + contactName);
        return contactName;

    }
    private Bitmap retrieveContactPhoto() {

        Bitmap photo = null;

        try {
            InputStream inputStream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),
                    ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(contactID)));

            if (inputStream != null) {
                photo = BitmapFactory.decodeStream(inputStream);
                ImageView imageView = (ImageView) findViewById(R.id.img_contact);
                imageView.setImageBitmap(photo);
            }

            assert inputStream != null;
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return photo;
    }
    private String retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();

        Log.d(TAG, "Contact Phone Number: " + contactNumber);
//        Toast.makeText(this, "Contact Phone Number: " + contactNumber, Toast.LENGTH_LONG).show();
        return contactNumber;
    }



    @Override
    protected void onResume() {
        super.onResume();
        customersAdapter.notifyDataSetChanged();
    }
}
