package com.esh_tech.aviram.barbershop.views;

import com.esh_tech.aviram.barbershop.data.*;
import com.esh_tech.aviram.barbershop.R;
import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.CustomersDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CustomersListActivity extends AppCompatActivity {

    private int MY_PERMISSIONS_REQUEST_IMPORT_CONTACT = 2;
    private static final String TAG = "Aviram";//FillCustomersActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;     // contacts unique ID

    ArrayList<Customer> allCustomers =new ArrayList<Customer>();
    ListView customerListView;
    MyCustomersAdapter usersAdapter;

//    Database
    BarbershopDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        init();

    }

    private void init() {

//        database
        dbHandler = new BarbershopDBHandler(this);


//        Connect list view
        customerListView = (ListView) findViewById(R.id.customersLv);

//        fill components
        populateCustomers();

//        Connect adapter with custom view
        usersAdapter = new MyCustomersAdapter(this, R.layout.custom_contact_row, allCustomers);

        customerListView.setAdapter(usersAdapter);



        customerListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        viewCustomer(allCustomers.get(position));
                    }
                }
        );
        customerListView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                        Toast.makeText(CustomersListActivity.this, "Edit customer", Toast.LENGTH_LONG).show();
                        editCustomer(allCustomers.get(position));
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent mainIntent =new Intent(getApplicationContext(),MainActivity.class);
        startActivity(mainIntent);
        return true;

    }

    private void viewCustomer(Customer customer) {
        Intent customerProfile = new Intent(this,CustomerActivity.class);
        customerProfile.putExtra("customerId",customer.get_id());
        startActivity(customerProfile);
        this.finish();
    }

    private void editCustomer(final Customer customer) {
        //alert box with Edit / delete / new appointment

            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

            mBuilder.setTitle(R.string.dialogManageCustomer);

        mBuilder.setNeutralButton(R.string.deleteCustomer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dbHandler.deleteCustomerById(customer.get_id())){
                    Toast.makeText(CustomersListActivity.this, "Customer Deleted", Toast.LENGTH_SHORT).show();
                    allCustomers = dbHandler.getAllCustomers();
                    usersAdapter.notifyDataSetChanged();
                }

            }
        });

//            mBuilder.setNeutralButton(R.string.edit, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(CustomersListActivity.this, R.string.edit, Toast.LENGTH_LONG).show();
//                    Intent myIntent = new Intent(CustomersListActivity.this,NewCustomerActivity.class);
//                    myIntent.putExtra(CustomersDBConstants.CUSTOMER_ID,customer.get_id());
//                    startActivity(myIntent);
//                    CustomersListActivity.this.finish();
//                }
//            });


            mBuilder.setNegativeButton(R.string.newAppointment, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent myIntent = new Intent(CustomersListActivity.this,NewAppointmentActivity.class);
                    myIntent.putExtra(CustomersDBConstants.CUSTOMER_ID,customer.get_id());
                    startActivity(myIntent);
                    CustomersListActivity.this.finish();

                }
            });


            AlertDialog dialog = mBuilder.create();
            dialog.show();


    }

    //Testing customer list
    private void populateCustomers() {
        allCustomers = dbHandler.getAllCustomers();
//        lvProducts.deferNotifyDataSetChanged();


    }

    // *Create new customer
    public void openNewCustomer(View view) {
        Intent myIntent = new Intent(this ,NewCustomerActivity.class);
        startActivity(myIntent);
        this.finish();
    }

    //Listener

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
            TextView tvPhone = (TextView)convertView.findViewById(R.id.customerPhoneEt);
            ImageView customerIcon = (ImageView)convertView.findViewById(R.id.customerIconIv);


            //Data
            tvName.setText(customer.getName());
            tvPhone.setText(String.valueOf(customer.getPhone()));

            customer.setPhoto(dbHandler.getUserPictureByID(customer.get_id()));

            if (customer.get_id()!= -1 && customer.getPhoto() != null) {
                customerIcon.setImageBitmap(customer.getPhoto());
            }else {
                if (customer.getGender() == 1) customerIcon.setImageResource(R.drawable.usermale48);
                else customerIcon.setImageResource(R.drawable.userfemale48);
            }

            usersAdapter.notifyDataSetChanged();

            return convertView;
        }
    }


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
            Bitmap photo =retrieveContactPhoto();
            if(photo == null){

            }else{
//                    newCustomer.setCustomerPhoto(photo);
            }
//                Toast.makeText(this, newCustomer.getName()+" :"+newCustomer.getPhone(), Toast.LENGTH_SHORT).show();

//                allCustomers.add(newCustomer);
            if(dbHandler.getCustomerByPhone(newCustomer.getPhone())== null) {
                if (dbHandler.addCustomer(newCustomer)) {
                    allCustomers.add(newCustomer);
                    usersAdapter.notifyDataSetChanged();
                    Toast.makeText(this, newCustomer.getName() + " Saved.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, newCustomer.getName() + " Didn't Saved.", Toast.LENGTH_SHORT).show();
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

/*//            Contact number
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

            Toast.makeText(this, "Contact Phone Number: " + contactNumber, Toast.LENGTH_LONG).show();*/
//            Toast.makeText(this, "Contact Name : " + contactName, Toast.LENGTH_LONG).show();
        }



        Log.d(TAG, "Contact Name: " + contactName);

//        Toast.makeText(this, contactName+" "+contactPhone, Toast.LENGTH_SHORT).show();
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
}
