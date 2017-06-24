package com.esh_tech.aviram.barbershop;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esh_tech.aviram.barbershop.Database.BarbershopDBHandler;

import java.util.ArrayList;
import java.util.List;


public class StockActivity extends AppCompatActivity implements View.OnClickListener{

    //    Database
    BarbershopDBHandler dbHandler;

    ArrayList<Product> allProducts =new ArrayList<>();
    ListView lvProducts;
    StockActivity.MyProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);


        init();

    }

    private void init() {

        //        Connect list view
        lvProducts =(ListView)findViewById(R.id.fillProductLv);

        //        Database
        dbHandler = new BarbershopDBHandler(this);

//        populateProducts();
        allProducts = dbHandler.getAllProducts();
        //        Connect adapter with custom view
        productsAdapter = new StockActivity.MyProductsAdapter(this,R.layout.custom_product_row,allProducts);

        lvProducts.setAdapter(productsAdapter);

        lvProducts.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){

                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        productHandler(position);
                        return false;
                    }
                }
        );


    }

    //    Handling the Appointment list view
    private void productHandler(int position) {


        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        final View mView = getLayoutInflater().inflate(R.layout.dialog_addproduct,null);

        final EditText mProductName = (EditText)mView.findViewById(R.id.etProductName);
        final EditText mProductQuantity = (EditText)mView.findViewById(R.id.etProductQuantity);
        final EditText mProductPrice = (EditText)mView.findViewById(R.id.etProductPrice);

        Product p1 = dbHandler.getProductByID(position);

        if(p1 != null) {
            Toast.makeText(this, "My pos :"+position, Toast.LENGTH_SHORT).show();
            Product editProduct = dbHandler.getProductByID(position);
            mProductName.setText(editProduct.getName());
            mProductQuantity.setText(String.valueOf(editProduct.getQuantity()));
            mProductPrice.setText(String.valueOf(editProduct.getPrice()));
        }else{
            Toast.makeText(this, "Filed to import", Toast.LENGTH_SHORT).show();
        }
        mBuilder.setNeutralButton(R.string.saveBt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String name = mProductName.getText().toString();
                String price = mProductPrice.getText().toString();
                String quantity = mProductQuantity.getText().toString();

                if(name.isEmpty()|| price.isEmpty()|| quantity.isEmpty()){
                    Toast.makeText(StockActivity.this, R.string.fields_are_not_full, Toast.LENGTH_LONG).show();
                }else{
                    if(dbHandler.upDateProduct(new Product(name , Integer.parseInt(quantity) ,Double.parseDouble(price)))){
                        Toast.makeText(StockActivity.this, name + R.string.saved, Toast.LENGTH_LONG).show();
                        allProducts = dbHandler.getAllProducts();
                        productsAdapter.notifyDataSetChanged();
                    }else Toast.makeText(StockActivity.this,  R.string.failedToSave +" "+name, Toast.LENGTH_LONG).show();

                }
            }
        });


        mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(StockActivity.this, R.string.cancel, Toast.LENGTH_LONG).show();
            }
        });


        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }



//    private void populateProducts() {
//
//        allProducts.add(new Product(1, "מסרק", 14, 5));
//        allProducts.add(new Product(1, "מסרק", 14, 5));
//        allProducts.add(new Product(1, "מסרק", 14, 5));
//        allProducts.add(new Product(1, "מסרק", 14, 5));
////        allProducts.add(new Appointment.Product(1, "מסרק", 14, 5));
////        allProducts.add(new Appointment.Product(1, "מסרק", 14, 5));
//
//    }

    public void addProduct(View view) {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        final View mView =getLayoutInflater().inflate(R.layout.dialog_addproduct,null);

        final EditText mProductName = (EditText)mView.findViewById(R.id.etProductName);
        final EditText mProductQuantity = (EditText)mView.findViewById(R.id.etProductQuantity);
        final EditText mProductPrice = (EditText)mView.findViewById(R.id.etProductPrice);

        /*Button mSave = (Button)mView.findViewById(R.id.bSave);
        Button mCancel = (Button)mView.findViewById(R.id.bCancel);*/



        mBuilder.setNeutralButton(R.string.saveBt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String name = mProductName.getText().toString();
                String price = mProductPrice.getText().toString();
                String quantity = mProductQuantity.getText().toString();

                if(name.isEmpty()|| price.isEmpty()|| quantity.isEmpty()){
                    Toast.makeText(StockActivity.this, R.string.fields_are_not_full, Toast.LENGTH_LONG).show();
                }else{
                    if(dbHandler.addProduct(new Product(name , Integer.parseInt(quantity) ,Double.parseDouble(price)))){
                        Toast.makeText(StockActivity.this, name + R.string.saved, Toast.LENGTH_LONG).show();
                        allProducts = dbHandler.getAllProducts();
                        productsAdapter.notifyDataSetChanged();
                    }else Toast.makeText(StockActivity.this,  R.string.failedToSave +" "+name, Toast.LENGTH_LONG).show();

                }
            }
        });


        mBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(StockActivity.this, R.string.cancel, Toast.LENGTH_LONG).show();
                    }
                });


        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ibSave){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            this.finish();
        }
    }


    //Creating custom Adpter for the list view GUI
    class MyProductsAdapter extends ArrayAdapter<Product> {

        public MyProductsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Product> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            Product product = getItem(position);

            if (convertView == null){
                Log.e("Test get view","inside if with position"+position);
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_product_row,parent,false);
            }

            TextView name = (TextView) convertView.findViewById(R.id.tvProductName);
            TextView price = (TextView) convertView.findViewById(R.id.tvProductPrice);
            TextView quantity = (TextView)convertView.findViewById(R.id.tvProductQuantity);


            //Data
            name.setText(product.getName());
            price.setText(String.valueOf(product.getPrice()));
            quantity.setText(String.valueOf(product.getQuantity()));

            return convertView;
        }
    }
}







