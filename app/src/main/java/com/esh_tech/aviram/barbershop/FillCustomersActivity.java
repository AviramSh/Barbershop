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

import com.esh_tech.aviram.barbershop.Codes.Customer;
import com.esh_tech.aviram.barbershop.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FillCustomersActivity extends AppCompatActivity {

    private static final String TAG = "Aviram";//FillCustomersActivity.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;     // contacts unique ID

    ArrayList<Customer> allCustomers =new ArrayList<Customer>();
    ListView customerListView;
    FillCustomersActivity.MyCustomersAdapter usersAdaper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_customers);

        //        Connect list view
        customerListView =(ListView)findViewById(R.id.fillCustomersLv);

        //        Connect adapter with custom view
        usersAdaper = new FillCustomersActivity.MyCustomersAdapter(this,R.layout.custom_contact_row,allCustomers);

        customerListView.setAdapter(usersAdaper);


    }
    //allCustomers.add(new Customer(retrieveContactName(),"050-342-3242","aaa@mmm.com",true));
    public void goMain(View view) {
        Intent mainIntent = new Intent(this,MainActivity.class);
        startActivity(mainIntent);
        this.finish();
    }



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
}
