package com.esh_tech.aviram.barbershop;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.io.IOException;
import java.io.InputStream;
import com.esh_tech.aviram.barbershop.Codes.Customer;

import java.util.ArrayList;
import java.util.List;

public class FillCustomersActivity extends AppCompatActivity {

    private static final String TAG = "Aviram";//FillCustomersActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 100;
    private Uri uriContact;
    private String contactID;     // contacts unique ID

    ArrayList<Customer> allCustomers =new ArrayList<Customer>();
    ListView customerListView;
    FillCustomersActivity.MyCustomersAdapter usersAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_customers);

        //        Connect list view
        customerListView =(ListView)findViewById(R.id.fillCustomersLv);

        //        Connect adapter with custom view
        usersAdapter = new FillCustomersActivity.MyCustomersAdapter(this,R.layout.custom_contact_row,allCustomers);

        customerListView.setAdapter(usersAdapter);


    }
    //allCustomers.add(new Customer(retrieveContactName(),"050-342-3242","aaa@mmm.com",true));
/*
    public void goMain(View view) {
        Intent mainIntent = new Intent(this,MainActivity.class);
        startActivity(mainIntent);
        this.finish();
    }
*/



    //Creating custom Adpter for the list view GUI
    class MyCustomersAdapter extends ArrayAdapter<Customer> {

        public MyCustomersAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Customer> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            Customer customer = getItem(position);

            if (convertView == null){
                Log.e("Test get view","inside if with position"+position);
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_contact_row,parent,false);
            }

            TextView tvName = (TextView) convertView.findViewById(R.id.customerNameET);
            TextView tvLastname = (TextView) convertView.findViewById(R.id.customerLastNameEt);
            TextView tvPhone = (TextView)convertView.findViewById(R.id.customerPhoneEt);
            ImageView customerIcon = (ImageView)convertView.findViewById(R.id.customerIconIv);


            //Data
            tvName.setText(customer.getName());
            tvLastname.setText(customer.getName());
            tvPhone.setText(customer.getPhone());
            //customerIcon.setImageBitmap();

            if(customer.isGender())customerIcon.setImageResource(R.drawable.usermale48);
            else customerIcon.setImageResource(R.drawable.userfemale48);

            return convertView;
        }
    }






    public void onClickSelectContact(View btnSelectContact) {

        // using native contacts selection
        // Intent.ACTION_PICK = Pick an item from the data, returning what was selected.
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

//            retrieveContactName();
            retrieveContactNumber();
//            retrieveContactPhoto();
//            retrieveContactNumbers();
        }
    }

    private void retrieveContactPhoto() {

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

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void retrieveContactNumber() {

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
        try {
         /*Using the contact ID now we will get contact phone number*/
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

        }catch (Exception e){
            Log.d(TAG, "Error" + e.toString());
        }
        Log.d(TAG, "Contact Phone Number: " + contactNumber);
    }

    private void retrieveContactName() {

        String contactName = null;

        /*querying contact data store*/
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            /* DISPLAY_NAME = The display name for the contact.
             HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.*/

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();

        Log.d(TAG, "Contact Name: " + contactName);

    }

    private void retrieveContactNumbers() {

        String contactNumber = null;
        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {
            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_HOME +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE +
                        ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK +
                        ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME +
                        ContactsContract.CommonDataKinds.Phone.TYPE_PAGER +
                        ContactsContract.CommonDataKinds.Phone.TYPE_OTHER +
                        ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK +
                        ContactsContract.CommonDataKinds.Phone.TYPE_CAR +
                        ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN +
                        ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX +
                        ContactsContract.CommonDataKinds.Phone.TYPE_RADIO +
                        ContactsContract.CommonDataKinds.Phone.TYPE_TELEX +
                        ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD +
                        ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE +
                        ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER +
                        ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MMS,

                new String[]{contactID},
                null);

        while (cursorPhone.moveToNext()){
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
            Log.d(TAG, "Contact Phone Number: " + contactNumber);
        }
        cursorPhone.close();

    }
}



