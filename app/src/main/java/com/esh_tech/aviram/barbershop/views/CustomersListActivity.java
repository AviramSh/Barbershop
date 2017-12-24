package com.esh_tech.aviram.barbershop.views;

import com.esh_tech.aviram.barbershop.Constants.BundleConstants;
import com.esh_tech.aviram.barbershop.Constants.UserDBConstants;
import com.esh_tech.aviram.barbershop.Utils.DateUtils;
import com.esh_tech.aviram.barbershop.Utils.MailUtils;
import com.esh_tech.aviram.barbershop.data.*;
import com.esh_tech.aviram.barbershop.R;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Constants.CustomersDBConstants;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

public class CustomersListActivity extends AppCompatActivity {

    private int MY_PERMISSIONS_REQUEST_IMPORT_CONTACT = 2;
    public static final int WRITE_EXTERNAL_STORAGE_REQUEST = 145;
    private static final String TAG = "Aviram";//FillCustomersActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;     // contacts unique ID

    EditText customerFilterText;
    MyCustomerAdapter customersAdapter;

    ImageButton ibNext;

    ArrayList<Customer> allCustomers =new ArrayList<Customer>();
    ListView customerListView;
//    MyCustomersAdapter usersAdapter;

//    Database
    BarbershopDBHandler dbHandler;

    String reportEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);
        init();

    }

    private void init() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        database
        dbHandler = new BarbershopDBHandler(this);
        allCustomers = dbHandler.getAllCustomers();
////        fill components
//        populateCustomers();


//        Full Adapter
        customerListView = (ListView)findViewById(R.id.customersLv);
        customerFilterText = (EditText)findViewById(R.id.customerFilterText);

        customersAdapter = new MyCustomerAdapter(this,
                R.layout.custom_contact_row,
                allCustomers);
        customerListView.setAdapter(customersAdapter);

        customerFilterText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customersAdapter.getFilter().filter(s.toString());
            }
        });

////        Connect list view
//        customerListView = (ListView) findViewById(R.id.customersLv);
////        Connect adapter with custom view
//        usersAdapter = new MyCustomersAdapter(this, R.layout.custom_contact_row, allCustomers);
//        customerListView.setAdapter(usersAdapter);
        final SharedPreferences settings;

        settings = PreferenceManager.getDefaultSharedPreferences(this);
        reportEmail = settings.getString(UserDBConstants.USER_EMAIL,"");

        if(!settings.getBoolean(UserDBConstants.USER_IS_REGISTER,false)) {
            ibNext = findViewById(R.id.ib_next);

            ibNext.setVisibility(View.VISIBLE);
            ibNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor;//To add values
                    editor = settings.edit();
                    editor.putBoolean(UserDBConstants.USER_IS_REGISTER,true);
                    editor.apply();

                    Intent intent = new Intent(CustomersListActivity.this,MainActivity.class);
                    startActivity(intent);
                    CustomersListActivity.this.finish();
                }
            });

        }/*else{

        }*/


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

//        test result

        Intent userIdIntent = getIntent();

        Bundle bundle = userIdIntent.getExtras();

        try {

            if(bundle.getInt(BundleConstants.GET_CUSTOMER)==0){

                customerListView.setOnItemClickListener(
                        new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                returnUser(position);
                            }
                        }
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void returnUser(int position) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        bundle.putInt(BundleConstants.GET_CUSTOMER,allCustomers.get(position).get_id());
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);

        finish();
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
    }

    private void editCustomer(final Customer customer) {
        //alert box with Edit / delete / new appointment

            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

            mBuilder.setTitle(R.string.dialogManageCustomer);

        mBuilder.setNeutralButton(R.string.deleteCustomer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(dbHandler.deleteCustomerById(customer.get_id())){
                    Toast.makeText(CustomersListActivity.this, R.string.customer_deleted, Toast.LENGTH_SHORT).show();
                    allCustomers = dbHandler.getAllCustomers();
                    populateCustomers();
                    customersAdapter.notifyDataSetChanged();

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
        customersAdapter = new MyCustomerAdapter(this,
                R.layout.custom_contact_row,
                allCustomers);
        customerListView.setAdapter(customersAdapter);
//        customerListView.deferNotifyDataSetChanged();


    }

    // *Create new customer
    public void openNewCustomer(View view) {
        Intent myIntent = new Intent(this ,NewCustomerActivity.class);
        startActivity(myIntent);
        this.finish();
    }

    //Listener

//    //Creating custom Adpter for the list view GUI
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
//            //Data
//            tvName.setText(customer.getName());
//            tvPhone.setText(String.valueOf(customer.getPhone()));
//
//            customer.setPhoto(dbHandler.getUserPictureByID(customer.get_id()));
//
//            if (customer.get_id()!= -1 && customer.getPhoto() != null) {
//                customerIcon.setImageBitmap(customer.getPhoto());
//            }else {
//                if (customer.getGender() == 1) customerIcon.setImageResource(R.drawable.usermale48);
//                else customerIcon.setImageResource(R.drawable.userfemale48);
//            }
//
//            usersAdapter.notifyDataSetChanged();
//
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
            newCustomer.setPhoto(retrieveContactPhoto());
//            Bitmap photo =retrieveContactPhoto();
//                Toast.makeText(this, newCustomer.getName()+" :"+newCustomer.getPhone(), Toast.LENGTH_SHORT).show();

//                allCustomers.add(newCustomer);
            if(dbHandler.getCustomerByPhone(newCustomer.getPhone())== null) {
                if (dbHandler.addCustomer(newCustomer)) {

                    if(newCustomer.getPhoto() != null){
                        dbHandler.addPicture(new Picture(
                                Calendar.getInstance(),
                                        newCustomer.getPhoto(),
                                dbHandler.getCustomerByPhone(newCustomer.getPhone()).get_id()));
                    }

                    allCustomers.add(newCustomer);
                    populateCustomers();
//                    customersAdapter.notifyDataSetChanged();
                    Toast.makeText(this, newCustomer.getName() + " Saved.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, newCustomer.getName() + " Didn't Saved.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, R.string.customerExist, Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(this, "Failed to import contact", Toast.LENGTH_SHORT).show();
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

//            Toast.makeText(this, "contact name is " + contactName, Toast.LENGTH_SHORT).show();

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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        populateCustomers();
    }

    public void createReport(View view) {
        if(!reportEmail.equals("")) {
            if (allCustomers.size() > 0) {
                new GenerateReportTask(allCustomers).execute();
            } else {
                Toast.makeText(CustomersListActivity.this, R.string.customers_list_is_empty, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(CustomersListActivity.this, R.string.report_filed, Toast.LENGTH_LONG).show();
        }

    }


    @SuppressLint("StaticFieldLeak")
    class GenerateReportTask extends AsyncTask<Void, Void, Boolean> {

        ArrayList<Customer> customersList;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CustomersListActivity.this);
            pd.show();
        }

        public GenerateReportTask(ArrayList<Customer> CustomersList) {
            this.customersList = CustomersList;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pd.dismiss();
            if (result)
                Toast.makeText(CustomersListActivity.this, R.string.report_generated, Toast.LENGTH_LONG).show();
            else
                Toast.makeText(CustomersListActivity.this, R.string.report_failure, Toast.LENGTH_LONG).show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            if (ContextCompat.checkSelfPermission(CustomersListActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CustomersListActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        WRITE_EXTERNAL_STORAGE_REQUEST);
            } else {

                File folder = new File(Environment.getExternalStorageDirectory() + "/Barbershop");

                boolean var = false;
                if (!folder.exists())
                    var = folder.mkdir();


                final String filename = folder.toString() + "/" + "Report1.csv";

                try {

                    PrintWriter writer = new PrintWriter(filename, "UTF-8");
                    writer.write('\ufeff');
                    writer.println(" ID , Name , Phone , Bill , Gender , Birthday ");

                    double calc = 0;

                    for (Customer customer : customersList) {

                        writer.println(
                                customer.get_id() + "," +
                                        customer.getName() + "," +
                                        customer.getPhone() + "," +
                                        customer.getBill() + "," +
                                        customer.getGender() + "," +
                                        customer.getBirthday());

                    }
//                    writer.println(
//                            "Total" + "," +
//                                    "" + "," +
//                                    "" + "," +
//                                    "" + "," +
//                                    "" + "," +
//                                    calc);

                    writer.close();

                    MailUtils.sendMail(CustomersListActivity.this, reportEmail, filename);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }

            return true;

        }
    }
}
