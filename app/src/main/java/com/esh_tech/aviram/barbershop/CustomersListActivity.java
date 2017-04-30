package com.esh_tech.aviram.barbershop;

import android.content.Context;
import android.content.Intent;
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
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.esh_tech.aviram.barbershop.Codes.Customer;
import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CustomersListActivity extends AppCompatActivity {

    ArrayList<Customer> allCustomers =new ArrayList<Customer>();
    ListView customerListView;
    MyCustomersAdapter usersAdaper;

//    Database
    BarbershopDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

//        database
        dbHandler = new BarbershopDBHandler(this);


//        Connect list view
        customerListView =(ListView)findViewById(R.id.customersLv);

//        fill components
        populateCustomers();

//        Connect adapter with custom view
        usersAdaper = new MyCustomersAdapter(this,R.layout.custom_contact_row,allCustomers);

        customerListView.setAdapter(usersAdaper);



    }

    //Testing customer list
    private void populateCustomers() {


        allCustomers = dbHandler.getAllCustomers();
/*
        allCustomers.add(new Customer("Aviram","Sarabi","0506792353",true));
        allCustomers.add(new Customer("Aviram","Sarabi","0506792353",true));
        allCustomers.add(new Customer("Aviram","Sarabi","0506792353",false));
        allCustomers.add(new Customer("Aviram","Sarabi","0506792353",true));
        allCustomers.add(new Customer("Aviram","Sarabi","0506792353",true));
        allCustomers.add(new Customer("Aviram","Sarabi","0506792353",false));
        allCustomers.add(new Customer("Aviram","Sarabi","0506792353",false));
        allCustomers.add(new Customer("Aviram","Sarabi","0506792353",true));
        allCustomers.add(new Customer("Aviram","Sarabi","0506792353",true));
        allCustomers.add(new Customer("Aviram","Sarabi","0506792353",false));
        allCustomers.add(new Customer("Aviram","Sarabi","0506792353",true));*/
    }

    // *Create new customer
    public void openNewCustomer(View view) {
        Intent myIntent = new Intent(this ,NewCustomerActivity.class);
        startActivity(myIntent);
    }

    //Listener

    //Creating custom Adpter for the list view GUI
    class MyCustomersAdapter extends ArrayAdapter<Customer>{

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
            TextView tvLastname = (TextView) convertView.findViewById(R.id.customerNameET);
            TextView tvPhone = (TextView)convertView.findViewById(R.id.customerNameET);
            ImageView customerIcon = (ImageView)convertView.findViewById(R.id.customerIconIv);


            //Data
            tvName.setText(customer.getName());
            tvPhone.setText(customer.getPhone());

            if(customer.isGender())customerIcon.setImageResource(R.drawable.usermale48);
            else customerIcon.setImageResource(R.drawable.userfemale48);

            return convertView;
        }
    }
}
