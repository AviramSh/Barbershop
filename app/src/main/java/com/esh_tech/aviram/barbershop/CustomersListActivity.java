package com.esh_tech.aviram.barbershop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

public class CustomersListActivity extends AppCompatActivity {

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
        this.setTitle(R.string.customers);

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
                        return false;
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
    }

    private void editCustomer(final Customer customer) {
        //alert box with Edit / delete / new appointment

            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);

            mBuilder.setTitle(R.string.dialogManageCustomer);

            mBuilder.setNeutralButton(R.string.edit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(CustomersListActivity.this, R.string.edit, Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(CustomersListActivity.this,NewCustomerActivity.class);
                    myIntent.putExtra(CustomersDBConstants.CUSTOMER_ID,customer.get_id());
                    startActivity(myIntent);
                    CustomersListActivity.this.finish();
                }
            });


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

            if(customer.getGender()==1)customerIcon.setImageResource(R.drawable.usermale48);
            else customerIcon.setImageResource(R.drawable.userfemale48);

            usersAdapter.notifyDataSetChanged();

            return convertView;
        }
    }
}
